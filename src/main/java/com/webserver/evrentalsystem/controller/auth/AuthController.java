package com.webserver.evrentalsystem.controller.auth;

import com.webserver.evrentalsystem.model.dto.request.ChangePasswordRequest;
import com.webserver.evrentalsystem.model.dto.request.ForgotPasswordRequest;
import com.webserver.evrentalsystem.model.dto.request.OtpRequest;
import com.webserver.evrentalsystem.model.dto.request.RegisterRequest;
import com.webserver.evrentalsystem.model.dto.request.ResetPasswordRequest;
import com.webserver.evrentalsystem.model.dto.response.RegisterResponse;
import com.webserver.evrentalsystem.model.dto.request.SignInRequest;
import com.webserver.evrentalsystem.model.dto.response.SignInResponse;
import com.webserver.evrentalsystem.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value="api/auth")
@Tag(name = "1. Authentication", description = "APIs liên quan đến đăng nhập/đăng ký/đăng xuất")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Đăng nhập", description = "Xác thực người dùng bằng email/số điện thoại và mật khẩu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công",
                    content = @Content(schema = @Schema(implementation = SignInResponse.class))),
            @ApiResponse(responseCode = "401", description = "Sai thông tin đăng nhập"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signinRequest, HttpServletResponse httpServletResponse) {
        SignInResponse signinResponse = authService.signIn(signinRequest, httpServletResponse);
        return ResponseEntity.ok(signinResponse);
    }

    @Operation(summary = "Đăng xuất", description = "Xoá access-token/refresh-token của người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng xuất thành công")
    })
    @PostMapping(value = "/sign-out")
    public void signOut(HttpServletResponse response) {
        authService.signOut(response);
    }

    @Operation(summary = "Đăng ký", description = "Tạo mới tài khoản cho renter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng ký thành công",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Thông tin đăng ký không hợp lệ")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        RegisterResponse registerResponse = authService.register(registerRequest, response);
        return ResponseEntity.ok(registerResponse);
    }

    @Operation(summary = "Xác thực OTP", description = "Xác thực OTP cho việc đăng ký hoặc quên mật khẩu. Nếu là quên mật khẩu, sẽ trả về token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xác thực OTP thành công"),
            @ApiResponse(responseCode = "400", description = "OTP không hợp lệ hoặc đã hết hạn")
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody OtpRequest request) {
        Map<String, String> result = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Gửi lại OTP", description = "Gửi lại OTP mới đến email của người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gửi lại OTP thành công")
    })
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody ForgotPasswordRequest request) {
        authService.resendOtp(request.getEmail());
        return ResponseEntity.ok("OTP resent successfully");
    }

    @Operation(summary = "Quên mật khẩu", description = "Gửi email chứa OTP đặt lại mật khẩu cho người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email chứa OTP đã được gửi đi"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng với email này")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("OTP has been sent successfully");
    }

    @Operation(summary = "Đặt lại mật khẩu", description = "Đặt lại mật khẩu mới bằng token đã nhận")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đặt lại mật khẩu thành công"),
            @ApiResponse(responseCode = "400", description = "Token không hợp lệ hoặc đã hết hạn")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully");
    }

    @Operation(summary = "Đổi mật khẩu", description = "Thay đổi mật khẩu cho người dùng đã đăng nhập")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
