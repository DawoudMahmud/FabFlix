package com.gitcodings.stack.core.spring;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServiceWebMvcConfigurer implements WebMvcConfigurer
{
    private final List<String> origins;
    private final List<String> methods;

    public ServiceWebMvcConfigurer()
    {
        this.origins = Collections.singletonList(
            "http://localhost:3000"
        );

        this.methods = new ArrayList<>(Arrays.asList(
            HttpMethod.OPTIONS.name(),
            HttpMethod.POST.name(),
            HttpMethod.GET.name(),
            HttpMethod.DELETE.name()
        ));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry
            .addMapping("/**")
            .allowedOrigins(origins.toArray(new String[0]))
            .allowedMethods(methods.toArray(new String[0]))
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers)
    {
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
