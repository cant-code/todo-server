package com.cantcode.overengineeredtodoserver.repository.models;

import java.util.UUID;

public record TodoModel(UUID id, String todo) {
}
