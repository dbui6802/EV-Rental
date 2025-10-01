package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.UserDto;
import com.webserver.evrentalsystem.service.UserService;
import com.webserver.evrentalsystem.utils.Logger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/get-info")
    public ResponseEntity<?> getInfo(HttpServletRequest request) {
        UserDto userDto = userService.getUserInfo(request);
        return ResponseEntity.ok(userDto);
    }
}
