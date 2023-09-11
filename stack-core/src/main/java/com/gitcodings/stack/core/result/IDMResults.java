package com.gitcodings.stack.core.result;

import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public final class IDMResults
{
    private IDMResults() { throw new AssertionError("No Instances of Results can be created"); }

    public static final Result PASSWORD_DOES_NOT_MEET_LENGTH_REQUIREMENTS =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(1000)
            .message("Password does not meet length requirements")
            .build();

    public static final Result PASSWORD_DOES_NOT_MEET_CHARACTER_REQUIREMENT =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(1001)
            .message("Password does not meet character requirement")
            .build();

    public static final Result EMAIL_ADDRESS_HAS_INVALID_FORMAT =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(1002)
            .message("Email address has invalid format")
            .build();

    public static final Result EMAIL_ADDRESS_HAS_INVALID_LENGTH =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(1003)
            .message("Email address has invalid length")
            .build();

    public static final Result USER_REGISTERED_SUCCESSFULLY =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(1010)
            .message("User registered successfully")
            .build();

    public static final Result USER_ALREADY_EXISTS =
        new Result.Builder()
            .status(HttpStatus.CONFLICT)
            .code(1011)
            .message("User with this email already exists")
            .build();

    public static final Result USER_LOGGED_IN_SUCCESSFULLY =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(1020)
            .message("User logged in successfully")
            .build();

    public static final Result USER_NOT_FOUND =
        new Result.Builder()
            .status(HttpStatus.UNAUTHORIZED)
            .code(1021)
            .message("User not found")
            .build();

    public static final Result INVALID_CREDENTIALS =
        new Result.Builder()
            .status(HttpStatus.FORBIDDEN)
            .code(1022)
            .message("Invalid credentials")
            .build();

    public static final Result USER_IS_LOCKED =
        new Result.Builder()
            .status(HttpStatus.FORBIDDEN)
            .code(1023)
            .message("User is locked")
            .build();

    public static final Result USER_IS_BANNED =
        new Result.Builder()
            .status(HttpStatus.FORBIDDEN)
            .code(1024)
            .message("User is banned")
            .build();

    public static final Result RENEWED_FROM_REFRESH_TOKEN =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(1030)
            .message("AccessToken has been refreshed")
            .build();

    public static final Result REFRESH_TOKEN_IS_EXPIRED =
        new Result.Builder()
            .status(HttpStatus.UNAUTHORIZED)
            .code(1031)
            .message("RefreshToken is expired")
            .build();

    public static final Result REFRESH_TOKEN_IS_REVOKED =
        new Result.Builder()
            .status(HttpStatus.UNAUTHORIZED)
            .code(1032)
            .message("RefreshToken is revoked")
            .build();

    public static final Result REFRESH_TOKEN_NOT_FOUND =
        new Result.Builder()
            .status(HttpStatus.UNAUTHORIZED)
            .code(1033)
            .message("RefreshToken not found")
            .build();

    public static final Result REFRESH_TOKEN_HAS_INVALID_LENGTH =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(1034)
            .message("RefreshToken has invalid length")
            .build();

    public static final Result REFRESH_TOKEN_HAS_INVALID_FORMAT =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(1035)
            .message("RefreshToken has invalid format")
            .build();

    public static final Result ACCESS_TOKEN_IS_VALID =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(1040)
            .message("AccessToken is valid")
            .build();

    public static final Result ACCESS_TOKEN_IS_EXPIRED =
        new Result.Builder()
            .status(HttpStatus.UNAUTHORIZED)
            .code(1041)
            .message("AccessToken is expired")
            .build();

    public static final Result ACCESS_TOKEN_IS_INVALID =
        new Result.Builder()
            .status(HttpStatus.UNAUTHORIZED)
            .code(1042)
            .message("AccessToken is invalid")
            .build();

    static Stream<Result> toStream()
    {
        return Stream.of(
            PASSWORD_DOES_NOT_MEET_LENGTH_REQUIREMENTS,
            PASSWORD_DOES_NOT_MEET_CHARACTER_REQUIREMENT,
            EMAIL_ADDRESS_HAS_INVALID_FORMAT,
            EMAIL_ADDRESS_HAS_INVALID_LENGTH,
            USER_REGISTERED_SUCCESSFULLY,
            USER_ALREADY_EXISTS,
            USER_LOGGED_IN_SUCCESSFULLY,
            USER_NOT_FOUND,
            INVALID_CREDENTIALS,
            USER_IS_LOCKED,
            USER_IS_BANNED,
            RENEWED_FROM_REFRESH_TOKEN,
            REFRESH_TOKEN_IS_EXPIRED,
            REFRESH_TOKEN_IS_REVOKED,
            REFRESH_TOKEN_NOT_FOUND,
            REFRESH_TOKEN_HAS_INVALID_LENGTH,
            REFRESH_TOKEN_HAS_INVALID_FORMAT,
            ACCESS_TOKEN_IS_VALID,
            ACCESS_TOKEN_IS_EXPIRED,
            ACCESS_TOKEN_IS_INVALID
        );
    }
}
