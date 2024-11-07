package com.ermakov.carslist.service.impl;


import static com.ermakov.carslist.model.entity.enums.Role.ROLE_USER;

import com.ermakov.carslist.exception.UserAlreadyExistsExceptiom;
import com.ermakov.carslist.mapper.UserMapper;
import com.ermakov.carslist.model.LoginResponse;
import com.ermakov.carslist.model.request.LoginRequest;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import com.ermakov.carslist.repository.RoleRepository;
import com.ermakov.carslist.repository.UserRepository;
import com.ermakov.carslist.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoleRepository roleRepository;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationManager authenticationManager;

  public AuthServiceImpl(JwtService jwtService, UserRepository userRepository,
                         UserMapper userMapper,
                         RoleRepository roleRepository, UserDetailsServiceImpl userDetailsService,
                         AuthenticationManager authenticationManager) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.roleRepository = roleRepository;
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public LoginResponse loginUser(LoginRequest loginRequest) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    return new LoginResponse(jwtService.generateToken(authentication.getName()));
  }

  @Override
  public void registerUser(RegisterUserRequest registerUserRequest) {
    if (userRepository.existsByEmail(registerUserRequest.email())) {
      throw new UserAlreadyExistsExceptiom();
    }
    var roleEntity = roleRepository.findByName(ROLE_USER)
        .orElseThrow(EntityNotFoundException::new);

    var userEntity = userMapper.toUserEntity(registerUserRequest);
    userEntity.addRole(roleEntity);

    userDetailsService.addUser(userEntity);
  }
}
