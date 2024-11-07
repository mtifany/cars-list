package com.ermakov.carslist.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends AbstractIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  private static final String REG_URL = "/v1/auth/register";
  private static final String AUTH_URL = "/v1/auth/login";

  RegisterUserRequest request =
      new RegisterUserRequest("name", "surname", "email@mail.ru", "password");

  @Test
  @Order(1)
  void whenRegisterUser_thenCreatedReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post(REG_URL)
            .content(asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());
  }

  @Test
  @Order(2)
  void whenRegisterUserAndUserExists_thenBadRequestReturned() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post(REG_URL)
            .content(asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "emailmail.ru", "email@", "@mail.ru", "email"})
  @Order(3)
  void whenRegisterUser_thenBadRequestReturned(String invalidEmail) throws Exception {
    var userRequest = new RegisterUserRequest("name", "surname", invalidEmail, "password");

    mockMvc.perform(MockMvcRequestBuilders.post(REG_URL)
            .content(asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  void whenLoginUser_thenSuccess() throws Exception {
    var loginRequest = new LoginRequest("email@mail.ru", "password");
    mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
            .content(asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  @Test
  @Order(5)
  void whenAuthUserAndNoUserFound_thenForbiddenReturned() throws Exception {
    var loginRequest = new LoginRequest("email@mail.ru2", "password");

    mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(loginRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(6)
  void whenAuthUserAndWrongPassword_thenForbiddenReturned() throws Exception {
    var loginRequest = new LoginRequest("email@mail.ru", "wrong password");

    mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(loginRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(7)
  void whenLoginUser_thenBadRequest() throws Exception {
    var loginRequest = new LoginRequest("", "password");
    mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
            .content(asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }
}