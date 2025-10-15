package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import com.webserver.evrentalsystem.model.dto.request.CreateUserRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateUserRequest;
import com.webserver.evrentalsystem.model.mapping.DocumentMapper;
import com.webserver.evrentalsystem.model.mapping.UserMapper;
import com.webserver.evrentalsystem.repository.DocumentRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.admin.UserManagementAdminService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserManagementAdminServiceImpl implements UserManagementAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        userValidation.validateAdmin();
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã tồn tại");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.fromValue(request.getRole()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers(String role, String phone) {
        userValidation.validateAdmin();
        return userRepository.findAll().stream()
                .filter(u -> (role == null || u.getRole().getValue().equalsIgnoreCase(role)))
                .filter(u -> (phone == null || u.getPhone().contains(phone)))
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        userValidation.validateAdmin();
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với id = " + id));
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        userValidation.validateAdmin();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với id = " + id));

        if (request.getFullName() != null) {
            if (request.getFullName().isBlank()) {
                throw new InvalidateParamsException("Tên không được để trống");
            }
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            if (request.getEmail().isBlank()) {
                throw new InvalidateParamsException("Email không được để trống");
            }
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new InvalidateParamsException("Email không hợp lệ");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            if (request.getPhone().isBlank()) {
                throw new InvalidateParamsException("Số điện thoại không được để trống");
            }
            if (!request.getPhone().matches("^(\\+84|0)\\d{9}$")) {
                throw new InvalidateParamsException("Số điện thoại không hợp lệ");
            }
            if (!request.getPhone().equals(user.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
                throw new InvalidateParamsException("Số điện thoại đã tồn tại");
            }
            user.setPhone(request.getPhone());
        }
        if (request.getRole() != null) {
            Role role = Role.fromValue(request.getRole());
            if (role == null || role == Role.RENTER) {
                throw new InvalidateParamsException("Vai trò phải là 'staff' hoặc 'admin'");
            }
            user.setRole(role);
        }
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userValidation.validateAdmin();
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy user với id = " + id);
        }
        User user = userValidation.validateAdmin();
        if (user.getId().equals(id)) {
            throw new InvalidateParamsException("Không thể xóa chính mình");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<DocumentDto> getRenterDocument(Long renterId) {
        userValidation.validateAdmin();
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với id = " + renterId));
        if (renter.getRole() != Role.RENTER) {
            throw new InvalidateParamsException("User không phải là renter");
        }
        return documentRepository.findByUserId(renterId).stream()
                .map(documentMapper::toDocumentDto)
                .collect(Collectors.toList());
    }
}
