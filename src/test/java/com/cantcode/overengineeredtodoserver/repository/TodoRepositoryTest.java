package com.cantcode.overengineeredtodoserver.repository;

import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
class TodoRepositoryTest {

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.9-alpine"))
            .withEnv("REDIS_PASSWORD", "test")
            .withCommand("redis-server", "--requirepass", "test")
            .withExposedPorts(6379);

    private static final UUID userId = UUID.randomUUID();

    @Autowired
    private TodoRepository todoRepository;

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", REDIS_CONTAINER::getHost);
        registry.add("redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Test
    @DisplayName("Test if Redis Container is Running")
    void testIfRedisIsRunning() {
        assertTrue(REDIS_CONTAINER.isRunning());
    }

    @Test
    @DisplayName("Find All Todos for user")
    void findAll() {
        TodoEntity todoEntity = getTodoEntity();
        todoRepository.save(userId.toString(), todoEntity).block();

        Flux<TodoEntity> val = todoRepository.findAll(String.valueOf(userId));
        StepVerifier.create(val)
                .expectNext(todoEntity)
                .verifyComplete();

    }

    @Test
    @DisplayName("Find Todo by ID")
    void findById() {
        TodoEntity todoEntity = getTodoEntity();
        todoRepository.save(userId.toString(), todoEntity).block();

        Mono<TodoEntity> val = todoRepository.findById(userId.toString(), todoEntity.getId());
        StepVerifier.create(val)
                .expectNext(todoEntity)
                .verifyComplete();
    }

    @Test
    @DisplayName("Save Todo")
    void save() {
        TodoEntity todoEntity = getTodoEntity();
        Mono<Boolean> val = todoRepository.save(userId.toString(), todoEntity);

        StepVerifier.create(val)
                .expectNext(true)
                .verifyComplete();

        Mono<TodoEntity> todo = todoRepository.findById(userId.toString(), todoEntity.getId());
        StepVerifier.create(todo)
                .expectNext(todoEntity)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete todo by ID")
    void deleteById() {
        TodoEntity todoEntity = getTodoEntity();
        todoRepository.save(userId.toString(), todoEntity).block();

        Mono<Long> val = todoRepository.deleteById(userId.toString(), todoEntity.getId());
        StepVerifier.create(val)
                .expectNext(Long.valueOf(1))
                .verifyComplete();
    }

    private TodoEntity getTodoEntity() {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setId(UUID.randomUUID());
        todoEntity.setUserId(userId.toString());
        todoEntity.setTodo(RandomStringUtils.randomAlphabetic(20));
        return todoEntity;
    }
}