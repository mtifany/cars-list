package com.ermakov.carslist.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record EditModelRequest(
    @NotNull @Positive
    Long modelId,
    @NotBlank
    String name,
    @Positive
    Long brandId
) {
}
