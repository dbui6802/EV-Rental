package com.webserver.evrentalsystem.service.auth;

public interface OtpAttemptService {
    void recordFailedAttempt(String key);

    void resetAttempts(String key);

    void checkLockout(String key);
}
