package com.ermakov.carslist.mapper;

import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.model.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BrandMapper {
  BrandEntity toBrandEntity(Brand brand);

  @Mapping(target = "id", ignore = true)
  Brand toBrand(BrandEntity brandEntity);
}
