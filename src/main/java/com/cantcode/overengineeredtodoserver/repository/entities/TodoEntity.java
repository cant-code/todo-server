package com.cantcode.overengineeredtodoserver.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("Todo")
@Data
@EqualsAndHashCode
public class TodoEntity implements Serializable {

    @Id
    private UUID id;
    private String userId;
    private String todo;
}
