package com.ermakov.carslist.service;

import static com.ermakov.carslist.model.entity.enums.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ermakov.carslist.model.entity.RoleEntity;
import com.ermakov.carslist.model.entity.UserEntity;
import com.ermakov.carslist.repository.UserRepository;
import com.ermakov.carslist.service.impl.UserDetailsServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void whenLoadUserByUsername_thenSuccess() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(ROLE_USER.name());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.addRole(roleEntity);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));

        UserDetails details = userDetailsService.loadUserByUsername(EMAIL);

        assertEquals(userEntity.getEmail(), details.getUsername());
        assertEquals(userEntity.getPassword(), details.getPassword());
        assertFalse(details.getAuthorities().isEmpty());
    }

    @Test
    void whenLoadUserByUsernameAndUserNotFound_thenExceptionThrown() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(EMAIL));
    }

    @Test
    void whenAddUser_thenSuccess() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(PASSWORD);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);

        userDetailsService.addUser(userEntity);

        verify(userRepository).save(userEntity);
    }

}
