package com.ermakov.carslist.repository;

import com.ermakov.carslist.model.entity.RoleEntity;
import com.ermakov.carslist.model.entity.enums.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
  Optional<RoleEntity> findByName(Role name);
}
