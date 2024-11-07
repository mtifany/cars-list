package com.ermakov.carslist.service.impl;

import com.ermakov.carslist.model.UserAuthDetails;
import com.ermakov.carslist.model.entity.UserEntity;
import com.ermakov.carslist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    var userDetail = userRepository.findByEmail(email);
    return userDetail.map(UserAuthDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
  }

  public void addUser(UserEntity userEntity) {
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    userRepository.save(userEntity);
  }
}
