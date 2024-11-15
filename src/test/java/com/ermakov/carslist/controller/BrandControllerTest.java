package com.ermakov.carslist.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ermakov.carslist.model.Brand;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrandControllerTest extends AbstractIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  private static final String BASE_URL = "/v1/brands";

  @Test
  @Order(1)
  @WithMockUser(roles = "USER")
  void whenCreateBrand_thenOkReturned() throws Exception {
    var brand = new Brand(0L, "Brand");
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(brand)))
        .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  @WithMockUser(roles = "USER")
  void whenFindBrandById_thenOkReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
        .andExpect(status().isOk());
  }

  @Test
  @Order(3)
  @WithMockUser(roles = "USER")
  void whenEditBrand_thenOkReturned() throws Exception {
    var brand = new Brand(1L, "Brand2");

    mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(brand)))
        .andExpect(status().isOk());
  }

  @Test
  @Order(4)
  @WithMockUser(roles = "USER")
  void whenDeleteBrand_thenNoContentReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
        .andExpect(status().isNoContent());
  }
}