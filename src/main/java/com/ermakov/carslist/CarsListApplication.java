package com.ermakov.carslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CarsListApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarsListApplication.class, args);
  }

}
