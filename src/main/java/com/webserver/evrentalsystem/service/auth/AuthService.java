package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.SignInRequest;
import com.webserver.evrentalsystem.model.dto.SignInResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest signinRequest, HttpServletResponse httpServletResponse);
    void signOut(HttpServletRequest request, HttpServletResponse response);
}
