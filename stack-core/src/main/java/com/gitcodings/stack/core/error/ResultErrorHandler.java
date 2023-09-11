package com.gitcodings.stack.core.error;

import com.gitcodings.stack.core.base.ResultResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResultErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(ResultError.class)
    protected ResponseEntity<ResultResponse> reportError(ResultError error)
    {
        return error.getBody().toResponse();
    }
}
