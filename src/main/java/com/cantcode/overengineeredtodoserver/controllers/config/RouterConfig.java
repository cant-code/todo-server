package com.cantcode.overengineeredtodoserver.controllers.config;

import com.cantcode.overengineeredtodoserver.controllers.APIDefinition;
import com.cantcode.overengineeredtodoserver.controllers.handler.TodoHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
public class RouterConfig {

    private final TodoHandler todoHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .path(APIDefinition.TODO, builder -> builder
                        .GET("/getAll", request -> todoHandler.getAllTodo())
                        .POST("/save", todoHandler::saveTodo)
                        .DELETE("/{id}", todoHandler::deleteTodoById))
                .build();
    }
}
