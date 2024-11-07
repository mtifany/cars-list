package com.ermakov.carslist.exception;

public class ModelNotFoundException extends RuntimeException{
  public ModelNotFoundException(Long modelId) {
    super(String.format("Model with id %s is not found", modelId));
  }
}
