package com.github.klefstad_teaching.cs122b.idm;

import com.gitcodings.stack.core.spring.StackService;
import com.github.klefstad_teaching.cs122b.idm.config.IDMServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@StackService
@EnableConfigurationProperties({
    IDMServiceConfig.class
})
public class IDMService
{
    public static void main(String[] args)
    {
        SpringApplication.run(IDMService.class, args);
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
