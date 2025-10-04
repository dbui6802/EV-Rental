package com.webserver.evrentalsystem.service.auth;

import com.webserver.evrentalsystem.model.dto.request.RegisterRequest;
import com.webserver.evrentalsystem.model.dto.response.RegisterResponse;
import com.webserver.evrentalsystem.model.dto.request.SignInRequest;
import com.webserver.evrentalsystem.model.dto.response.SignInResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest signinRequest, HttpServletResponse httpServletResponse);
    void signOut(HttpServletResponse response);
    RegisterResponse register(RegisterRequest request, HttpServletResponse response);
}
