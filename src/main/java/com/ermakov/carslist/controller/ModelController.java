package com.ermakov.carslist.controller;

import static com.ermakov.carslist.util.ObjectTransformUtils.getObjectFromJson;

import com.ermakov.carslist.model.CarsFilter;
import com.ermakov.carslist.model.Model;
import com.ermakov.carslist.model.UniqueModelsNames;
import com.ermakov.carslist.model.request.CreateModelRequest;
import com.ermakov.carslist.model.request.EditModelRequest;
import com.ermakov.carslist.service.ModelService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/models")
@Validated
public class ModelController {
  public final ModelService modelService;

  public ModelController(ModelService modelService) {
    this.modelService = modelService;
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {
      MediaType.APPLICATION_JSON_VALUE})
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Model> createModel(@RequestParam("photo") @NotNull MultipartFile photo,
                                           @RequestParam("request") @NotBlank String request) {
    var createModelRequest = getObjectFromJson(request, CreateModelRequest.class);
    return ResponseEntity.ok(modelService.createModel(createModelRequest, photo));
  }

  @GetMapping("/{modelId}")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @ApiResponse(responseCode = "404", description = "NOT_FOUND")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Model> findModelById(@PathVariable("modelId")
                                             @NotNull @Positive Long modelId) {
    return new ResponseEntity<>(modelService.getModel(modelId), HttpStatus.OK);
  }

  @DeleteMapping("/{modelId}")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @ApiResponse(responseCode = "404", description = "NOT_FOUND")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteModel(@PathVariable("modelId")
                                          @NotNull @Positive Long modelId) {
    modelService.deleteModel(modelId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/names")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UniqueModelsNames> getUniqueNames() {
    return ResponseEntity.ok(modelService.getUniqueModelsNames());
  }

  @GetMapping
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Page<Model>> findModels(@RequestParam(required = false) String brandName, @RequestParam(required = false) String modelName, @RequestParam @NotNull @Min(value = 0) Integer page) {
    var carsFilter = new CarsFilter(modelName, brandName);
    Page<Model> modelPage = modelService.getFilteredModelsPaged(carsFilter, page);
    return ResponseEntity.ok(modelPage);
  }

  @PutMapping
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @ApiResponse(responseCode = "404", description = "NOT_FOUND")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Model> editModel(@RequestParam("photo") MultipartFile photo,
                                         @RequestParam("request") @NotBlank String request) {

    var editModelRequest = getObjectFromJson(request, EditModelRequest.class);
    return ResponseEntity.ok(modelService.editModel(editModelRequest, photo));
  }

}
