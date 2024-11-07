package com.ermakov.carslist.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EditModelRequest(
    @NotNull @Positive
    Long modelId,
    @NotBlank
    String name,
    @Positive
    Long brandId
) {
}
