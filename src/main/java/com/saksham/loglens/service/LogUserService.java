package com.saksham.loglens.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.saksham.loglens.model.LogUser;
import com.saksham.loglens.repository.LogAnalysisRepository;
import com.saksham.loglens.repository.LogBatchRepository;
import com.saksham.loglens.repository.LogEntryRepository;
import com.saksham.loglens.repository.LogSummaryRepository;
import com.saksham.loglens.repository.LogUserRepository;

@Service
public class LogUserService {

    private final LogUserRepository logUserRepository;
    private final LogBatchRepository logBatchRepository;
    private final LogEntryRepository logEntryRepository;
    private final LogSummaryRepository logSummaryRepository;
    private final LogAnalysisRepository logAnalysisRepository;

    public LogUserService(LogUserRepository logUserRepository,
                           LogBatchRepository logBatchRepository,
                           LogEntryRepository logEntryRepository,
                           LogSummaryRepository logSummaryRepository,
                           LogAnalysisRepository logAnalysisRepository) {
        this.logUserRepository = logUserRepository;
        this.logBatchRepository = logBatchRepository;
        this.logEntryRepository = logEntryRepository;
        this.logSummaryRepository = logSummaryRepository;
        this.logAnalysisRepository = logAnalysisRepository;
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

        Long userId = user.getUserId();
        logEntryRepository.deleteAllByUserId(userId);
        logSummaryRepository.deleteAllByUserId(userId);
        logAnalysisRepository.deleteAllByUserId(userId);
        logBatchRepository.deleteAllByUserId(userId);
        logUserRepository.delete(user);
    }
}