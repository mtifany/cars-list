package com.ermakov.carslist.model.entity;

import com.ermakov.carslist.model.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class RoleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_role_name")
  @Enumerated(EnumType.STRING)
  private Role name;

  public void setName(String name) {
    this.name = Role.getRoleByName(name);
  }
  public String getName() {
    return name.toString();
  }

  public RoleEntity() {
  }

  public RoleEntity(Long id, Role name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleEntity that = (RoleEntity) o;
    return Objects.equals(id, that.id) && name == that.name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
