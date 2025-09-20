package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.UserDto;
import com.webserver.evrentalsystem.model.mapping.UserMapping;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.UserService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapping userMapping;

    @Override
    public UserDto getUserInfo(HttpServletRequest request) {
        User user = UserValidation.validateUser(userRepository, request);
        return userMapping.toUserDto(user);
    }
}
