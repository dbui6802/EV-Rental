package com.webserver.evrentalsystem.service.auth.impl;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.model.dto.request.RegisterRequest;
import com.webserver.evrentalsystem.model.dto.response.RegisterResponse;
import com.webserver.evrentalsystem.model.dto.request.SignInRequest;
import com.webserver.evrentalsystem.model.dto.response.SignInResponse;
import com.webserver.evrentalsystem.model.mapping.UserMapper;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.auth.AuthService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import com.webserver.evrentalsystem.utils.CookieUtils;
import com.webserver.evrentalsystem.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${JWT_ACCESS_TOKEN_EXPIRATION_MS}")
    private long jwtAccessTokenExpirationMs;

    @Value("${JWT_REFRESH_TOKEN_EXPIRATION_MS}")
    private long jwtRefreshTokenExpirationMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public SignInResponse signIn(SignInRequest signinRequest, HttpServletResponse httpServletResponse) {
        String phone = signinRequest.getPhone();
        String password = signinRequest.getPassword();

        if (phone.isEmpty() || password.isEmpty()) {
            throw new InvalidateParamsException("Số điện thoại và mật khẩu không được để trống");
        }

        User user = userRepository.findByPhone(phone);

        if (user == null) {
            throw new UserNotFoundException("Người dùng không tồn tại");
        }

        String hashedPassword = user.getPassword();

        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new InvalidateParamsException("Mật khẩu không chính xác");
        }

        String accessToken = jwtUtils.generateJwtAccessToken(user);
        String refreshToken = jwtUtils.generateJwtRefreshToken(user);

        // save refresh token to database
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        SignInResponse signinResponse = new SignInResponse();
        signinResponse.setUserInfo(userMapper.toUserDto(user));
        signinResponse.setAccessToken(accessToken);
        signinResponse.setRefreshToken(refreshToken);

        // save access token to cookie
        ResponseCookie accessTokenCookie = CookieUtils.createCookie(CookieUtils.ACCESS_TOKEN, accessToken, (int) (jwtAccessTokenExpirationMs / 1000));
        ResponseCookie refreshTokenCookie = CookieUtils.createCookie(CookieUtils.REFRESH_TOKEN, refreshToken, (int) (jwtRefreshTokenExpirationMs / 1000));
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return signinResponse;
    }

    @Override
    public void signOut(HttpServletResponse httpServletResponse) {
        User user = userValidation.validateUser();

        user.setRefreshToken(null);
        userRepository.save(user);

        // remove access token and refresh token from cookie
        ResponseCookie accessTokenCookie = CookieUtils.createCookie(CookieUtils.ACCESS_TOKEN, null, 0);
        ResponseCookie refreshTokenCookie = CookieUtils.createCookie(CookieUtils.REFRESH_TOKEN, null, 0);
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    @Override
    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {
        String phone = request.getPhone();
        String password = request.getPassword();
        String fullName = request.getFullName();

        if (phone.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            throw new InvalidateParamsException("Số điện thoại, mật khẩu và tên không được để trống");
        }

        User existingUser = userRepository.findByPhone(phone);
        if (existingUser != null) {
            throw new InvalidateParamsException("Số điện thoại đã được đăng ký");
        }

        // validate Viet Nam phone number
        String regex = "^(?:\\+84|0)(?:3[2-9]|5(?:6|8|9)|7(?:0|6|7|8|9)|8[1-9]|9[0-9])\\d{7}$";
        if (!phone.matches(regex)) {
            throw new InvalidateParamsException("Số điện thoại không hợp lệ");
        }

        // check strong password
        if (password.length() < 8) {
            throw new InvalidateParamsException("Mật khẩu phải có ít nhất 8 ký tự");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidateParamsException("Mật khẩu phải có ít nhất 1 ký tự in hoa");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new InvalidateParamsException("Mật khẩu phải có ít nhất 1 ký tự in thường");
        }
        if (!password.matches(".*\\d.*")) {
            throw new InvalidateParamsException("Mật khẩu phải có ít nhất 1 chữ số");
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new InvalidateParamsException("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // validate email
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!request.getEmail().matches(emailRegex)) {
                throw new InvalidateParamsException("Email không hợp lệ");
            }
        }

        User newUser = new User();
        newUser.setPhone(phone);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFullName(fullName);
        newUser.setEmail(request.getEmail());
        newUser.setRole(Role.RENTER); // default role is RENTER

        userRepository.save(newUser);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setMessage("Đăng ký thành công");

        return registerResponse;
    }
}
