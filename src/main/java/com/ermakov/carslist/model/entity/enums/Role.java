package com.ermakov.carslist.model.entity.enums;

import java.util.Arrays;

public enum Role {
  ROLE_USER,
  ROLE_ADMIN;

  public static Role getRoleByName(String role) {
    return Arrays.stream(Role.values())
        .filter(value -> role.equalsIgnoreCase(value.toString()))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
