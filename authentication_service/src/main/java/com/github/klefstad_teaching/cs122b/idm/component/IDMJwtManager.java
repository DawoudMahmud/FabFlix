package com.github.klefstad_teaching.cs122b.idm.component;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.idm.config.IDMServiceConfig;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.Role;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.TokenStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class IDMJwtManager
{
    private final JWTManager jwtManager;

    @Autowired
    public IDMJwtManager(IDMServiceConfig serviceConfig)
    {
        this.jwtManager =
            new JWTManager.Builder()
                .keyFileName(serviceConfig.keyFileName())
                .accessTokenExpire(serviceConfig.accessTokenExpire())
                .maxRefreshTokenLifeTime(serviceConfig.maxRefreshTokenLifeTime())
                .refreshTokenExpire(serviceConfig.refreshTokenExpire())
                .build();
    }

    private JWTClaimsSet createClaimSet(User user)
    {
        Instant current = Instant.now();
        Duration expire = jwtManager.getAccessTokenExpire();
        Instant expireTime = current.plus(expire);
        List<Role> userRoles = user.getRoles();
        if(userRoles.isEmpty())
            userRoles = new ArrayList<>(0);

        return new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .expirationTime(Date.from(expireTime))
                .claim(JWTManager.CLAIM_ID, user.getId())    // we set claims like values in a map
                .claim(JWTManager.CLAIM_ROLES, userRoles)
                .issueTime(Date.from(Instant.now()))
                .build();
    }

    private SignedJWT buildAndSignJWT(JWTClaimsSet claimsSet)
        throws JOSEException
    {
        //use create claimsSet function for claim set. Do we use User? How do we get email?
        JWSHeader header =
                new JWSHeader.Builder(JWTManager.JWS_ALGORITHM)
                        .keyID(jwtManager.getEcKey().getKeyID())
                        .type(JWTManager.JWS_TYPE)
                        .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(jwtManager.getSigner());
        return signedJWT;
    }

    private void verifyJWT(SignedJWT jwt) {
        try {
            String serialized = jwt.serialize();
            SignedJWT signedJWT = SignedJWT.parse(serialized);

            signedJWT.verify(jwtManager.getVerifier());
            jwtManager.getJwtProcessor().process(signedJWT, null);

            // Do logic to check if expired manually
            if(Instant.now().isAfter(signedJWT.getJWTClaimsSet().getExpirationTime().toInstant()))
                throw new ResultError(IDMResults.ACCESS_TOKEN_IS_EXPIRED);
        } catch (IllegalStateException | JOSEException | BadJOSEException | ParseException e) {
            //LOG.error("This is not a real token, DO NOT TRUST");
            e.printStackTrace();
            throw new ResultError(IDMResults.ACCESS_TOKEN_IS_INVALID);
        }
    }

    public String buildAccessToken(User user) throws JOSEException {
       JWTClaimsSet jcs = createClaimSet(user);
       SignedJWT sjwt = buildAndSignJWT(jcs);
       jwtManager.getAccessTokenExpire();
       return sjwt.serialize();
    }

    public void verifyAccessToken(String jws) {
        try {
            SignedJWT sjwt = SignedJWT.parse(jws);
            sjwt.verify(jwtManager.getVerifier());
            jwtManager.getJwtProcessor().process(sjwt, null);
            if (Instant.now().isAfter(sjwt.getJWTClaimsSet().getExpirationTime().toInstant()))
                throw new ResultError(IDMResults.ACCESS_TOKEN_IS_EXPIRED);
        }
        catch (IllegalStateException | JOSEException | ParseException| BadJOSEException e)
        {
            e.printStackTrace();
            throw new ResultError(IDMResults.ACCESS_TOKEN_IS_INVALID);
        }
    }

    public RefreshToken buildRefreshToken(User user)
    {
        RefreshToken r = new RefreshToken();
        r.setTokenStatus(TokenStatus.ACTIVE);
        r.setUserId(user.getId());
        r.setToken(generateUUID().toString());
        r.setMaxLifeTime(Instant.now().plus(jwtManager.getMaxRefreshTokenLifeTime()));
        r.setExpireTime(Instant.now().plus(jwtManager.getRefreshTokenExpire()));
        return r;
    }

    public Duration getExpireRefresh() {return jwtManager.getRefreshTokenExpire();}

    public boolean hasExpired(RefreshToken refreshToken)
    {
        return refreshToken.getTokenStatus().id() == 2 || (Instant.now().isAfter(refreshToken.getExpireTime()) || (Instant.now().isAfter(refreshToken.getMaxLifeTime())));
    }

    public boolean needsRefresh(RefreshToken refreshToken) //should this include maxLifeTime?
    {
        return hasExpired(refreshToken) && Instant.now().isBefore(refreshToken.getMaxLifeTime());
    }

    public void updateRefreshTokenExpireTime(RefreshToken refreshToken)
    {
        refreshToken.setExpireTime(Instant.now().plus(jwtManager.getRefreshTokenExpire()));
    }

    private UUID generateUUID()
    {
        return UUID.randomUUID();
    }
}
