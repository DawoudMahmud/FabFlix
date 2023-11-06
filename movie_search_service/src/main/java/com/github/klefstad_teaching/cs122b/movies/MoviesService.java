package com.github.klefstad_teaching.cs122b.movies;

import com.gitcodings.stack.core.spring.SecuredStackService;
import com.github.klefstad_teaching.cs122b.movies.config.MoviesServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@SecuredStackService
@EnableConfigurationProperties({
        MoviesServiceConfig.class
})
public class MoviesService {
    public static void main(String[] args) {
        SpringApplication.run(MoviesService.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOrigins("*");
            }
        };
    }
}