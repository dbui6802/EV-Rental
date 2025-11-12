package com.webserver.evrentalsystem.service.email;

public interface EmailService {
    void sendOtp(String to, String otp);
}
