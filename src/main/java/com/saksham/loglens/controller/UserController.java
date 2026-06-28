package com.saksham.loglens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saksham.loglens.service.LogUserService;

@RestController
@RequestMapping("/loglens/users")
public class UserController {

    private final LogUserService logUserService;

    public UserController(LogUserService logUserService) {
        this.logUserService = logUserService;
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestParam String newName, Authentication authentication) {
        String email = authentication.getName(); 
        logUserService.updateAccount(email, newName);
        return ResponseEntity.ok("Profile updated successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfile(Authentication authentication) {
        String email = authentication.getName(); 
        logUserService.deleteAccount(email);
        return ResponseEntity.ok("Account deleted permanently!");
    }
}