package com.ermakov.carslist.mapper;

import com.ermakov.carslist.model.entity.UserEntity;
import com.ermakov.carslist.model.request.RegisterUserRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  UserEntity toUserEntity(RegisterUserRequest registerUserRequest);
}
