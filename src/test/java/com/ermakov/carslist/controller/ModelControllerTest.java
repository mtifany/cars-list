package com.ermakov.carslist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.model.Model;
import com.ermakov.carslist.model.request.CreateModelRequest;
import com.ermakov.carslist.model.request.EditModelRequest;
import com.ermakov.carslist.service.ModelService;
import com.ermakov.carslist.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class ModelControllerTest {
  private static final String BASE_URL = "/v1/models";
  private static final String REQUEST = "request";

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  ModelService modelService;
  @MockBean
  S3Service s3Service;

  CreateModelRequest createModelRequest;
  Brand brand;
  Model model;
  EditModelRequest editModelRequest;
  MockMultipartFile photo;

  @BeforeEach
  void init() {
    createModelRequest = new CreateModelRequest("Cooper JCW", 1L);
    brand = new Brand(1L, "Mini");
    model = new Model(1L,"Cooper JCW", "photo", brand);
    photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[1]);
    editModelRequest = new EditModelRequest(100L, "NewModel", 1L);

  }

  @Test
  @SneakyThrows
  @WithMockUser(roles = "USER")
  void whenCreateModel_thenSuccess() {
    when(modelService.createModel(any(), any())).thenReturn(model);

    mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
            .file(photo)
            .param(REQUEST, asJsonString(createModelRequest))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("POST");
              return request;
            }))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  @WithMockUser(roles = "USER")
  void whenGetModel_thenSuccess() {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void whenGetModel_thenForbidden() {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenFindUniqueNames_thenSuccess() {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/names"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = {"USER", "ADMIN"})
  @SneakyThrows
  void whenEditModel_thenSuccess() {
    mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
            .file(photo)
            .param(REQUEST, asJsonString(editModelRequest))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PUT");
              return request;
            }))
        .andExpect(status().isOk());

  }

  @Test
  @WithMockUser(roles = {"USER", "ADMIN"})
  @SneakyThrows
  void whenEditModelAndRequestIdEmpty_thenBadRequest() {
    mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
            .file(photo)
            .param(REQUEST, asJsonString(Strings.EMPTY))
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenEditModelAndUserIsNotAdmin_thenForbiddenReturned() {

    mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
            .file(photo)
            .param(REQUEST, asJsonString(editModelRequest))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(request -> {
              request.setMethod("PUT");
              return request;
            }))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenDelete_thenNoContent() {
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
        .andExpect(status().isNoContent());
  }


  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
