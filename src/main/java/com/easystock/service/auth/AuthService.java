package com.easystock.service.auth;

import com.easystock.dto.auth.LoginRequestDto;
import com.easystock.dto.auth.UserDto;

public interface AuthService {

    UserDto login(LoginRequestDto loginRequest);
}