package com.ermakov.carslist.controller;

import com.ermakov.carslist.model.LoginResponse;
import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import com.ermakov.carslist.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Auth Controller", description = "User Authentication")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201", description = "CREATED")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "409", description = "Conflict")
  @Operation(summary = "Register user")
  public void registerUser(@RequestBody @NotNull @Valid RegisterUserRequest registerRequest) {
    authService.registerUser(registerRequest);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @Operation(summary = "Login user")
  public LoginResponse authenticateUser(@RequestBody @NotNull @Valid LoginRequest loginRequest) {
    return authService.loginUser(loginRequest);
  }
}

