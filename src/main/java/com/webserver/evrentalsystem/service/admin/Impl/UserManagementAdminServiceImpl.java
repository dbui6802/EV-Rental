package com.webserver.evrentalsystem.service.admin.impl;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.entity.RentalStatus;
import com.webserver.evrentalsystem.exception.ConflictException;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.NotFoundException;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import com.webserver.evrentalsystem.model.dto.request.CreateUserRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateUserRequest;
import com.webserver.evrentalsystem.model.mapping.DocumentMapper;
import com.webserver.evrentalsystem.model.mapping.UserMapper;
import com.webserver.evrentalsystem.repository.*;
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

    @Autowired
    private StaffStationRepository staffStationRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        userValidation.validateAdmin();
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã tồn tại");
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email đã tồn tại");
            }
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
            if (!request.getEmail().equalsIgnoreCase(user.getEmail())
                    && userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email đã tồn tại");
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
    public void toggleUserStatus(Long id) {
        userValidation.validateAdmin();
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với id = " + id));

        User adminUser = userValidation.validateAdmin();
        if (adminUser.getId().equals(id)) {
            throw new InvalidateParamsException("Không thể tự vô hiệu hóa chính mình");
        }

        // If we are deactivating the user, check for conflicts.
        if (targetUser.getIsActive()) {
            boolean isStaffAssigned = staffStationRepository.findByStaffIdAndIsActiveTrue(id) != null;
            if (isStaffAssigned) {
                throw new ConflictException("Không thể vô hiệu hóa nhân viên đang được phân công cho một trạm.");
            }

            boolean hasActiveRental = rentalRepository.existsByRenterIdAndStatusNotIn(
                    targetUser.getId(),
                    List.of(RentalStatus.RETURNED, RentalStatus.CANCELLED)
            );

            if (hasActiveRental) {
                throw new InvalidateParamsException("Người dùng đang có đơn thuê xe chưa kết thúc, không thể vô hiệu hóa");
            }

            boolean hasActiveReservation = reservationRepository.existsByRenterIdAndStatusNotIn(
                    targetUser.getId(),
                    List.of(ReservationStatus.CANCELLED, ReservationStatus.EXPIRED)
            );

            if (hasActiveReservation) {
                throw new InvalidateParamsException("Người dùng đang có đặt chỗ chưa kết thúc, không thể vô hiệu hóa");
            }
        }

        // Toggle the isActive status
        targetUser.setIsActive(!targetUser.getIsActive());
        targetUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(targetUser);
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
