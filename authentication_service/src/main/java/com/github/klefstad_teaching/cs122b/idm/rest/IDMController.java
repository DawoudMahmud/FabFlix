package com.github.klefstad_teaching.cs122b.idm.rest;

import com.github.klefstad_teaching.cs122b.core.base.ResultResponse;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.idm.component.IDMAuthenticationManager;
import com.github.klefstad_teaching.cs122b.idm.component.IDMJwtManager;
import com.github.klefstad_teaching.cs122b.idm.model.request.AuthenticateRequestModel;
import com.github.klefstad_teaching.cs122b.idm.model.request.LoginRequestModel;
import com.github.klefstad_teaching.cs122b.idm.model.request.RefreshRequestModel;
import com.github.klefstad_teaching.cs122b.idm.model.request.UserRequest;
import com.github.klefstad_teaching.cs122b.idm.model.response.*;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.util.Validate;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.text.ParseException;
import java.util.UUID;

//define all endpoints in this class
@RestController
public class IDMController
{
    private final IDMAuthenticationManager authManager;
    private final IDMJwtManager            jwtManager;
    private final Validate                 validate;
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public IDMController(IDMAuthenticationManager authManager,
                         IDMJwtManager jwtManager,
                         Validate validate, NamedParameterJdbcTemplate template)
    {
        this.authManager = authManager;
        this.jwtManager = jwtManager;
        this.validate = validate;
        this.template = template;
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> user(@RequestBody UserRequest request)
    {

        authManager.createAndInsertUser(request.getEmail(), request.getPassword());
        RegisterResponse response = new RegisterResponse()
                .setResult(IDMResults.USER_REGISTERED_SUCCESSFULLY);

        return ResponseEntity.status(response.getResult().status())
                            .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseModel> login(
            @RequestBody LoginRequestModel request
    ) throws JOSEException {
        String tryEmail = request.getEmail();
        char[] tryPassword = request.getPassword();
        User newUser = authManager.selectAndAuthenticateUser(tryEmail,tryPassword);
        RefreshToken r = jwtManager.buildRefreshToken(newUser);
        LoginResponseModel login = new LoginResponseModel()
                .setAccessToken(jwtManager.buildAccessToken(newUser))
                .setRefreshToken(r.getToken())
                .setResult(IDMResults.USER_LOGGED_IN_SUCCESSFULLY);
        authManager.insertRefreshToken(r);
        return ResponseEntity.status(login.getResult().status())
                .body(login);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponseModel> authenticate(
            @RequestBody AuthenticateRequestModel request
    )  {
        jwtManager.verifyAccessToken((request.getAccessToken()));
        AuthenticateResponseModel auth = new AuthenticateResponseModel()
                .setResult(IDMResults.ACCESS_TOKEN_IS_VALID);

        return ResponseEntity.status(auth.getResult().status())
                .body(auth);

    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseModel> refresh(
            @RequestBody RefreshRequestModel request
            ) throws JOSEException {
        RefreshResponseModel resp = new RefreshResponseModel();
        String check = request.getRefreshToken();
        if(check.length() != 36) {
            resp.setResult(IDMResults.REFRESH_TOKEN_HAS_INVALID_LENGTH);
            return ResponseEntity.status(resp.getResult().status())
                    .body(resp);
        }
        try {
            UUID u = UUID.fromString(check);
        }
        catch (IllegalArgumentException e)
        {
            resp.setResult(IDMResults.REFRESH_TOKEN_HAS_INVALID_FORMAT);
            return ResponseEntity.status(resp.getResult().status())
                    .body(resp);
        }

        RefreshToken r = authManager.verifyRefreshToken(check); //throws corresponding errors if any
           if (jwtManager.hasExpired(r)) {
               authManager.expireRefreshToken(r);
               resp.setResult(IDMResults.REFRESH_TOKEN_IS_EXPIRED);
           } else {
               authManager.updateRefreshTokenExpireTime(r, jwtManager.getExpireRefresh());
               User u = authManager.getUserFromRefreshToken(r);
               if (r.getExpireTime().isAfter(r.getMaxLifeTime())) {
                   authManager.revokeRefreshToken(r);
                   RefreshToken newRToken = jwtManager.buildRefreshToken(u);
                   String newAToken = jwtManager.buildAccessToken(u);
                   authManager.insertRefreshToken(newRToken);
                    resp.setResult(IDMResults.RENEWED_FROM_REFRESH_TOKEN)
                           .setRefreshToken(newRToken.getToken())
                           .setAccessToken(newAToken);
               } else {
                   authManager.updateDBExpireTime(r);
                   String newAToken = jwtManager.buildAccessToken(u);
                    resp.setRefreshToken(r.getToken())
                           .setResult(IDMResults.RENEWED_FROM_REFRESH_TOKEN)
                           .setAccessToken(newAToken);
                }
           }

        return ResponseEntity.status(resp.getResult().status())
                .body(resp);
    }

}
