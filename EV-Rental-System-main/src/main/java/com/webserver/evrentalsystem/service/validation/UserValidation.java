package com.webserver.evrentalsystem.service.validation;

import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.utils.Constant;
import com.webserver.evrentalsystem.utils.Logger;
import jakarta.servlet.http.HttpServletRequest;

public class UserValidation {

    public static User validateUser(UserRepository userRepository, HttpServletRequest httpRequest) {

        String phone = (String) httpRequest.getAttribute(Constant.Attribute.PHONE);
        String role = (String) httpRequest.getAttribute(Constant.Attribute.ROLE);

        Logger.printf("Phone: " + phone);
        Logger.printf("Role: " + role);

        if (phone == null || role == null) {
            Logger.printf("Không thể xác thực được người dùng");
            throw new InvalidateParamsException("Không thể xác thực được người dùng");
        }

        User user = userRepository.findByPhone(phone);

        if (user == null) {
            Logger.printf("Không tìm thấy người dùng");
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }

        if (!role.equals(user.getRole().getValue())) {
            Logger.printf("Quyền không khớp");
            throw new PermissionDeniedException("Đăng nhập lại để fix lỗi này");
        }

        return user;
    }
}
