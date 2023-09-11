package com.gitcodings.stack.core.result;

import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public final class BasicResults
{
    private BasicResults() { throw new AssertionError("No Instances of Results can be created"); }

    public static final Result HELLO =
        new Result.Builder()
            .code(10)
            .message("Greeting sent successfully")
            .status(HttpStatus.OK)
            .build();

    public static final Result STRING_SUCCESSFULLY_REVERSED =
        new Result.Builder()
            .code(20)
            .message("String successfully reversed")
            .status(HttpStatus.OK)
            .build();

    public static final Result STRING_IS_BLANK =
        new Result.Builder()
            .code(21)
            .message("String is blank")
            .status(HttpStatus.BAD_REQUEST)
            .build();

    public static final Result STRING_CONTAINS_INVALID_CHARACTERS =
        new Result.Builder()
            .code(22)
            .message("String contains invalid characters")
            .status(HttpStatus.BAD_REQUEST)
            .build();

    public static final Result CALCULATION_SUCCESSFUL =
        new Result.Builder()
            .code(30)
            .message("Calculation successful")
            .status(HttpStatus.OK)
            .build();

    public static final Result DATA_CONTAINS_MISSING_INTEGERS =
        new Result.Builder()
            .code(31)
            .message("Data contains missing integers")
            .status(HttpStatus.BAD_REQUEST)
            .build();

    public static final Result DATA_CONTAINS_INVALID_INTEGERS =
        new Result.Builder()
            .code(32)
            .message("Data contains invalid integers")
            .status(HttpStatus.BAD_REQUEST)
            .build();

    static Stream<Result> toStream()
    {
        return Stream.of(
            HELLO,
            STRING_SUCCESSFULLY_REVERSED,
            STRING_IS_BLANK,
            STRING_CONTAINS_INVALID_CHARACTERS,
            CALCULATION_SUCCESSFUL,
            DATA_CONTAINS_MISSING_INTEGERS,
            DATA_CONTAINS_INVALID_INTEGERS
        );
    }
}
