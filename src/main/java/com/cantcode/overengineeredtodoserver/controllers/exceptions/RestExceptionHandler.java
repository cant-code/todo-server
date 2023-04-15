package com.cantcode.overengineeredtodoserver.controllers.exceptions;

import com.cantcode.overengineeredtodoserver.repository.exceptions.TodoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @Bean
    WebFilter handleExceptions() {
        return (exchange, chain) -> chain.filter(exchange)
                .onErrorResume(TodoException.class, throwable -> {
                    log.error("Error: ", throwable);
                    return Mono.error(new ResponseStatusException(throwable.getHttpStatus(),
                            throwable.getMessage(), throwable));
                });
    }
}
