package com.cantcode.overengineeredtodoserver.service.spi;

import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TodoService {

    Mono<TodoEntity> getTodo(String userId, UUID id);

    Mono<Boolean> addTodo(String userId, TodoModel todoModel);

    Flux<TodoModel> getAllTodo(String userId);

    Mono<Long> deleteById(String userId, UUID id);
}
