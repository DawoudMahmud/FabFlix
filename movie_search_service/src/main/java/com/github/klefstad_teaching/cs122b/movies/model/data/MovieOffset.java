package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

public class MovieOffset {

    private static int value;

    public MovieOffset(int page)
    {
        value = page;
    }

    public int getValue()
    {
        return value;
    }

    public String toSql()
    {
        return (" OFFSET " + value);
    }
}
