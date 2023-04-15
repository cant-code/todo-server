package com.cantcode.overengineeredtodoserver.config.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Todo Server", version = "v1"), security = @SecurityRequirement(name = "bearer"))
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "bearer")
public class OpenAPIConfig {

    @Bean
    public OpenApiCustomizer customizer() {
        return openApi ->
                openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                    ApiResponses apiResponses = operation.getResponses();
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                            new ApiResponse().description(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                    .content(new Content().addMediaType(APPLICATION_JSON_VALUE, new MediaType())));
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                            new ApiResponse().description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                    .content(new Content().addMediaType(APPLICATION_JSON_VALUE, new MediaType())));
                }));
    }
}
