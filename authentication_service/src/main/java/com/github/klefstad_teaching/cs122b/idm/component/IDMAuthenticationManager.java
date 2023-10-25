package com.github.klefstad_teaching.cs122b.idm.component;

import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.IDMResults;
import com.gitcodings.stack.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.idm.config.IDMServiceConfig;
import com.github.klefstad_teaching.cs122b.idm.model.request.UserRequest;
import com.github.klefstad_teaching.cs122b.idm.repo.IDMRepo;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.TokenStatus;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.UserStatus;
import com.github.klefstad_teaching.cs122b.idm.rest.IDMController;
import com.github.klefstad_teaching.cs122b.idm.util.Validate;
//import com.sun.org.apache.bcel.internal.generic.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.UUID;

@Component
public class IDMAuthenticationManager
{
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String       HASH_FUNCTION = "PBKDF2WithHmacSHA512";

    private static final int ITERATIONS     = 10000;
    private static final int KEY_BIT_LENGTH = 512;

    private static final int SALT_BYTE_LENGTH = 4;

    public final IDMRepo repo;

    @Autowired
    public IDMAuthenticationManager(IDMRepo repo)
    {
        this.repo = repo;
    }


    private static byte[] hashPassword(final char[] password, String salt)
    {
        return hashPassword(password, Base64.getDecoder().decode(salt));
    }

    private static byte[] hashPassword(final char[] password, final byte[] salt)
    {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_FUNCTION);

            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_BIT_LENGTH);

            SecretKey key = skf.generateSecret(spec);

            return key.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] genSalt()
    {
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }
 //should be using all these functions to make sure logic is complete
    public User selectAndAuthenticateUser(String email, char[] passwordAttempt)
    {
        try {
            // TODO: create new function for check password
            //      Shouldn't be getting a length error on login attempt
            Validate.checkPassword(passwordAttempt);
            Validate.checkEmail(email);
            User potential = repo.selectUser(email);
            if(potential.getUserStatus().id() == 2)
                    throw new ResultError(IDMResults.USER_IS_LOCKED);
            if(potential.getUserStatus().id() == 3)
                throw new ResultError(IDMResults.USER_IS_BANNED);
            String userSalt = potential.getSalt();
            byte[] passwordTry = hashPassword(passwordAttempt, userSalt);
            String p = Base64.getEncoder().encodeToString(passwordTry);
            if(!p.equals(potential.getHashedPassword()))
                throw new ResultError(IDMResults.INVALID_CREDENTIALS);
            return potential;



        } catch(DataAccessException e) {
            throw new ResultError(IDMResults.USER_NOT_FOUND);
        }

    }

    public void createAndInsertUser(String email, char[] password)
    {
        if(repo.checkExist(email))
            throw new ResultError(IDMResults.USER_ALREADY_EXISTS);

        Validate.checkPassword(password);
        Validate.checkEmail(email);

        User toAdd = new User();
        toAdd.setEmail(email);
        int user_status_id = 1;

        byte[] salt = genSalt();
        byte[] hashed_password = hashPassword(password,salt);

        String base64EncodedHashedSalt = Base64.getEncoder().encodeToString(salt);
        String base64EncodedHashedPassword = Base64.getEncoder().encodeToString(hashed_password);

        repo.insertUser(email,base64EncodedHashedSalt,base64EncodedHashedPassword,user_status_id);
    }

    public void insertRefreshToken(RefreshToken refreshToken)
    {
        repo.insertRefresh(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token)
    {
        RefreshToken retrieved = repo.getTokenFromString(token);
        if(retrieved.getTokenStatus().id() == 2)
            throw new ResultError(IDMResults.REFRESH_TOKEN_IS_EXPIRED);
        if(retrieved.getTokenStatus().id() == 3)
            throw new ResultError(IDMResults.REFRESH_TOKEN_IS_REVOKED);
        if(token.length() != 36)
            throw new ResultError(IDMResults.REFRESH_TOKEN_HAS_INVALID_LENGTH);
        try {
            UUID u = UUID.fromString(token); //will throw exception if not in correct format
            return retrieved;
        }
        catch (IllegalArgumentException e)
        {
            throw new ResultError(IDMResults.REFRESH_TOKEN_HAS_INVALID_FORMAT);
        }
    }

    public void updateRefreshTokenExpireTime(RefreshToken token, Duration expireTime)
    {
        token.setExpireTime(Instant.now().plus(expireTime));
    }
    public void updateDBExpireTime(RefreshToken token)
    {
        repo.updateRefreshExpireTime(token);
    }

    public void expireRefreshToken(RefreshToken token)
    {
        token.setTokenStatus(TokenStatus.EXPIRED);
        repo.updateRefreshTokenStatus(token);
    }

    public void revokeRefreshToken(RefreshToken token)
    {
        token.setTokenStatus(TokenStatus.REVOKED);
        repo.updateRefreshTokenStatus(token);

    }

    public User getUserFromRefreshToken(RefreshToken refreshToken)
    {
       return repo.getUserFromRefresh(refreshToken);
    }
}
