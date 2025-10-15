package com.webserver.evrentalsystem;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class EVRentalSystemWebServerApplication extends SpringBootServletInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${ADMIN_PHONE_NUMBER}")
    private String adminPhoneNumber;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EVRentalSystemWebServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(EVRentalSystemWebServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        setupAdminUser();
    }

    private void setupAdminUser() {
        List<User> adminUsers = userRepository.findAllByRole(Role.ADMIN);

        boolean shouldCreateNewAdmin = true;

        for (User adminUser : adminUsers) {
            if (adminUser.getPhone().equals(adminPhoneNumber)) {
                shouldCreateNewAdmin = false;
                break;
            }
        }

        if (shouldCreateNewAdmin) {
            User adminUser = new User();
            adminUser.setFullName("Admin");
            adminUser.setPhone(adminPhoneNumber);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole(Role.ADMIN);
            adminUser.setCreatedAt(java.time.LocalDateTime.now());
            userRepository.save(adminUser);
            Logger.printf("Tạo tài khoản admin " + adminUser.getFullName() + " thành công!");
        }
    }
}
