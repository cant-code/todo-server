package com.cantcode.overengineeredtodoserver.repository.mappers;

import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class TodoMapper {

    public TodoModel mapTodoEntityToTodoModel(TodoEntity todo) {
        return new TodoModel(todo.getId(), todo.getTodo());
    }

    public TodoEntity mapTodoModelToTodoEntity(TodoModel todoModel) {
        TodoEntity todo = new TodoEntity();
        todo.setId(Objects.isNull(todoModel.id()) ? UUID.randomUUID() : todoModel.id());
        todo.setTodo(todoModel.todo());
        return todo;
    }
}
