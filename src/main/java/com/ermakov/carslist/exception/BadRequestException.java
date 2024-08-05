package com.ermakov.carslist.exception;

public class BadRequestException extends RuntimeException{
  public BadRequestException() {
    super("Invalid request was sent");
  }
}
