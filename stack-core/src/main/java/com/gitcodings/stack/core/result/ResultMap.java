package com.gitcodings.stack.core.result;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ResultMap
{
    private static final Map<Integer, Result> RESULT_MAP;

    static {
        RESULT_MAP =
            Stream.of(
                      BasicResults.toStream(),
                      IDMResults.toStream(),
                      MoviesResults.toStream(),
                      BillingResults.toStream()
                  )
                  .flatMap(r -> r)
                  .collect(
                      Collectors.toMap(
                          Result::code,
                          r -> r
                      )
                  );

        RESULT_MAP.put(Result.NO_RESULT.code(), Result.NO_RESULT);
    }

    public static Result fromCode(int code)
    {
        return RESULT_MAP.get(code);
    }
}
