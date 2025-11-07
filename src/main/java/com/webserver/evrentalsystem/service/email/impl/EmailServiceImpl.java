package com.webserver.evrentalsystem.service.email.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.webserver.evrentalsystem.service.email.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOtp(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your OTP Code");

            String htmlContent = """
                <html>
                    <body>
                        <h1>Welcome to EV Rental System!</h1>
                        <p>Your One-Time Password (OTP) is: <strong>%s</strong></p>
                        <p>This OTP is valid for 5 minutes.</p>
                        <p>If you did not request this, please ignore this email.</p>
                    </body>
                </html>
            """.formatted(otp);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Password Reset Request");

            String resetUrl = "http://localhost:3000/reset-password?token=" + token;

            String htmlContent = String.format("""
                <html>
                    <body>
                        <h1>Password Reset Request</h1>
                        <p>You have requested to reset your password. Click the link below to reset it:</p>
                        <a href="%s">Reset Password</a>
                        <p>This link is valid for 15 minutes.</p>
                        <p>If you did not request this, please ignore this email.</p>
                    </body>
                </html>
            """, resetUrl);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
