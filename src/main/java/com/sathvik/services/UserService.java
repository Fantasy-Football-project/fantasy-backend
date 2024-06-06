//This service will handle the login and register functions/functionality.
package com.sathvik.services;

import com.sathvik.config.UserDto;
import com.sathvik.dto.CredentialsDto;
import com.sathvik.entities.User;
import com.sathvik.exceptions.AppException;
import com.sathvik.mappers.UserMapper;
import com.sathvik.repositories.UserRepository;
import org.springframework.http.HttpStatus;

public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        
    }
}
