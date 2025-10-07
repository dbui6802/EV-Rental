package com.webserver.evrentalsystem.model.dto.response;

import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private UserDto userInfo;
    private String accessToken;
    private String refreshToken;
}
