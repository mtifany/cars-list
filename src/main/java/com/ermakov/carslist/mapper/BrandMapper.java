package com.ermakov.carslist.mapper;

import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.model.entity.BrandEntity;
import org.mapstruct.Mapper;

@Mapper
public interface BrandMapper {
  BrandEntity toBrandEntity(Brand brand);

  Brand toBrand(BrandEntity brandEntity);
}
