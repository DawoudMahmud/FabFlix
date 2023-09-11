package com.gitcodings.stack.core.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class JWTAuthenticationFilter extends OncePerRequestFilter
{
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException
    {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "No Authorization header found"
            );
        } else if (!authorization.startsWith(BEARER_PREFIX)) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Invalid Authorization. Token does not start with '" +
                BEARER_PREFIX +
                "'. Make sure the token starts with this prefix" +
                "(Including the space)"
            );
        } else {
            Optional<Authentication> authentication = getAuthenticationFromHeader(authorization);

            if (authentication.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
                filterChain.doFilter(request, response);
            } else {
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Failed to parse bearer token."
                );
            }
        }
    }

    private Optional<Authentication> getAuthenticationFromHeader(String authorization)
    {
        try {
            String token = authorization.substring(BEARER_PREFIX.length());

            return Optional.of(JWTAuthenticationToken.fromSerializedToken(token));

        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
