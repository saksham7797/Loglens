package com.saksham.loglens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saksham.loglens.dto.AuthResponse;
import com.saksham.loglens.dto.LoginRequest;
import com.saksham.loglens.dto.RegisterRequest;
import com.saksham.loglens.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loglens/auth")
public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register") 
    public ResponseEntity<String> userRegistration(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.userRegistration(registerRequest);
        return ResponseEntity.ok("Registration Successful!\nProceed to Login....");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.userLogin(loginRequest));
    }

}
