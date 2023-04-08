package com.cantcode.overengineeredtodoserver.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class SecurityControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final UUID userId = UUID.randomUUID();

    @Test
    @DisplayName("Get Email from Token")
    public void getEmailFromToken() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@test.com";

        webTestClient.mutateWith(mockJwt()
                        .jwt(builder -> builder
                                .claim("user_id", userId.toString())
                                .claim("email", email)
                        )
                )
                .get().uri("/security/email")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(email);
    }

    @Test
    @DisplayName("Get User ID from Token")
    public void getUserIdFromToken() {
        webTestClient.mutateWith(mockJwt()
                        .jwt(builder -> builder
                                .claim("user_id", userId.toString())
                        )
                )
                .get().uri("/security/id")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(userId.toString());
    }
}
