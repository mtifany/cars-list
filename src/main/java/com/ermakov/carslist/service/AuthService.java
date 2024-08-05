package com.ermakov.carslist.service;

import com.ermakov.carslist.model.LoginResponse;
import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;

public interface AuthService {
  LoginResponse loginUser(LoginRequest loginRequest);

  void registerUser(RegisterUserRequest registerUserRequest);
}
