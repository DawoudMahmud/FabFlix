package com.github.klefstad_teaching.cs122b.idm.repo;

import com.gitcodings.stack.core.base.ResultResponse;
import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.idm.component.IDMAuthenticationManager;
import com.github.klefstad_teaching.cs122b.idm.model.request.UserRequest;
import com.github.klefstad_teaching.cs122b.idm.model.response.RegisterResponse;
import com.github.klefstad_teaching.cs122b.idm.model.response.UserResponse;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.TokenStatus;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.UserStatus;
import com.mysql.cj.protocol.a.ResultsetRowReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.zip.Checksum;

@Component
public class IDMRepo
{
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public IDMRepo(NamedParameterJdbcTemplate template)
    {
        this.template = template;
    }

    public void insertUser(String email, String salt, String hashed_password, int user_status_id)
    {

        this.template.update(
                "insert into idm.user (email, user_status_id, hashed_password, salt)" +
                        "values (:email, :user_status_id, :hashed_password, :salt)",
                new MapSqlParameterSource()
                        .addValue("email", email, Types.VARCHAR)
                        .addValue("user_status_id", user_status_id, Types.INTEGER)
                        .addValue("hashed_password", hashed_password, Types.VARCHAR)
                        .addValue("salt", salt, Types.VARCHAR)
        );
    }
    public boolean checkExist(String email)
    {
        List<User> users = this.template.query("select id, email, user_status_id, salt, hashed_password " +
                        "from idm.user " +
                        "where email = :email ",
                new MapSqlParameterSource()
                        .addValue("email", email, Types.VARCHAR),
                (rs, rowNum) ->
                        new User()
                                .setId(rs.getInt("id"))
                                .setEmail(rs.getString("email"))
                                .setUserStatus(UserStatus.fromId(rs.getInt("user_status_id")))
                                .setSalt(rs.getString("salt"))
                                .setHashedPassword(rs.getString("hashed_password"))

        );
        return users.size() > 0;
    }
    public User selectUser(String email)
    {
        try{
            return this.template.queryForObject("select id, email, user_status_id, salt, hashed_password " +
                    "from idm.user " +
                    "where email = :email",
            new MapSqlParameterSource()
                    .addValue("email", email, Types.VARCHAR),
                    (rs, rowNum) ->
                            new User()
                                    .setId(rs.getInt("id"))
                                    .setEmail(rs.getString("email"))
                                    .setUserStatus(UserStatus.fromId(rs.getInt("user_status_id")))
                                    .setSalt(rs.getString("salt"))
                                    .setHashedPassword(rs.getString("hashed_password"))

            );
        } catch(DataAccessException e) {
            throw new ResultError(IDMResults.USER_NOT_FOUND);
        }

    }
    public void insertRefresh(RefreshToken refresh)
    {
        this.template.update(
                "insert into idm.refresh_token (token, user_id, token_status_id, expire_time, max_life_time)" +
                        "values (:token, :user_id, :token_status_id, :expire_time, :max_life_time)",
                new MapSqlParameterSource()
                        .addValue("token", refresh.getToken(), Types.VARCHAR)
                        .addValue("user_id", refresh.getUserId(), Types.INTEGER)
                        .addValue("token_status_id", refresh.getTokenStatus().id(), Types.INTEGER)
                        .addValue("expire_time", Timestamp.from(refresh.getExpireTime()), Types.TIMESTAMP)
                        .addValue("max_life_time", Timestamp.from(refresh.getMaxLifeTime()), Types.TIMESTAMP)
        );
    }
    public void updateRefreshTokenStatus(RefreshToken token)
    {
        try {
            this.template.update("update idm.refresh_token " +
                                        "set token_status_id = :token_status_id " +
                                        "where id = :id",
                    new MapSqlParameterSource()
                            .addValue("token_status_id", token.getTokenStatus().id(), Types.INTEGER)
                            .addValue("id", token.getId(), Types.INTEGER)
            );
        }
        catch (DataAccessException e) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    public void updateRefreshExpireTime(RefreshToken token)
    {
        try {
            this.template.update("update idm.refresh_token " +
                            "set expire_time = :expire_time " +
                            "where id = :id",
                    new MapSqlParameterSource()
                            .addValue("expire_time", Timestamp.from(token.getExpireTime()), Types.TIMESTAMP)
                            .addValue("id", token.getId(), Types.INTEGER)
            );
        }
        catch (DataAccessException e) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    public RefreshToken getTokenFromString(String token)
    {
        try {
            return this.template.queryForObject("select id, token, user_id, token_status_id, expire_time, max_life_time " +
                            "from idm.refresh_token " +
                            "where token = :token",
                    new MapSqlParameterSource()
                            .addValue("token", token, Types.VARCHAR),
                    (rs, rowNum) ->
                            new RefreshToken()
                                    .setId(rs.getInt("id"))
                                    .setToken(rs.getString("token"))
                                    .setUserId(rs.getInt("user_id"))
                                    .setTokenStatus(TokenStatus.fromId(rs.getInt("token_status_id")))
                                    .setExpireTime((rs.getTimestamp("expire_time")).toInstant())
                                    .setMaxLifeTime((rs.getTimestamp("max_life_time")).toInstant())

            );
        }
        catch (DataAccessException e){
            throw new ResultError(IDMResults.REFRESH_TOKEN_NOT_FOUND);
        }
    }
    public User getUserFromRefresh(RefreshToken ref)
    {
        try {
            return this.template.queryForObject("select distinct u.id, email, user_status_id, salt, hashed_password " +
                            "from idm.refresh_token rt join idm.user u " +
                            "where :id = u.id",
                    new MapSqlParameterSource()
                            .addValue("id", ref.getUserId(), Types.INTEGER),
                    (rs, rowNum) ->
                            new User()
                                    .setId(rs.getInt("id"))
                                    .setEmail(rs.getString("email"))
                                    .setUserStatus(UserStatus.fromId(rs.getInt("user_status_id")))
                                    .setSalt(rs.getString("salt"))
                                    .setHashedPassword(rs.getString("hashed_password"))

            );
        }
        catch (DataAccessException e){
            throw new ResultError(IDMResults.REFRESH_TOKEN_NOT_FOUND);
        }
    }
    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }
}


