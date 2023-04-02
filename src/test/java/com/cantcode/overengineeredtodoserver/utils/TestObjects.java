package com.cantcode.overengineeredtodoserver.utils;

import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import com.cantcode.overengineeredtodoserver.repository.models.TodoModel;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class TestObjects {

    public static TodoEntity getTodoEntity(String userId) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setId(UUID.randomUUID());
        todoEntity.setUserId(userId);
        todoEntity.setTodo(RandomStringUtils.randomAlphabetic(20));
        return todoEntity;
    }

    public static TodoModel getTodoModel() {
        return new TodoModel(UUID.randomUUID(), RandomStringUtils.randomAlphanumeric(20));
    }
}
