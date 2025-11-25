package com.easystock.service.auth;

import com.easystock.dto.auth.LoginRequestDto;
import com.easystock.dto.auth.UserDto;
import com.easystock.entity.User;
import com.easystock.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserDto login(LoginRequestDto loginRequest) {

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Invalid username or password"));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new EntityNotFoundException("Invalid username or password");
        }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}