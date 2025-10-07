package com.webserver.evrentalsystem.service.validation;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {

    @Autowired
    private UserRepository userRepository;

    public User validateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            Logger.printf("Không thể xác thực được người dùng");
            throw new InvalidateParamsException("Không thể xác thực được người dùng");
        }

        String phone = (String) authentication.getPrincipal(); // principal chính là phone
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        Logger.printf("Phone: " + phone);
        Logger.printf("Role: " + role);

        if (phone == null || role == null) {
            throw new InvalidateParamsException("Không thể xác thực được người dùng");
        }

        User user = userRepository.findByPhone(phone);

        if (user == null) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        if (!role.equals(user.getRole().getValue())) {
            throw new PermissionDeniedException("Đăng nhập lại để fix lỗi này");
        }

        return user;
    }

    public User validateRenter() {
        User user = validateUser();
        if (user.getRole().equals(Role.RENTER)) {
            return user;
        }
        throw new PermissionDeniedException("Người dùng không có quyền thực hiện hành động này");
    }

    public User validateStaff() {
        User user = validateUser();
        if (user.getRole().equals(Role.STAFF)) {
            return user;
        }
        throw new PermissionDeniedException("Người dùng không có quyền thực hiện hành động này");
    }

    public User validateAdmin() {
        User user = validateUser();
        if (user.getRole().equals(Role.ADMIN)) {
            return user;
        }
        throw new PermissionDeniedException("Người dùng không có quyền thực hiện hành động này");
    }
}