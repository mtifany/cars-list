package com.ermakov.carslist.controller;

import static com.ermakov.carslist.controller.ModelControllerTest.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.service.impl.BrandServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerTest {

  private static final String BASE_URL = "/v1/brands";
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  BrandServiceImpl brandService;

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenCreateBrand_thenOk() {
    var brand = new Brand(1L, "Mini");

    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(brand)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenFindBrandById_thenOk() {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenEditBrand_thenOkReturned() {
    var brand = new Brand(2L, "BMW");

    mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(brand)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  @SneakyThrows
  void whenDeleteBrand_thenNoContentReturned() {
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
        .andExpect(status().isNoContent());
  }

}
