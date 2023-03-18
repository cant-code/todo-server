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

import java.util.UUID;

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

    public Mono<ServerResponse> deleteTodoById(ServerRequest serverRequest) {
        UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        Mono<String> resp = SecurityUtils
                .getUserId()
                .flatMap(s -> todoService.deleteById(s, id))
                .map(aLong -> "Deleted items: " + aLong);
        return ServerResponse
                .ok()
                .body(resp, String.class);
    }
}
