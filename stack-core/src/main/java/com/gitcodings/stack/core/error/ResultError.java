package com.gitcodings.stack.core.error;

import com.gitcodings.stack.core.result.Result;
import com.gitcodings.stack.core.base.ResultResponse;

public final class ResultError extends RuntimeException
{
    private final ResultResponse body;

    public ResultError(Result result)
    {
        this.body = ResultResponse.of(result);
    }

    public ResultResponse getBody()
    {
        return body;
    }
}
