package com.ermakov.carslist.service.cache;


import com.ermakov.carslist.model.entity.ModelEntity;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ModelCacheServiceImpl implements ModelCacheService {
  private final JpaRepository<ModelEntity, Long> repository;
  private Map<Long, ModelEntity> cacheMap = new ConcurrentHashMap<>();

  public ModelCacheServiceImpl(JpaRepository<ModelEntity, Long> repository) {
    this.repository = repository;
  }

  @PostConstruct
  @Scheduled(cron = "${scheduler.cache.period}")
  public void loadCache() {
    this.cacheMap = repository.findAll().stream()
        .collect(Collectors.toConcurrentMap(ModelEntity::getId, Function.identity()));
  }

  public void put(ModelEntity entity) {
    cacheMap.put(entity.getId(), entity);
  }

  public Optional<ModelEntity> get(Long modelId) {
    return Optional.ofNullable(cacheMap.get(modelId));
  }

  public List<ModelEntity> getAll() {
    return cacheMap.values().stream()
        .toList();
  }

  public void remove(ModelEntity entity) {
    cacheMap.remove(entity.getId());
  }
}
