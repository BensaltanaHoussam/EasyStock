package com.easystock.controller.auth;

import com.easystock.config.auth.Auth;
import com.easystock.dto.auth.LoginRequestDto;
import com.easystock.dto.auth.UserDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequestDto loginRequest, HttpServletRequest request) {

        UserDto userDto = authService.login(loginRequest);
        HttpSession session = request.getSession(true);
        session.setAttribute("user", userDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");

        if (userDto == null) {
            return ResponseEntity.status(401).body(null); // 401 Unauthorized
        }

        return ResponseEntity.ok(userDto);
    }


    @GetMapping("/admin-only")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("This is a secret message for ADMINS only!");
    }

    @GetMapping("/any-user")
    @Auth
    public ResponseEntity<String> getAnyUserData() {
        return ResponseEntity.ok("This is a message for any logged-in user.");
    }
}