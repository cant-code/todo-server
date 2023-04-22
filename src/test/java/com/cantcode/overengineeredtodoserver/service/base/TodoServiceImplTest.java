package com.cantcode.overengineeredtodoserver.service.base;

import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import com.cantcode.overengineeredtodoserver.service.spi.TodoService;
import com.cantcode.overengineeredtodoserver.utils.AbstractTestContainers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.cantcode.overengineeredtodoserver.utils.TestObjects.getTodoModel;

@SpringBootTest
class TodoServiceImplTest extends AbstractTestContainers {

    @Autowired
    private TodoService todoService;

    private final UUID userId = UUID.randomUUID();

    @Test
    @DisplayName("Get Todo")
    void getTodo() {
        TodoModel todoModel = getTodoModel();
        todoService.addTodo(userId.toString(), todoModel).block();

        TodoModel todo = todoService.getAllTodo(userId.toString()).blockFirst();

        Assertions.assertNotNull(todo);

        Mono<TodoModel> todoEntityMono = todoService.getTodo(userId.toString(), todo.id());
        StepVerifier.create(todoEntityMono)
                .thenConsumeWhile(todoModel1 -> todoModel.todo().equals(todoModel1.todo()))
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

        Assertions.assertNotNull(todoModelFlux);

        Mono<Long> count = todoService.deleteById(userId.toString(), todoModelFlux.id());

        StepVerifier.create(count)
                .expectNext(Long.valueOf(1))
                .verifyComplete();
    }
}