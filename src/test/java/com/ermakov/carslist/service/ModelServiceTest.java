package com.ermakov.carslist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ermakov.carslist.exception.ModelNotFoundException;
import com.ermakov.carslist.exception.PhotoProcessingException;
import com.ermakov.carslist.mapper.ModelMapper;
import com.ermakov.carslist.model.entity.BrandEntity;
import com.ermakov.carslist.model.entity.ModelEntity;
import com.ermakov.carslist.model.request.CreateModelRequest;
import com.ermakov.carslist.model.request.EditModelRequest;
import com.ermakov.carslist.repository.ModelRepository;
import com.ermakov.carslist.service.impl.BrandServiceImpl;
import com.ermakov.carslist.service.impl.ModelServiceImpl;
import com.ermakov.carslist.specification.CarSpecification;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ModelServiceTest {

  private static final String BRAND_NAME = "Volkswagen";
  private static final String MODEL_NAME = "Bora";
  private static final String AWS_FILE_KEY = "cars/photo.jpg";
  private static final String FILE_NAME = "photo.jpg";
  private static final Long BRAND_ID = 1L;
  private static final Long MODEL_ID = 42L;

  @Mock
  private ModelRepository modelRepository;
  @Mock
  private ModelMapper modelMapper;
  @Mock
  BrandServiceImpl brandService;
  @Mock
  private S3Service s3Service;
  @InjectMocks
  private ModelServiceImpl modelService;
  private CreateModelRequest createModelRequest;
  private ModelEntity modelEntity;
  private BrandEntity brandEntity;
  private MockMultipartFile emptyMultipartFile;
  private MockMultipartFile multipartFile;
  private EditModelRequest editModelRequest;

  @BeforeEach
  void init() {
    emptyMultipartFile = new MockMultipartFile(FILE_NAME, new byte[0]);
    multipartFile = new MockMultipartFile(FILE_NAME, new byte[1]);
    createModelRequest = new CreateModelRequest(MODEL_NAME, BRAND_ID);
    brandEntity = new BrandEntity(BRAND_ID, BRAND_NAME, null);
    modelEntity = new ModelEntity(null, MODEL_NAME,AWS_FILE_KEY,brandEntity);
    editModelRequest = new EditModelRequest(MODEL_ID,MODEL_NAME,BRAND_ID );
  }

  @Test
  void whenCreateModel_thenEntitySaved() {
    when(s3Service.putFile(any(), any(), any())).thenReturn(AWS_FILE_KEY);
    when(brandService.getBrandEntity(any())).thenReturn(brandEntity);

    modelService.createModel(createModelRequest, multipartFile);

    verify(modelRepository).save(modelEntity);
  }

  @Test
  void whenCreateModelAndFileIsEmpty_thenPhotoProcessingExceptionThrown() {

    assertThrows(PhotoProcessingException.class,
        () -> modelService.createModel(createModelRequest, emptyMultipartFile));

    verify(modelRepository, times(0)).save(modelEntity);
    verify(s3Service, times(0)).putFile(any(), any(), any());
  }

  @Test
  void whenEditModel_thenSuccess() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.of(modelEntity));
    when(s3Service.putFile(any(), any(), any())).thenReturn(AWS_FILE_KEY);

    modelService.editModel(editModelRequest, multipartFile);

    assertEquals(MODEL_NAME, modelEntity.getName());
    assertEquals(AWS_FILE_KEY, modelEntity.getPhotoAwsKey());
    verify(modelRepository).save(modelEntity);
    verify(s3Service).deleteFile(any());
    verify(s3Service).putFile(any(), any(), any());
  }

  @Test
  void whenEditModel_thenModelNotFoundExceptionThrown() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.empty());

    assertThrows(ModelNotFoundException.class,
        () -> modelService.editModel(editModelRequest, emptyMultipartFile));

    verify(modelRepository, times(0)).save(modelEntity);
    verify(s3Service, times(0)).deleteFile(modelEntity.getPhotoAwsKey());
    verify(s3Service, times(0)).putFile(any(), any(), any());
  }

  @Test
  void whenEditModelAndFileNotPassed_thenS3ContentNotUpdated() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.of(modelEntity));

    modelService.editModel(editModelRequest, emptyMultipartFile);

    assertEquals(MODEL_NAME, modelEntity.getName());
    assertEquals(AWS_FILE_KEY, modelEntity.getPhotoAwsKey());
    verify(modelRepository).save(modelEntity);
    verify(s3Service, times(0)).deleteFile(modelEntity.getPhotoAwsKey());
    verify(s3Service, times(0)).putFile(any(), any(), any());
  }

  @Test
  void whenFindModels_thenSuccess() {
    when(modelRepository.findAll(any(CarSpecification.class), any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(modelEntity, modelEntity)));

    modelService.getFilteredModelsPaged(null,0);

    verify(modelMapper, times(2)).toModel(any());
  }

  @Test
  void whenFindModelById_thenSuccess() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.of(modelEntity));
    modelService.getModel(MODEL_ID);

    verify(modelMapper).toModel(any());
  }

  @Test
  void whenFindModelById_thenModelNotFoundExceptionThrown() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.empty());

    assertThrows(ModelNotFoundException.class, () -> modelService.getModel(MODEL_ID));
  }

  @Test
  void whenDeleteModel_thenSuccess() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.of(modelEntity));

    modelService.deleteModel(MODEL_ID);

    verify(s3Service).deleteFile(any(String.class));
    verify(modelRepository).delete(any(ModelEntity.class));
  }

  @Test
  void whenDeleteModel_thenModelNotFoundExceptionThrown() {
    when(modelRepository.findById(MODEL_ID)).thenReturn(Optional.empty());

    assertThrows(ModelNotFoundException.class, () -> modelService.deleteModel(MODEL_ID));

    verify(s3Service, times(0)).deleteFile(any(String.class));
    verify(modelRepository, times(0)).delete(any(ModelEntity.class));
  }

  @Test
  void whenGetUniqueModelsNames_thenSuccess() {
    when(modelRepository.findDistinctNames()).thenReturn(Set.of(modelEntity.getName()));

    var uniqueModelsNames = modelService.getUniqueModelsNames();

    assertTrue(uniqueModelsNames.uniqueNames().contains(modelEntity.getName()));
  }
}
