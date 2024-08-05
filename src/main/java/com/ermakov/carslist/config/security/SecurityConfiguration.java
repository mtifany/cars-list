package com.ermakov.carslist.config.security;

import static com.ermakov.carslist.model.entity.enums.Role.ROLE_ADMIN;
import static com.ermakov.carslist.model.entity.enums.Role.ROLE_USER;

import com.ermakov.carslist.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                 JwtAuthFilter jwtAuthFilter) throws Exception {
    return httpSecurity
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers(HttpMethod.POST, "/v1/auth/*").permitAll();
          auth.requestMatchers(HttpMethod.PUT, "/v1/models")
              .hasAuthority(ROLE_ADMIN.name());
          auth.requestMatchers("/v1/models/*")
              .hasAnyAuthority(ROLE_USER.name(), ROLE_ADMIN.name());
          auth.requestMatchers("/v1/brands")
              .hasAnyAuthority(ROLE_USER.name(), ROLE_ADMIN.name());
          auth.anyRequest().authenticated();
        })
        .sessionManagement(
            session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
                                                       UserDetailsServiceImpl userDetailsService) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}