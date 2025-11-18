package com.notesapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private static final String BEARER_AUTH_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Note API")
                        .description("API for Notes App")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME_NAME, new SecurityScheme()
                                .name(BEARER_AUTH_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token with 'Bearer ' prefix, e.g. 'Bearer abcde12345'")));

    }
}
