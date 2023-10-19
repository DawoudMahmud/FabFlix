package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.gitcodings.stack.core.error.ResultError;
import com.gitcodings.stack.core.result.MoviesResults;

import java.util.Locale;

public enum PersonDirection {
    ASC(" ASC, p.id ASC "),
    DESC(" DESC, p.id ASC ");

    private final String sql;

    PersonDirection(String sql)
    {
        this.sql = sql;
    }

    public String toSql()
    {
        return sql;
    }

    public static PersonDirection fromString(String direction)
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
