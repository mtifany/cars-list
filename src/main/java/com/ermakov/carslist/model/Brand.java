package com.ermakov.carslist.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Brand(
    Long id,
    @NotBlank
    @NotNull
    String name
) {
}
