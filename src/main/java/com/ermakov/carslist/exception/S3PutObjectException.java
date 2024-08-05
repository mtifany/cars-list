package com.ermakov.carslist.exception;

  public class S3PutObjectException extends RuntimeException {
    public S3PutObjectException(String message) {
      super(message);
    }
}
