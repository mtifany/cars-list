package com.ermakov.carslist.model;

import com.ermakov.carslist.model.entity.UserEntity;
import com.ermakov.carslist.model.entity.RoleEntity;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthDetails implements UserDetails {

    private final String email;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;

    public UserAuthDetails(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.authorities = userEntity.getRoles().stream()
                .map(RoleEntity::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    }
