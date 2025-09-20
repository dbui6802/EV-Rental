package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDto getUserInfo(HttpServletRequest request);
}
