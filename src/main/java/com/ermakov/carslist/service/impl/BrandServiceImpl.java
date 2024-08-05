package com.ermakov.carslist.service.impl;

import com.ermakov.carslist.exception.BrandNotFoundException;
import com.ermakov.carslist.mapper.BrandMapper;
import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.model.entity.BrandEntity;
import com.ermakov.carslist.repository.BrandRepository;
import com.ermakov.carslist.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
  private final BrandRepository brandRepository;
  private final BrandMapper brandMapper;

  @Override
  public Brand createBrand(Brand brand) {
    var brandEntity = brandMapper.toBrandEntity(brand);
    return brandMapper.toBrand(brandRepository.save(brandEntity));
  }

  @Override
  public Brand getBrand(Long brandId) {
    var brandEntity = getBrandEntity(brandId);
    return brandMapper.toBrand(brandRepository.save(brandEntity));
  }

  @Transactional
  @Override
  public Brand editBrand(Long brandId, Brand brand) {
    var brandEntity = getBrandEntity(brandId);
    brandEntity.setName(brand.name());
    return brandMapper.toBrand(brandRepository.save(brandEntity));
  }

  @Override
  public void deleteBrand(Long brandId) {
    var brandEntity = getBrandEntity(brandId);
    brandRepository.delete(brandEntity);
  }
  @Override
  public BrandEntity getBrandEntity(Long brandId) {
    return brandRepository.findById(brandId).orElseThrow(()-> new BrandNotFoundException(brandId));
  }
}
