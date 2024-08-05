package com.ermakov.carslist.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "brands")
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity {
  @Id
  @Column(name = "id")
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  @Setter
  String name;

  @OneToMany(mappedBy = "brandEntity", cascade = CascadeType.REMOVE)
  @Setter
  Set<ModelEntity> modelEntities;
}
