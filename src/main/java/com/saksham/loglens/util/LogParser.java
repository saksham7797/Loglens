package com.saksham.loglens.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.saksham.loglens.model.LogEntry;

public class LogParser {
    
    // Sateek Regex jo Spring Boot logs ko accurately nichodega
    private static final Pattern PATTERN = Pattern.compile("^.*?\\s+(INFO|WARN|ERROR|DEBUG|TRACE)\\s+\\d+\\s+---\\s+\\[.*?\\]\\s+([^\\s:]+)\\s*:\\s+(.*)$");

    public static LogEntry parser(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            LogEntry entry = new LogEntry();
            entry.setLevel(matcher.group(1));
            entry.setClassName(matcher.group(2));
            entry.setMessage(matcher.group(3));
            return entry;
        }
        
        return null; 
    }
}