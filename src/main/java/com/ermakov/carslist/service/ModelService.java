package com.ermakov.carslist.service;

import com.ermakov.carslist.model.CarsFilter;
import com.ermakov.carslist.model.request.CreateModelRequest;
import com.ermakov.carslist.model.request.EditModelRequest;
import com.ermakov.carslist.model.Model;
import com.ermakov.carslist.model.UniqueModelsNames;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ModelService {
  Model createModel(CreateModelRequest modelRequest, MultipartFile photo);
  Model getModel(Long id);
  Model editModel(EditModelRequest request, MultipartFile photo);
  void deleteModel(Long modelId);
  Page<Model> getFilteredModelsPaged(CarsFilter carsFilter, Integer pageNumber);
  UniqueModelsNames getUniqueModelsNames();
}
