package com.cantcode.overengineeredtodoserver.controllers;

import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static com.cantcode.overengineeredtodoserver.utils.TestObjects.getTodoModel;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TodoEndpointsTest {

    @Autowired
    private WebTestClient webTestClient;

    private final UUID userId = UUID.randomUUID();

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.9-alpine"))
            .withEnv("REDIS_PASSWORD", "test")
            .withCommand("redis-server", "--requirepass", "test")
            .withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Test
    @DisplayName("Call Save Todo with correct token and return 201 Status")
    public void saveTodoReturnsIsCreatedStatus() {
        webTestClient.mutateWith(mockJwt()
                        .jwt(builder -> builder.claim("user_id", userId.toString()))
                )
                .post().uri("/todo/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getTodoModel()), TodoModel.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    @DisplayName("Return 401 Unauthorized when no token is passed")
    public void saveTodoReturnsUnauthorized() {
        webTestClient
                .post().uri("/todo/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(getTodoModel()), TodoModel.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @DisplayName("Get all Todos")
    public void getAllTodo() {
        this.saveTodoReturnsIsCreatedStatus();

        List<TodoModel> response = webTestClient.mutateWith(mockJwt()
                        .jwt(builder -> builder.claim("user_id", userId.toString())))
                .get().uri("/todo/getAll")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(new ParameterizedTypeReference<List<TodoModel>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Delete todo by ID")
    public void deleteTodo() {
        this.saveTodoReturnsIsCreatedStatus();

        List<TodoModel> response = webTestClient.mutateWith(mockJwt()
                        .jwt(builder -> builder.claim("user_id", userId.toString())))
                .get().uri("/todo/getAll")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(new ParameterizedTypeReference<List<TodoModel>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(response);

        response.forEach(todoModel -> {
            String res = webTestClient.mutateWith(mockJwt()
                        .jwt(builder -> builder.claim("user_id", userId.toString()))
                    )
                    .delete()
                    .uri("/todo/{id}", todoModel.id())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(String.class)
                    .returnResult()
                    .getResponseBody();

            Assertions.assertNotNull(res);
            Assertions.assertEquals("Deleted items: 1", res);
        });
    }
}
