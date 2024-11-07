package com.ermakov.carslist.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "brands")
public class BrandEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @Column(name = "name")
  String name;
  @OneToMany(mappedBy = "brandEntity", cascade = CascadeType.REMOVE)
  Set<ModelEntity> modelEntities;

  public Set<ModelEntity> getModelEntities() {
    return modelEntities;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public BrandEntity(Long id, String name, Set<ModelEntity> modelEntities) {
    this.id = id;
    this.name = name;
    this.modelEntities = modelEntities;
  }

  public BrandEntity() {
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setModelEntities(Set<ModelEntity> modelEntities) {
    this.modelEntities = modelEntities;
  }

  @Override
  public String toString() {
    return "BrandEntity{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", modelEntities=" + modelEntities +
        '}';
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrandEntity that = (BrandEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
        Objects.equals(modelEntities, that.modelEntities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, modelEntities);
  }
}
