package com.ermakov.carslist.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateModelRequest (
    @NotBlank
    String name,
    @Positive
    Long brandId
){
}
