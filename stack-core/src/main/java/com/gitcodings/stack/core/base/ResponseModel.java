package com.gitcodings.stack.core.base;

import com.gitcodings.stack.core.result.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ResponseModel<T extends ResponseModel<?>>
{
    private Result result = Result.NO_RESULT;

    public Result getResult()
    {
        return result;
    }

    public T setResult(Result result)
    {
        this.result = Objects.requireNonNull(result);

        @SuppressWarnings("unchecked") T self = (T) this;
        return self;
    }

    public ResponseEntity<T> toResponse()
    {
        @SuppressWarnings("unchecked") T self = (T) this;
        return ResponseEntity.status(getResult().status()).body(self);
    }
}
