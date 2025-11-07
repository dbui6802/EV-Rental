package com.webserver.evrentalsystem.service.auth.impl;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.model.dto.request.ChangePasswordRequest;
import com.webserver.evrentalsystem.model.dto.request.RegisterRequest;
import com.webserver.evrentalsystem.model.dto.response.RegisterResponse;
import com.webserver.evrentalsystem.model.dto.request.SignInRequest;
import com.webserver.evrentalsystem.model.dto.response.SignInResponse;
import com.webserver.evrentalsystem.model.mapping.UserMapper;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.auth.AuthService;
import com.webserver.evrentalsystem.service.email.EmailService;
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

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

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

    @Autowired
    private EmailService emailService;

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes
    private static final long RESET_TOKEN_VALID_DURATION_MINUTES = 15; // 15 minutes


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

        if (!user.isVerified()) {
            throw new InvalidateParamsException("Tài khoản của bạn chưa được xác thực");
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new InvalidateParamsException("Tài khoản của bạn đã bị vô hiệu hóa, vui lòng liên hệ quản trị viên");
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

        validatePassword(password);

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // validate email
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!request.getEmail().matches(emailRegex)) {
                throw new InvalidateParamsException("Email không hợp lệ");
            }
        } else {
            throw new InvalidateParamsException("Email không được để trống");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidateParamsException("Email đã được đăng ký");
        }

        User newUser = new User();
        newUser.setPhone(phone);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFullName(fullName);
        newUser.setEmail(request.getEmail());
        newUser.setRole(Role.RENTER); // default role is RENTER
        newUser.setCreatedAt(LocalDateTime.now());

        String otp = generateOtp();
        newUser.setOtp(otp);
        newUser.setOtpRequestedTime(LocalDateTime.now());

        userRepository.save(newUser);
        emailService.sendOtp(newUser.getEmail(), otp);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setMessage("Đăng ký thành công, vui lòng kiểm tra email để xác thực");

        return registerResponse;
    }

    @Override
    public void verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Người dùng không tồn tại");
        }

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new InvalidateParamsException("OTP không hợp lệ");
        }

        long otpRequestedTimeMillis = user.getOtpRequestedTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (otpRequestedTimeMillis + OTP_VALID_DURATION < System.currentTimeMillis()) {
            throw new InvalidateParamsException("OTP đã hết hạn");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpRequestedTime(null);
        userRepository.save(user);
    }

    @Override
    public void resendOtp(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Người dùng không tồn tại");
        }

        if (user.isVerified()) {
            throw new InvalidateParamsException("Tài khoản đã được xác thực");
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpRequestedTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtp(user.getEmail(), otp);
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Không tìm thấy người dùng với email này");
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(RESET_TOKEN_VALID_DURATION_MINUTES));
        userRepository.save(user);

        // You need to implement sendPasswordResetEmail in your EmailService
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new InvalidateParamsException("Token không hợp lệ hoặc đã hết hạn");
        }

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidateParamsException("Token đã hết hạn");
        }

        validatePassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = userValidation.validateUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidateParamsException("Mật khẩu hiện tại không chính xác");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new InvalidateParamsException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        validatePassword(request.getNewPassword());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void validatePassword(String password) {
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
    }
}
