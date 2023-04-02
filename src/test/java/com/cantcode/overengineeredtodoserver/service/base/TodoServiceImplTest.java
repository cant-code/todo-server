package com.cantcode.overengineeredtodoserver.service.base;

import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import com.cantcode.overengineeredtodoserver.service.spi.TodoService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.cantcode.overengineeredtodoserver.utils.TestObjects.getTodoModel;

@Testcontainers
@SpringBootTest
class TodoServiceImplTest {

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.9-alpine"))
            .withEnv("REDIS_PASSWORD", "test")
            .withCommand("redis-server", "--requirepass", "test")
            .withExposedPorts(6379);

    @Autowired
    private TodoService todoService;

    private final UUID userId = UUID.randomUUID();

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Test
    @DisplayName("Get Todo")
    void getTodo() {
        TodoModel todoModel = getTodoModel();
        todoService.addTodo(userId.toString(), todoModel).block();

        TodoModel todo = todoService.getAllTodo(userId.toString()).blockFirst();

        Mono<TodoEntity> todoEntityMono = todoService.getTodo(userId.toString(), todo.id());
        StepVerifier.create(todoEntityMono)
                .thenConsumeWhile(todoModel1 -> todoModel.todo().equals(todoModel1.getTodo()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Add Todo")
    void addTodo() {
        TodoModel todoModel = getTodoModel();
        Mono<Boolean> count = todoService.addTodo(userId.toString(), todoModel);

        StepVerifier.create(count)
                .expectNext(true)
                .verifyComplete();

        Flux<TodoModel> todo = todoService.getAllTodo(userId.toString());
        StepVerifier.create(todo)
                .thenConsumeWhile(todoModel1 -> todoModel.todo().equals(todoModel1.todo()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Get All Todos")
    void getAllTodo() {
        TodoModel todoModel = getTodoModel();
        todoService.addTodo(userId.toString(), todoModel).block();

        Flux<TodoModel> todoModelFlux = todoService.getAllTodo(userId.toString());

        StepVerifier.create(todoModelFlux)
                .thenConsumeWhile(todoModel1 -> todoModel.todo().equals(todoModel1.todo()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete todo")
    void deleteById() {
        TodoModel todoModel = getTodoModel();
        todoService.addTodo(userId.toString(), todoModel).block();

        TodoModel todoModelFlux = todoService.getAllTodo(userId.toString()).blockFirst();

        Mono<Long> count = todoService.deleteById(userId.toString(), todoModelFlux.id());

        StepVerifier.create(count)
                .expectNext(Long.valueOf(1))
                .verifyComplete();
    }
}