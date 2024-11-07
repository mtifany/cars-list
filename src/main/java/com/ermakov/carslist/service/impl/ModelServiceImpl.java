package com.ermakov.carslist.service.impl;

import static com.ermakov.carslist.util.ObjectTransformUtils.getPhotoBytes;

import com.ermakov.carslist.exception.ModelNotFoundException;
import com.ermakov.carslist.exception.PhotoProcessingException;
import com.ermakov.carslist.mapper.ModelMapper;
import com.ermakov.carslist.model.CarsFilter;
import com.ermakov.carslist.model.Model;
import com.ermakov.carslist.model.UniqueModelsNames;
import com.ermakov.carslist.model.entity.ModelEntity;
import com.ermakov.carslist.model.request.CreateModelRequest;
import com.ermakov.carslist.model.request.EditModelRequest;
import com.ermakov.carslist.repository.ModelRepository;
import com.ermakov.carslist.service.ModelService;
import com.ermakov.carslist.service.S3Service;
import com.ermakov.carslist.specification.CarSpecification;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ModelServiceImpl implements ModelService {
  private final ModelRepository modelRepository;
  private final S3Service s3Service;
  private final ModelMapper modelMapper;
  private final BrandServiceImpl brandService;
  private static final int PAGE_SIZE = 10;

  public ModelServiceImpl(ModelRepository modelRepository, S3Service s3Service,
                          ModelMapper modelMapper, BrandServiceImpl brandService) {
    this.modelRepository = modelRepository;
    this.s3Service = s3Service;
    this.modelMapper = modelMapper;
    this.brandService = brandService;
  }

  @Override
  public Model createModel(CreateModelRequest modelRequest, MultipartFile photo) {
    if (photo.isEmpty()) {
      throw new PhotoProcessingException();
    }
    var brandEntity = brandService.getBrandEntity(modelRequest.brandId());
    var photoAwsKey = s3Service.putFile(UUID.randomUUID().toString(),
        FilenameUtils.getExtension(photo.getOriginalFilename()), getPhotoBytes(photo));
    var model = new ModelEntity(null, modelRequest.name(), photoAwsKey, brandEntity);
    return modelMapper.toModel(modelRepository.save(model));
  }

  @Override
  public Model getModel(Long modelId) {
    var modelEntity = getModelEntity(modelId);
    return modelMapper.toModel(modelEntity);
  }

  @Transactional
  @Override
  public Model editModel(EditModelRequest request, MultipartFile photo) {
    var modelEntity = getModelEntity(request.modelId());
    if (request.name() != null) {
      modelEntity.setName(request.name());
    }
    if (request.brandId() != null) {
      var brandEntity = brandService.getBrandEntity(request.brandId());
      modelEntity.setBrandEntity(brandEntity);
    }
    if (!photo.isEmpty()) {
      var photoAwsKey = modelEntity.getPhotoAwsKey();
      if (!photoAwsKey.isEmpty()) {
        s3Service.deleteFile(photoAwsKey);
      }
      photoAwsKey = s3Service.putFile(UUID.randomUUID().toString(),
          FilenameUtils.getExtension(photo.getOriginalFilename()), getPhotoBytes(photo));
      modelEntity.setPhotoAwsKey(photoAwsKey);
    }
    return modelMapper.toModel(modelRepository.save(modelEntity));
  }

  @Override
  public void deleteModel(Long modelId) {
    var model = getModelEntity(modelId);
    s3Service.deleteFile(model.getPhotoAwsKey());
    modelRepository.delete(model);
  }

  @Override
  public UniqueModelsNames getUniqueModelsNames() {
    var modelsSet = modelRepository.findDistinctNames();
    return new UniqueModelsNames(modelsSet);
  }

  @Override
  public Page<Model> getFilteredModelsPaged(CarsFilter carsFilter, Integer pageNumber) {
    var pageable = PageRequest.of(pageNumber, PAGE_SIZE);
    var modelEntities = modelRepository.findAll(new CarSpecification(carsFilter), pageable);
    return modelEntities.map(modelMapper::toModel);
  }

  private ModelEntity getModelEntity(Long modelId) {
    return modelRepository.findById(modelId).orElseThrow(() -> new ModelNotFoundException(modelId));
  }
}
