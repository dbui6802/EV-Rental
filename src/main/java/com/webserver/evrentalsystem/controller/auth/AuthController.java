package com.webserver.evrentalsystem.controller.auth;

import com.webserver.evrentalsystem.model.dto.request.RegisterRequest;
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
import org.springframework.web.bind.annotation.*;

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
}
