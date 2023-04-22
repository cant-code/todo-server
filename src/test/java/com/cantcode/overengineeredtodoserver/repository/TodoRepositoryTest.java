package com.cantcode.overengineeredtodoserver.repository;

import com.cantcode.overengineeredtodoserver.config.utils.RedisConfig;
import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.utils.AbstractTestContainers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.cantcode.overengineeredtodoserver.utils.TestObjects.getTodoEntity;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataRedisTest
@Import({ TodoRepository.class, RedisConfig.class })
class TodoRepositoryTest extends AbstractTestContainers {

    private static final UUID userId = UUID.randomUUID();

    @Autowired
    private TodoRepository todoRepository;

    @Test
    @DisplayName("Test if Redis Container is Running")
    void testIfRedisIsRunning() {
        assertTrue(REDIS_CONTAINER.isRunning());
    }

    @Test
    @DisplayName("Find All Todos for user")
    void findAll() {
        TodoEntity todoEntity = getTodoEntity(userId.toString());
        todoRepository.save(userId.toString(), todoEntity).block();

        Flux<TodoEntity> val = todoRepository.findAll(String.valueOf(userId));
        StepVerifier.create(val)
                .expectNext(todoEntity)
                .verifyComplete();

    }

    @Test
    @DisplayName("Find Todo by ID")
    void findById() {
        TodoEntity todoEntity = getTodoEntity(userId.toString());
        todoRepository.save(userId.toString(), todoEntity).block();

        Mono<TodoEntity> val = todoRepository.findById(userId.toString(), todoEntity.getId());
        StepVerifier.create(val)
                .expectNext(todoEntity)
                .verifyComplete();
    }

    @Test
    @DisplayName("Save Todo")
    void save() {
        TodoEntity todoEntity = getTodoEntity(userId.toString());
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
        TodoEntity todoEntity = getTodoEntity(userId.toString());
        todoRepository.save(userId.toString(), todoEntity).block();

        Mono<Long> val = todoRepository.deleteById(userId.toString(), todoEntity.getId());
        StepVerifier.create(val)
                .expectNext(Long.valueOf(1))
                .verifyComplete();
    }
}