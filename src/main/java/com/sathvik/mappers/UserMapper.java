package com.sathvik.mappers;

import com.sathvik.dto.UserDto;
import com.sathvik.dto.SignUpDto;
import com.sathvik.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    //Password is ignored because it is not of the same format.
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
