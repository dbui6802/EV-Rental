package com.webserver.evrentalsystem.service.auth;

import com.webserver.evrentalsystem.model.dto.request.ChangePasswordRequest;
import com.webserver.evrentalsystem.model.dto.request.RegisterRequest;
import com.webserver.evrentalsystem.model.dto.response.RegisterResponse;
import com.webserver.evrentalsystem.model.dto.request.SignInRequest;
import com.webserver.evrentalsystem.model.dto.response.SignInResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthService {
    SignInResponse signIn(SignInRequest signinRequest, HttpServletResponse httpServletResponse);
    void signOut(HttpServletResponse response);
    RegisterResponse register(RegisterRequest request, HttpServletResponse response);
    Map<String, String> verifyOtp(String email, String otp);
    void resendOtp(String email);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    void changePassword(ChangePasswordRequest request);

}
