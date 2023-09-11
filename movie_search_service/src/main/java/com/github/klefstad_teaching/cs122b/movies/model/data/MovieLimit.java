package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

import java.util.Locale;

public enum MovieLimit {
    TEN(" LIMIT 10 "),
    TWENTY_FIVE(" LIMIT 25 "),
    FIFTY(" LIMIT 50 "),
    ONE_HUNDRED (" LIMIT 100 ");

    private final String sql;
    private static int value;

    MovieLimit(String sql)
    {
        this.sql = sql;
    }

    public String toSql()
    {
        return sql;
    }
    public int getValue()
    {
        return value;
    }

    public static MovieLimit fromInt(int limit)
    {
        if (limit == 0) {
            value = 10;
            return TEN;
        }

        switch (limit)
        {
            case 10: {
                value = 10;
                return TEN;
            }
            case 25: {
                value = 25;
                return TWENTY_FIVE;
            }
            case 50: {
                value = 50;
                return FIFTY;
            }
            case 100: {
                value = 100;
                return ONE_HUNDRED;
            }
            default:
                throw new ResultError(MoviesResults.INVALID_LIMIT);
        }
    }
}
