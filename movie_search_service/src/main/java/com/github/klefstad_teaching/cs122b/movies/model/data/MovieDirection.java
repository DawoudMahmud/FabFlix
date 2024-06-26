package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.MoviesResults;

import java.util.Locale;

public enum MovieDirection {
    ASC(" ASC, m.id ASC "),
    DESC(" DESC, m.id ASC ");

    private final String sql;

    MovieDirection(String sql)
    {
        this.sql = sql;
    }

    public String toSql()
    {
        return sql;
    }

    public static MovieDirection fromString(String direction)
    {
        if (direction == null)
            return ASC;

        switch (direction.toUpperCase(Locale.ROOT))
        {
            case "ASC":
                return ASC;
            case "DESC":
                return DESC;
            default:
                throw new ResultError(MoviesResults.INVALID_DIRECTION);
        }
    }
}
