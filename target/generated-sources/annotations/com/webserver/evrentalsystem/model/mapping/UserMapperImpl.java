package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T08:53:04+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setRole( userRoleValue( user ) );
        userDto.setId( user.getId() );
        userDto.setFullName( user.getFullName() );
        userDto.setEmail( user.getEmail() );
        userDto.setPhone( user.getPhone() );
        userDto.setCreatedAt( user.getCreatedAt() );
        userDto.setUpdatedAt( user.getUpdatedAt() );

        return userDto;
    }

    private String userRoleValue(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String value = role.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
