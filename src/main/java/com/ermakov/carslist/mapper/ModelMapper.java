package com.ermakov.carslist.mapper;

import com.ermakov.carslist.model.Model;
import com.ermakov.carslist.model.entity.ModelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ModelMapper {

  @Mapping(target = "brand.name", source = "modelEntity.brandEntity.name")
  Model toModel(ModelEntity modelEntity);
}
