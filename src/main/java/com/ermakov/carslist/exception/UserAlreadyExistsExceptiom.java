package com.ermakov.carslist.exception;

public class UserAlreadyExistsExceptiom extends RuntimeException {
  public UserAlreadyExistsExceptiom() {
    super("User already exists");
  }
}