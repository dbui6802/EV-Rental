package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.Role;
import jakarta.persistence.AttributeConverter;

public class RoleJpaConverter implements AttributeConverter<Role, String> {

        @Override
        public String convertToDatabaseColumn(Role role) {
            if (role == null) {
                return null;
            }
            return role.getValue();
        }

        @Override
        public Role convertToEntityAttribute(String s) {
            if (s == null) {
                return null;
            }
            return Role.fromValue(s);
        }
}
