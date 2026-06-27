package com.saksham.loglens.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.saksham.loglens.model.LogUser;
import com.saksham.loglens.repository.LogUserRepository;

@Service
public class LogUserService {

    private final LogUserRepository logUserRepository;

    public LogUserService(LogUserRepository logUserRepository) {
        this.logUserRepository = logUserRepository;
    }
    
    public boolean existsByEmail(String email) {
        return logUserRepository.existsByEmail(email);
    }

    public LogUser saveUser(LogUser user) {
        return logUserRepository.save(user);
    }

    public Optional<LogUser> findByEmail(String email) {
        return logUserRepository.findByEmail(email);
    }

    public LogUser updateAccount(String email, String newName) {
        LogUser user = logUserRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(newName);
        return logUserRepository.save(user);
    }

    public void deleteAccount(String email) {
        LogUser user = logUserRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        logUserRepository.delete(user);
    }
}