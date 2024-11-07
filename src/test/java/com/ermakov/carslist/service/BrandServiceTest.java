package com.ermakov.carslist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ermakov.carslist.exception.BrandNotFoundException;
import com.ermakov.carslist.mapper.BrandMapper;
import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.model.entity.BrandEntity;
import com.ermakov.carslist.repository.BrandRepository;
import com.ermakov.carslist.service.impl.BrandServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
  private static final Long BRAND_ID = 1L;

  @Mock
  BrandRepository brandRepository;
  @Mock
  BrandMapper brandMapper;
  @InjectMocks
  BrandServiceImpl brandService;

  private Brand brand;
  private BrandEntity brandEntity;

  @BeforeEach
  void init() {
    brand = new Brand(1L,"Mini");
    brandEntity = new BrandEntity(BRAND_ID, "Fiat", null);
  }

  @Test
  void whenCreateBrand_thenSuccess() {
    when(brandMapper.toBrandEntity(brand)).thenReturn(brandEntity);

    brandService.createBrand(brand);

    verify(brandRepository).save(brandEntity);
  }

  @Test
  void whenGetBrand_thenSuccess() {
    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brandEntity));

    brandService.getBrand(BRAND_ID);

    verify(brandRepository).findById(BRAND_ID);
  }

  @Test
  void whenGetBrand_thenBrandNotFoundExceptionThrown() {
    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

    assertThrows(BrandNotFoundException.class, () -> brandService.getBrand(BRAND_ID));
  }

  @Test
  void whenEditBrand_thenSuccess() {
    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brandEntity));

    brandService.editBrand(BRAND_ID, brand);

    assertEquals(brand.name(), brandEntity.getName());
    verify(brandRepository).findById(BRAND_ID);
  }

  @Test
  void whenEditBrand_thenBrandNotFoundExceptionThrown() {
    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

    assertThrows(BrandNotFoundException.class, () -> brandService.editBrand(BRAND_ID, brand));
  }

  @Test
  void whenDeleteBrand_thenSuccess() {
    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.of(brandEntity));

    brandService.deleteBrand(BRAND_ID);

    verify(brandRepository).delete(brandEntity);
  }

  @Test
  void whenDeleteBrand_thenBrandNotFoundExceptionThrown() {
    when(brandRepository.findById(BRAND_ID)).thenReturn(Optional.empty());

    assertThrows(BrandNotFoundException.class, () -> brandService.deleteBrand(BRAND_ID));
  }

}
