package com.ermakov.carslist.aspect;

import com.ermakov.carslist.model.entity.ModelEntity;
import com.ermakov.carslist.service.cache.ModelCacheService;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAspect {
  private final ModelCacheService modelCacheService;

  public CacheAspect(ModelCacheService modelCacheService) {
    this.modelCacheService = modelCacheService;
  }

  @Around(
      value = """
          execution(* com.ermakov.carslist.repository.ModelRepository
          .findAll(org.springframework.data.domain.Pageable))
          """)
  public Object getModels(ProceedingJoinPoint joinPoint) {
    var pageable = (Pageable) joinPoint.getArgs()[0];
    return getPage(modelCacheService.getAll(), pageable);
  }

  @Around(value = "execution(* com.ermakov.carslist.repository.ModelRepository.findById(..))")
  public Object getModelById(ProceedingJoinPoint joinPoint) {
    var entityId = (Long) joinPoint.getArgs()[0];
    return modelCacheService.get(entityId);
  }

  @Around(value = "execution(* com.ermakov.carslist.repository.ModelRepository.save(..))")
  public Object saveModel(ProceedingJoinPoint joinPoint) throws Throwable {
    var modelEntity = (ModelEntity) joinPoint.proceed();
    modelCacheService.put(modelEntity);
    return modelEntity;
  }

  @Around(value = "execution(* com.ermakov.carslist.repository.ModelRepository.delete(..))")
  public Object deleteModel(ProceedingJoinPoint joinPoint) throws Throwable {
    var modelEntity = (ModelEntity) joinPoint.getArgs()[0];
    joinPoint.proceed();
    return modelEntity;
  }

  private <T> Page<T> getPage(List<T> filteredItems, Pageable pageable) {
    return new PageImpl<>(filteredItems, pageable, pageable.getPageSize());
  }

}
