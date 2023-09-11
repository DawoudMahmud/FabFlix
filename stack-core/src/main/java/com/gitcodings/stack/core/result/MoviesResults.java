package com.gitcodings.stack.core.result;

import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public final class MoviesResults
{
    private MoviesResults() { throw new AssertionError("No Instances of Results can be created"); }

    public static final Result INVALID_ORDER_BY =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(2000)
            .message("Invalid 'orderBy' value given")
            .build();

    public static final Result INVALID_DIRECTION =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(2001)
            .message("Invalid 'direction' value given")
            .build();

    public static final Result INVALID_LIMIT =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(2002)
            .message("Invalid 'limit' value given")
            .build();

    public static final Result INVALID_PAGE =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(2003)
            .message("Invalid 'offset' value given")
            .build();

    public static final Result MOVIE_WITH_ID_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2010)
            .message("Movie found with the specified ID")
            .build();

    public static final Result NO_MOVIE_WITH_ID_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2011)
            .message("No Movie found for the specified ID")
            .build();

    public static final Result MOVIES_FOUND_WITHIN_SEARCH =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2020)
            .message("Movies with the given search parameters found")
            .build();

    public static final Result NO_MOVIES_FOUND_WITHIN_SEARCH =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2021)
            .message("No movies found with the given search parameters")
            .build();

    public static final Result MOVIES_WITH_PERSON_ID_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2030)
            .message("Movies found with the personId")
            .build();

    public static final Result NO_MOVIES_WITH_PERSON_ID_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2031)
            .message("No Movies found with the personId")
            .build();

    public static final Result PERSON_WITH_ID_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2040)
            .message("Person found with the specified ID")
            .build();

    public static final Result NO_PERSON_WITH_ID_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2041)
            .message("No Person found for the specified ID")
            .build();

    public static final Result PERSONS_FOUND_WITHIN_SEARCH =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2050)
            .message("Persons with the given search parameters found")
            .build();

    public static final Result NO_PERSONS_FOUND_WITHIN_SEARCH =
        new Result.Builder()
            .status(HttpStatus.OK)
            .code(2051)
            .message("No Persons found with the given search parameters")
            .build();

    static Stream<Result> toStream()
    {
        return Stream.of(
            INVALID_ORDER_BY,
            INVALID_DIRECTION,
            INVALID_LIMIT,
            INVALID_PAGE,
            MOVIE_WITH_ID_FOUND,
            NO_MOVIE_WITH_ID_FOUND,
            MOVIES_FOUND_WITHIN_SEARCH,
            NO_MOVIES_FOUND_WITHIN_SEARCH,
            MOVIES_WITH_PERSON_ID_FOUND,
            NO_MOVIES_WITH_PERSON_ID_FOUND,
            PERSON_WITH_ID_FOUND,
            NO_PERSON_WITH_ID_FOUND,
            PERSONS_FOUND_WITHIN_SEARCH,
            NO_PERSONS_FOUND_WITHIN_SEARCH
        );
    }
}
