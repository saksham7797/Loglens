package com.saksham.loglens.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saksham.loglens.dto.LogAnalysisResultResponse;
import com.saksham.loglens.dto.LogBatchResponse;
import com.saksham.loglens.service.AiAnalysisService;
import com.saksham.loglens.service.LogService;


@RestController
@RequestMapping("/loglens/logs")
public class LogController {

    private final AiAnalysisService aiAnalysisService;
    private final LogService logService;

    public LogController(LogService logService, AiAnalysisService aiAnalysisService) {
        this.logService = logService;
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/upload")
    public ResponseEntity<LogBatchResponse> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return ResponseEntity.ok(logService.processLogFile(file, email));
        } catch (IOException e) {
            throw new IOException("File type not valid!");    
        }
    }

    @PostMapping("/analysis/{batchId}")
    public ResponseEntity<LogAnalysisResultResponse> aiAnalysis(@PathVariable Long batchId) {
        return ResponseEntity.ok(aiAnalysisService.aiService(batchId));
    }
}
