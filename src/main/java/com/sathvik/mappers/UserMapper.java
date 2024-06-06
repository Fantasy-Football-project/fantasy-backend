package com.sathvik.mappers;

import com.sathvik.config.UserDto;
import com.sathvik.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
}
