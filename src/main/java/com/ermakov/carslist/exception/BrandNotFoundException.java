package com.ermakov.carslist.exception;

public class BrandNotFoundException extends RuntimeException{
  public BrandNotFoundException(Long brandId) {
    super(String.format("Brand with id %s is not found", brandId));
  }
}
