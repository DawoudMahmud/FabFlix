package com.gitcodings.stack.core.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public final class Result
{
    public static final Result NO_RESULT =
        new Result.Builder()
            .code(0)
            .message("No result set, did you forget to add one?")
            .status(HttpStatus.NOT_IMPLEMENTED)
            .build();

    private final Integer    code;
    private final String     message;
    private final HttpStatus status;

    private Result(Builder builder)
    {
        this.code = Objects.requireNonNull(builder.code);
        this.message = Objects.requireNonNull(builder.message);
        this.status = Objects.requireNonNull(builder.status);
    }

    @JsonProperty public Integer code()
    {
        return code;
    }

    @JsonProperty public String message()
    {
        return message;
    }

    @JsonIgnore public HttpStatus status()
    {
        return status;
    }

    public static final class Builder
    {
        private Integer    code;
        private String     message;
        private HttpStatus status;

        public Builder code(Integer code)
        {
            this.code = code;
            return this;
        }

        public Builder message(String message)
        {
            this.message = message;
            return this;
        }

        public Builder status(HttpStatus status)
        {
            this.status = status;
            return this;
        }

        public Result build()
        {
            return new Result(this);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Result result = (Result) o;

        return code.equals(result.code) &&
               message.equals(result.message) &&
               status == result.status;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(code, message, status);
    }

    @Override
    public String toString()
    {
        return "Result[" + code + "]: '" + message + '\'';
    }
}
