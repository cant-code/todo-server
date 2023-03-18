package com.cantcode.overengineeredtodoserver.service.base;

import com.cantcode.overengineeredtodoserver.repository.TodoRepository;
import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import com.cantcode.overengineeredtodoserver.service.spi.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {


    private final TodoRepository todoRepository;

    @Override
    public Mono<TodoEntity> getTodo(String userId, UUID id) {
        return todoRepository.findById(userId, id);
    }

    @Override
    public Mono<Boolean> addTodo(String userId, TodoModel todoModel) {
        log.info("Starting Add Todo");
        TodoEntity todoEntity = new TodoEntity();
        log.debug("User: {}; Entity: {}", userId, todoEntity);
        todoEntity.setId(UUID.randomUUID());
        todoEntity.setTodo(todoModel.todo());
        todoEntity.setUserId(userId);
        return todoRepository.save(userId, todoEntity);
    }

    @Override
    public Flux<TodoModel> getAllTodo(String userId) {
        log.info("Called Get All TODO");
        return todoRepository.findAll(userId).map(todoEntity -> new TodoModel(todoEntity.getId(), todoEntity.getTodo()));
    }
}
