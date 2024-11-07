package com.ermakov.carslist.repository;

import com.ermakov.carslist.model.entity.ModelEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ModelRepository
    extends JpaRepository<ModelEntity, Long>, JpaSpecificationExecutor<ModelEntity> {

  @Query("SELECT DISTINCT model.name FROM ModelEntity model")
  Set<String> findDistinctNames();
}
