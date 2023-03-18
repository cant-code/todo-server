package com.cantcode.overengineeredtodoserver.controllers.handler;

import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import com.cantcode.overengineeredtodoserver.service.spi.TodoService;
import com.cantcode.overengineeredtodoserver.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TodoHandler {

    private final TodoService todoService;

    public Mono<ServerResponse> getAllTodo() {
        Flux<TodoModel> todoModelFlux = SecurityUtils.getUserId().flatMapMany(todoService::getAllTodo);
        return ServerResponse
                .ok()
                .body(todoModelFlux, TodoModel.class);
    }

    public Mono<ServerResponse> saveTodo(ServerRequest serverRequest) {
        Mono<TodoModel> todoModelMono = serverRequest.bodyToMono(TodoModel.class);
        Mono<Boolean> todoId = SecurityUtils
                .getUserId()
                .flatMap(s -> todoModelMono.flatMap(todoModel -> todoService.addTodo(s, todoModel)));
        return ServerResponse
                .created(serverRequest.uri())
                .body(todoId, Boolean.class);
    }
}
