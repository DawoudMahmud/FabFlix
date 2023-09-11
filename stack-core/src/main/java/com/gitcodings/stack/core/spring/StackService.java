package com.gitcodings.stack.core.spring;

import com.gitcodings.stack.core.error.ResultErrorHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({
    ResultErrorHandler.class,
    ServiceWebMvcConfigurer.class,
    ServiceRequestLogger.class
})
public @interface StackService
{
}
