package com.ermakov.carslist.exception;

public class PhotoProcessingException extends RuntimeException {
  public PhotoProcessingException() {
    super("Photo can't be processed");
  }
}