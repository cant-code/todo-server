package com.cantcode.overengineeredtodoserver.service.base;

import com.cantcode.overengineeredtodoserver.repository.TodoRepository;
import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.repository.mappers.TodoMapper;
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
    private final TodoMapper todoMapper;

    @Override
    public Mono<TodoModel> getTodo(String userId, UUID id) {
        return todoRepository.findById(userId, id).map(todoMapper::mapTodoEntityToTodoModel);
    }

    @Override
    public Mono<Boolean> addTodo(String userId, TodoModel todoModel) {
        log.info("Starting Add Todo");
        TodoEntity todoEntity = todoMapper.mapTodoModelToTodoEntity(todoModel);
        log.debug("User: {}; Entity: {}", userId, todoEntity);
        todoEntity.setUserId(userId);
        return todoRepository.save(userId, todoEntity);
    }

    @Override
    public Flux<TodoModel> getAllTodo(String userId) {
        log.info("Called Get All TODO");
        return todoRepository.findAll(userId).map(todoMapper::mapTodoEntityToTodoModel);
    }

    @Override
    public Mono<Long> deleteById(String userId, UUID id) {
        log.info("Deleting Todo: {}", id);
        return todoRepository.deleteById(userId, id);
    }
}
