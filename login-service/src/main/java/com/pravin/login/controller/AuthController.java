package com.pravin.login.controller;

import com.pravin.login.dto.AuthResponse;
import com.pravin.login.dto.LoginRequest;
import com.pravin.login.dto.RegisterRequest;
import com.pravin.login.entity.User;
import com.pravin.login.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register API
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        return userService.registerUser(user);
    }

    // Login API
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
