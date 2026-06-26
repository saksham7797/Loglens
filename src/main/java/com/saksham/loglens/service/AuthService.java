package com.saksham.loglens.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saksham.loglens.dto.AuthResponse;
import com.saksham.loglens.dto.LoginRequest;
import com.saksham.loglens.dto.RegisterRequest;
import com.saksham.loglens.model.LogUser;
import com.saksham.loglens.util.JwtUtil;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final LogUserService logUserService; 
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    AuthService(LogUserService logUserService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.logUserService = logUserService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String userRegistration(RegisterRequest registerRequest) {
        if(logUserService.existsByEmail(registerRequest.getEmail())) throw new RuntimeException("User Already Exists!"); 

        LogUser newLogUser = new LogUser();
        newLogUser.setName(registerRequest.getName());
        newLogUser.setEmail(registerRequest.getEmail());
        newLogUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newLogUser.setRole("USER");
        logUserService.saveUser(newLogUser);

        return "User registered Successfully";
    }

    public AuthResponse userLogin(LoginRequest loginRequest) {        
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        LogUser user = logUserService.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.tokenGenerator(loginRequest.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }   
}