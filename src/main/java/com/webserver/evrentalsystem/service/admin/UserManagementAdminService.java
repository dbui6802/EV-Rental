package com.webserver.evrentalsystem.service.admin;

import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import com.webserver.evrentalsystem.model.dto.request.CreateUserRequest;
import com.webserver.evrentalsystem.model.dto.request.UpdateUserRequest;

import java.util.List;

public interface UserManagementAdminService {

    UserDto createUser(CreateUserRequest request);

    List<UserDto> getAllUsers(String role, String phone);

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);

    List<DocumentDto> getRenterDocument(Long renterId);
}
