package com.ermakov.carslist.service;

import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.model.entity.BrandEntity;

public interface BrandService {
  Brand createBrand(Brand brand);

  Brand getBrand(Long brandId);

  Brand editBrand(Long brandId, Brand brand);

  void deleteBrand(Long brandId);

  BrandEntity getBrandEntity(Long brandId);

}
