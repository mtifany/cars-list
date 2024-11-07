package com.ermakov.carslist.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest (
    @NotNull
    @NotBlank
    String firstName,
    @NotNull
    @NotBlank
    String lastName,
    @NotNull
    @NotBlank
    @Email
    String email,
    @NotNull
    @NotBlank
    String password
){
}
