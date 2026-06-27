package com.saksham.loglens.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saksham.loglens.dto.LogBatchResponse;
import com.saksham.loglens.model.LogBatch;
import com.saksham.loglens.model.LogEntry;
import com.saksham.loglens.model.LogUser;
import com.saksham.loglens.repository.LogBatchRepository;
import com.saksham.loglens.repository.LogEntryRepository;
import com.saksham.loglens.repository.LogUserRepository;
import com.saksham.loglens.util.LogParser;

@Service
public class LogService {

    private final LogBatchRepository logBatchRepository;
    private final LogEntryRepository logEntryRepository;
    private final LogUserRepository logUserRepository;

    LogService(LogEntryRepository logEntryRepository, LogUserRepository logUserRepository, LogBatchRepository logBatchRepository) {
        this.logEntryRepository = logEntryRepository;
        this.logUserRepository = logUserRepository;
        this.logBatchRepository = logBatchRepository;
    }

    public LogBatchResponse processLogFile(MultipartFile file, String email) throws IOException {
        LogUser user = logUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found!"));
        
        LogBatch logBatch = new LogBatch();
        logBatch.setFileName(file.getOriginalFilename());
        logBatch.setUploadTime(LocalDateTime.now());
        logBatch.setLogUser(user);

        logBatch = logBatchRepository.save(logBatch);
        
        List<LogEntry> entries = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while((line = reader.readLine()) != null) {
                LogEntry logEntry = LogParser.parser(line);
                if(logEntry == null) {
                    System.out.println("PARSER FAILED FOR LINE: " + line); // Ye tujhe bata dega fail kaha hua
                } else {
                    // ... rest of the code
                }
                if(logEntry != null) {
                    logEntry.setLogBatch(logBatch);
                    entries.add(logEntry);
                }
            }
            
        } catch (IOException e) {
            throw new IOException("File is not valid!");
        }

        logEntryRepository.saveAll(entries);
        return new LogBatchResponse(logBatch.getBatchId(), logBatch.getFileName(), Integer.toString(entries.size()), "Success");
    }
}
