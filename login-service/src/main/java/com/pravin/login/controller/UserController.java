package com.pravin.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/users/profile")
    public String profile() {
        return "Welcome Pravin! JWT Authentication Successful.";
    }
}
