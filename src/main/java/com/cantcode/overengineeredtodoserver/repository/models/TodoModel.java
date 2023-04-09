package com.cantcode.overengineeredtodoserver.repository.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record TodoModel(
        @Schema(description = "Todo ID") UUID id,
        @NotEmpty @Schema(description = "Todo message", example = "This is a todo") String todo) {
}
