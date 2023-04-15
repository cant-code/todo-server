package com.cantcode.overengineeredtodoserver.controllers.config;

import com.cantcode.overengineeredtodoserver.controllers.APIDefinition;
import com.cantcode.overengineeredtodoserver.controllers.handler.TodoHandler;
import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
public class RouterConfig {

    private final TodoHandler todoHandler;

    @RouterOperations({
            @RouterOperation(
                    path = APIDefinition.TODO + "/getAll",
                    method = RequestMethod.GET,
                    operation = @Operation(operationId = "getAllTodo", summary = "Get All Todo for user",
                            tags = {APIDefinition.TODO},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = TodoModel.class))))
                            })),
            @RouterOperation(
                    path = APIDefinition.TODO + "/save",
                    method = RequestMethod.POST,
                    operation = @Operation(operationId = "saveTodo", summary = "Save Todo",
                            tags = {APIDefinition.TODO},
                            requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TodoModel.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Successful Operation",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = Boolean.class))),
                                    @ApiResponse(responseCode = "400", description = "Bad Request",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                            })),
            @RouterOperation(
                    path = APIDefinition.TODO + "/{id}",
                    method = RequestMethod.DELETE,
                    operation = @Operation(operationId = "deleteTodoById", summary = "Delete Todo by ID",
                            tags = {APIDefinition.TODO},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Todo ID")},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = String.class, example = "Test"))),
                                    @ApiResponse(responseCode = "404", description = "Todo Not Found",
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                            }))
    })
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
