package com.easystock.dto.auth;

import com.easystock.entity.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private UserRole role;
}