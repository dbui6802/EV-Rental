package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapping {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        return userDto;
    }
}
