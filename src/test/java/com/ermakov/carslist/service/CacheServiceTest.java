package com.ermakov.carslist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ermakov.carslist.model.entity.ModelEntity;
import com.ermakov.carslist.repository.ModelRepository;
import com.ermakov.carslist.service.cache.ModelCacheService;
import com.ermakov.carslist.service.cache.ModelCacheServiceImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

class CacheServiceTest {
  @Mock
  private ModelRepository modelRepository;

  private ModelCacheService cacheService;

  private ModelEntity modelEntity;

  private Map<Long, ModelEntity> cacheMap;

  @BeforeEach
  void setUp() {
    cacheService = new ModelCacheServiceImpl(modelRepository);
    modelEntity = new ModelEntity(1L, "name", null, null);
    cacheMap = new ConcurrentHashMap<>();
    cacheMap.put(modelEntity.getId(), modelEntity);
    ReflectionTestUtils.setField(cacheService, "cacheMap", cacheMap);
  }

  @Test
  void whenGet_thenSuccess() {
    var model = cacheService.get(1L).get();

    assertEquals(modelEntity, model);
  }

  @Test
  void whenGetAndNoCachedEntityFound_thenNullReturned() {
    var model = cacheService.get(2L).isEmpty();

    assertTrue(model);
  }

  @Test
  void whenPut_thenSuccess() {
    cacheService.put(new ModelEntity(2L, "name", null, null));

    assertEquals(2, cacheMap.size());
  }

  @Test
  void whenGetAll_thenSuccess() {
    cacheService.put(new ModelEntity(2L, "name", null, null));
    var modelEntityList = cacheService.getAll();
    assertEquals(2, modelEntityList.size());
  }

  @Test
  void whenEvict_thenSuccess() {
    cacheService.remove(modelEntity);

    assertEquals(0, cacheMap.size());
  }
}
