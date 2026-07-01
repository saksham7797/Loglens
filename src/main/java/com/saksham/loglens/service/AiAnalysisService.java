package com.saksham.loglens.service;

import java.security.MessageDigest;
import java.time.Duration;
import java.util.List;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import com.saksham.loglens.dto.LogAnalysisResultResponse;
import com.saksham.loglens.model.LogAnalysisResult;
import com.saksham.loglens.model.LogEntry;
import com.saksham.loglens.repository.LogAnalysisRepository;
import com.saksham.loglens.repository.LogEntryRepository;

import io.github.bucket4j.Bucket;

@Service
public class AiAnalysisService {

    private final LogAnalysisRepository logAnalysisRepository;
    private final ChatModel chatModel;
    private final LogEntryRepository logEntryRepository;

    public AiAnalysisService(LogEntryRepository logEntryRepository, ChatModel chatModel, LogAnalysisRepository logAnalysisRepository) {
        this.chatModel = chatModel;
        this.logEntryRepository = logEntryRepository;
        this.logAnalysisRepository = logAnalysisRepository;
    }
    
    private static Bucket bucket = Bucket.builder().addLimit(limit -> limit.capacity(20).refillGreedy(10, Duration.ofMinutes(1))).build();

    public LogAnalysisResultResponse aiService(Long batchId) {
        if (bucket.tryConsume(1)) {
            List<LogEntry> errorLogs = logEntryRepository.findByLogBatch_BatchIdAndLevel(batchId, "ERROR");

            if(errorLogs.isEmpty()) {
                throw new RuntimeException("Logs Not Found!");
            }
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < errorLogs.size(); i++) {
                log.append(errorLogs.get(i).getClassName()).append(" ").append(errorLogs.get(i).getMessage()).append("\n");
            }

            String errorPatternHash;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                byte[] array = md.digest(log.toString().getBytes());

                StringBuilder sb = new StringBuilder();
                
                for (byte b : array) {
                    sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
                }
                
                errorPatternHash = sb.toString(); 
            } catch (java.security.NoSuchAlgorithmException e) {
                errorPatternHash = "HASH_ERROR";
            }

            String prompt = """
                You are LogLens, an elite, multi-language DevOps and Automated Log Analysis Engine. 
                Your task is to analyze the provided error log stream, which could belong to any programming language or framework (Java, Python, Node.js, C#, Go, etc.).

                Analyze the logs with 95%+ technical precision and strictly structure your response into the following blocks:

                1. [DETECTED ENVIRONMENT]: Identify the programming language/framework and the specific exception/error type.
                2. [ROOT CAUSE ANALYSIS]: Explain exactly why this failure occurred in plain, engineering-first terms. Break down the failure point.
                3. [EXPLANATION]: Clear, concise details of what went wrong in the architecture or code logic.
                4. [FIXING STEPS]: Step-by-step resolution guide to fix the underlying issue.
                5. [CODE / CONFIG FIX]: Provide the precise, corrected code snippet or configuration block. Keep it minimal and highly accurate.

                CRITICAL INSTRUCTIONS:
                - DO NOT output your internal thinking process or any chain-of-thought blocks. Output ONLY the final structured response starting directly with [DETECTED ENVIRONMENT].
                - The log stream contains MULTIPLE DISTINCT ERRORS. You MUST analyze and provide fixing steps for ALL of them. Do not stop at the first error you find.
                - Ignore dynamic data noise like unique PIDs, timestamps, or memory addresses when diagnosing the root cause.
                - If the log contains multiple instances of the same error pattern, consolidate them into a single comprehensive solution.
                - Keep the tone professional, objective, and direct. No conversational filler.
                - CRITICAL: You MUST return a complete, non-empty response. Under no circumstances should you return an empty string.

                Error Log Stream to Analyze:
            """ + log.toString();   
            
            String response = "";
            try {
                response = chatModel.call(prompt);
                
                // SANITIZATION: Remove reasoning blocks like <think>...</think> if the model generates them
                if (response != null && response.contains("</think>")) {
                    response = response.substring(response.indexOf("</think>") + 8).trim();
                }
                
            } catch (Exception e) {
                System.err.println("AI Error: " + e.getMessage());
                response = "AI Engine is temporarily down or blocked the request due to limits/safety. Reason: " + e.getMessage();
            }

            if (response == null || response.trim().isEmpty()) {
                response = "AI API ne empty response bheja h. Try modifying the prompt or check safety settings.";
            }

            LogAnalysisResult result = new LogAnalysisResult();
            result.setBatchId(batchId);
            result.setErrorPatternHash(errorPatternHash);
            result.setAiExplanation(response);
            result.setFixExplanation("Follow Steps given above!");
            logAnalysisRepository.save(result);
            return new LogAnalysisResultResponse(errorPatternHash, response, "Follow Steps given above!");
        } else {
            throw new RuntimeException("Too many requests. Please wait a minute.");
        }
    }       
}