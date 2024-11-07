package com.ermakov.carslist.service.cache;


import com.ermakov.carslist.model.entity.ModelEntity;
import java.util.List;
import java.util.Optional;

public interface ModelCacheService {

  void put(ModelEntity model);

  Optional<ModelEntity> get(Long modelId);

  List<ModelEntity> getAll();

  void remove(ModelEntity model);
}
