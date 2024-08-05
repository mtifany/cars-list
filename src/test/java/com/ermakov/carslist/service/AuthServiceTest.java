package com.ermakov.carslist.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ermakov.carslist.exception.UserAlreadyExistsExceptiom;
import com.ermakov.carslist.mapper.UserMapper;
import com.ermakov.carslist.model.entity.RoleEntity;
import com.ermakov.carslist.model.entity.UserEntity;
import com.ermakov.carslist.model.entity.enums.Role;
import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import com.ermakov.carslist.repository.RoleRepository;
import com.ermakov.carslist.repository.UserRepository;
import com.ermakov.carslist.service.impl.AuthServiceImpl;
import com.ermakov.carslist.service.impl.JwtService;
import com.ermakov.carslist.service.impl.UserDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  private static final String EMAIL = "mail@mail.com";

  @Mock
  private JwtService jwtService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private UserDetailsServiceImpl userDetailsService;

  @Mock
  private AuthenticationManager authenticationManager;

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private Authentication authentication;

  private LoginRequest loginRequest = new LoginRequest(EMAIL, "password");

  private RegisterUserRequest registerUserRequest =
      new RegisterUserRequest("name", "surname", EMAIL, "password");

  @Test
  void whenAuthUser_thenSuccess() {
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(authentication.getName()).thenReturn(EMAIL);

    authService.loginUser(loginRequest);

    verify(jwtService).generateToken(loginRequest.email());
  }

  @Test
  void whenRegisterUser_thenSuccess() {
    when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
    when(roleRepository.findByName(Role.ROLE_USER)).thenReturn(Optional.of(new RoleEntity(1L, Role.ROLE_USER)));
    when(userMapper.toUserEntity(registerUserRequest)).thenReturn(new UserEntity());

    authService.registerUser(registerUserRequest);

    verify(userDetailsService).addUser(any());
  }

  @Test
  void whenRegisterExistingUser_thenUserAlreadyExistsExceptionThrown() {
    when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

    assertThrows(UserAlreadyExistsExceptiom.class,
        () -> authService.registerUser(registerUserRequest));

    verify(userDetailsService, times(0)).addUser(any());
  }

  @Test
  void whenRegisterUserAndRoleNotFound_thenExceptionThrown() {
    when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
    when(roleRepository.findByName(Role.ROLE_USER)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> authService.registerUser(registerUserRequest));

    verify(userDetailsService, times(0)).addUser(any());
  }

}
