package com.ermakov.carslist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ermakov.carslist.model.entity.BrandEntity;
import com.ermakov.carslist.model.request.CreateModelRequest;
import com.ermakov.carslist.model.request.EditModelRequest;
import com.ermakov.carslist.repository.BrandRepository;
import com.ermakov.carslist.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModelControllerTest extends AbstractIntegrationTest {

  @MockBean
  private S3Service s3Service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BrandRepository brandRepository;

  private static final String BASE_URL = "/v1/models";
  private static final String REQUEST = "request";

  MockMultipartFile photo;

  @BeforeEach
  void init() {
    photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[1]);
  }

  @Test
  @Order(1)
  @WithMockUser(roles = "USER")
  void whenCreateModel_thenOkReturned() throws Exception {
    var brandId = createBrand("brand");
    var requestDto = new CreateModelRequest("model1", brandId);
    var file =
        new MockMultipartFile("photo", "photo.png", MediaType.IMAGE_PNG_VALUE, "Photo".getBytes());

    when(s3Service.putFile(any(), any(), any())).thenReturn("resource");

    mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
            .file(file)
            .param(REQUEST, asJsonString(requestDto))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("POST");
              return request;
            }))
        .andExpect(status().isOk());

    verify(s3Service).putFile(any(), any(), any());
  }

  @Test
  @Order(2)
  @WithMockUser(roles = "USER")
  void whenFindModelById_thenOkReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
        .andExpect(status().isOk());
  }

  @Test
  @Order(3)
  @WithMockUser(roles = "USER")
  void whenFindModelByIdAndModelNotFound_thenNotFoundReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/100"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(4)
  @WithMockUser(roles = "USER")
  void whenFindModels_thenOkReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "?page=0"))
        .andExpect(status().isOk());
  }

  @Test
  @Order(5)
  @WithMockUser(roles = "USER")
  void whenFindUniqueNames_thenOkReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/names"))
        .andExpect(status().isOk());
  }

  @Test
  @Order(6)
  @WithMockUser(roles = {"USER", "ADMIN"})
  void whenEditModel_thenOkReturned() throws Exception {
    var brandId = createBrand("brand2");
    var editModelRequest = new EditModelRequest(1L, "NewModel", brandId);
    var file =
        new MockMultipartFile("photo", "photo.png", MediaType.IMAGE_PNG_VALUE, "Photo".getBytes());

    when(s3Service.putFile(any(), any(), any())).thenReturn("resource");

    mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
            .file(file)
            .param(REQUEST, asJsonString(editModelRequest))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PUT");
              return request;
            }))
        .andExpect(status().isOk());

    verify(s3Service).deleteFile(any());
    verify(s3Service).putFile(any(), any(), any());
  }

  @Test
  @Order(7)
  @WithMockUser(roles = "USER")
  void whenEditModelAndUserHasNoEditorRole_thenForbiddenReturned() throws Exception {
    var brandId = createBrand("Brand3");
    var editModelRequest = new EditModelRequest(1L, "NewModel", brandId);


    mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
            .param(REQUEST, asJsonString(editModelRequest))
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(9)
  @WithMockUser(roles = "USER")
  void whenDeleteModel_thenNoContentReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
        .andExpect(status().isNoContent());
  }

  private Long createBrand(String name) {
    BrandEntity brandEntity = new BrandEntity();
    brandEntity.setName(name);
    return brandRepository.save(brandEntity).getId();
  }

}