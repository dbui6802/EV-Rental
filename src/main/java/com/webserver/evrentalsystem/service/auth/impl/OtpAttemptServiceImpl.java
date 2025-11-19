package com.webserver.evrentalsystem.service.auth.impl;

import com.webserver.evrentalsystem.exception.TooManyRequestsException;
import com.webserver.evrentalsystem.service.auth.OtpAttemptService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpAttemptServiceImpl implements OtpAttemptService {

    private static final int MAX_ATTEMPTS = 5; // Số lần thử sai tối đa
    private static final Duration LOCKOUT_DURATION = Duration.ofMinutes(15); // Thời gian khóa: 15 phút

    private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lockoutCache = new ConcurrentHashMap<>();

    @Override
    public void recordFailedAttempt(String key) {
        // Nếu đang bị khóa, không cần làm gì thêm
        if (isLocked(key)) {
            return;
        }

        int attempts = attemptsCache.getOrDefault(key, 0) + 1;

        if (attempts >= MAX_ATTEMPTS) {
            // Khóa tài khoản
            lockoutCache.put(key, LocalDateTime.now().plus(LOCKOUT_DURATION));
            attemptsCache.remove(key); // Xóa bộ đếm sau khi đã khóa
        } else {
            // Cập nhật bộ đếm
            attemptsCache.put(key, attempts);
        }
    }

    @Override
    public void resetAttempts(String key) {
        attemptsCache.remove(key);
        lockoutCache.remove(key);
    }

    @Override
    public void checkLockout(String key) {
        if (isLocked(key)) {
            LocalDateTime lockoutTime = lockoutCache.get(key);
            long minutesRemaining = Duration.between(LocalDateTime.now(), lockoutTime).toMinutes();
            throw new TooManyRequestsException("Bạn đã nhập sai OTP quá nhiều lần. Vui lòng thử lại sau " + (minutesRemaining + 1) + " phút.");
        }
    }

    private boolean isLocked(String key) {
        LocalDateTime lockoutTime = lockoutCache.get(key);
        if (lockoutTime == null) {
            return false; // Không bị khóa
        }

        if (LocalDateTime.now().isAfter(lockoutTime)) {
            // Hết thời gian khóa, xóa khỏi cache
            lockoutCache.remove(key);
            return false;
        }

        return true; // Vẫn đang trong thời gian khóa
    }
}
