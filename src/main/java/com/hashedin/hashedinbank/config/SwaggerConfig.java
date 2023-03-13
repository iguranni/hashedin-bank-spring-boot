package com.hashedin.hashedinbank.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth",type = SecuritySchemeType.HTTP,bearerFormat = "JWT", scheme = "bearer")
public class SwaggerConfig {

    public static final String CONTACT_NAME = "HashedIn By Deloitte";
    public static final String SWAGGER_TITLE = "Hashedin Bank App API";
    public static final String SWAGGER_DESCRIPTION = "Hashedin Bank does B2B business only, serving only MNC clients";
    public static final String VERSION = "v1";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(SWAGGER_TITLE).description(SWAGGER_DESCRIPTION).version(VERSION)
                        .contact(new Contact().name(CONTACT_NAME)))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}