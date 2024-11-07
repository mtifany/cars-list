package com.ermakov.carslist.controller;

import com.ermakov.carslist.model.LoginResponse;
import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import com.ermakov.carslist.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Validated
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerUser(@RequestBody @NotNull @Valid RegisterUserRequest registerRequest) {
    authService.registerUser(registerRequest);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public LoginResponse authenticateUser(@RequestBody @NotNull @Valid LoginRequest loginRequest) {
    return authService.loginUser(loginRequest);
  }
}

