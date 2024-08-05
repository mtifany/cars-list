package com.ermakov.carslist.model;

public record Model(
    Long id,
    String name,
    String photoAwsKey,
    Brand brand) {
}
