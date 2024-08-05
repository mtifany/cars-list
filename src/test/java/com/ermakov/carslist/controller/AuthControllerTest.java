package com.ermakov.carslist.controller;

import static com.ermakov.carslist.controller.ModelControllerTest.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import com.ermakov.carslist.service.AuthService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
  @MockBean
  AuthService authService;
  @Autowired
  private MockMvc mockMvc;
  RegisterUserRequest request =
      new RegisterUserRequest("name", "surname", "email@mail.ru", "password");


  @Test
  @SneakyThrows
  void whenRegisterUser_thenCreatedReturned() {
    mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/register")
            .content(asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());
  }

  @ParameterizedTest
  @SneakyThrows
  @ValueSource(strings = {"", "emailmail.ru", "email@", "@mail.ru", "email"})
  void whenRegisterUser_thenBadRequestReturned(String invalidEmail) {
    var request = new RegisterUserRequest("name", "surname", invalidEmail, "password");

    mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/register")
            .content(asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void whenLoginUser_thenSuccess() {
    LoginRequest loginRequest = new LoginRequest("email", "password");
    mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
            .content(asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void whenLoginUser_thenBadRequest() {
    LoginRequest loginRequest = new LoginRequest("", "password");
    mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
            .content(asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }
}
