package com.ermakov.carslist.repository;

import com.ermakov.carslist.model.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
}
