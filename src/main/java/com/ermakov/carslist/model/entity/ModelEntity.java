package com.ermakov.carslist.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "models")
public class ModelEntity {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @Column(name = "name")
  String name;
  @Column(name = "photo_aws_key")
  String photoAwsKey;
  @ManyToOne
  @JoinColumn(name = "brand_id")
  BrandEntity brandEntity;

  public ModelEntity() {
  }

  public ModelEntity(Long id, String name, String photoAwsKey, BrandEntity brandEntity) {
    this.id = id;
    this.name = name;
    this.photoAwsKey = photoAwsKey;
    this.brandEntity = brandEntity;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhotoAwsKey(String photoAwsKey) {
    this.photoAwsKey = photoAwsKey;
  }

  public void setBrandEntity(BrandEntity brandEntity) {
    this.brandEntity = brandEntity;
  }
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPhotoAwsKey() {
    return photoAwsKey;
  }

  public BrandEntity getBrandEntity() {
    return brandEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelEntity that = (ModelEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
        Objects.equals(photoAwsKey, that.photoAwsKey) &&
        Objects.equals(brandEntity, that.brandEntity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, photoAwsKey, brandEntity);
  }
}
