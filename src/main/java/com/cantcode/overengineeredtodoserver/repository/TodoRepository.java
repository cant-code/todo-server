package com.cantcode.overengineeredtodoserver.repository;

import com.cantcode.overengineeredtodoserver.repository.entities.TodoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class TodoRepository {
    private final ReactiveHashOperations<String, UUID, TodoEntity> reactiveHashOperations;

    @Autowired
    public TodoRepository(ReactiveRedisTemplate<String, TodoEntity> reactiveRedisTemplate) {
        this.reactiveHashOperations = reactiveRedisTemplate.opsForHash();
    }

    public Flux<TodoEntity> findAll(String userId) {
        return reactiveHashOperations.values(userId);
    }

    public Mono<TodoEntity> findById(String userId, UUID id) {
        return reactiveHashOperations.get(userId, id);
    }

    public Mono<Boolean> save(String userId, TodoEntity todoEntity) {
        return reactiveHashOperations.put(userId, todoEntity.getId(), todoEntity);
    }
}
