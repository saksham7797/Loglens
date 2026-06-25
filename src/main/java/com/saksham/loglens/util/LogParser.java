package com.saksham.loglens.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.saksham.loglens.model.LogEntry;

public class LogParser {
    
    private static final Pattern LOG_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+\\[[^\\]]+\\]\\s+([A-Z]+)\\s+([\\w\\.]+)\\s+-\\s+(.*)$");
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static LogEntry parser(String rawLog) {
        if(rawLog == null || rawLog.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = LOG_PATTERN.matcher(rawLog);
        if(matcher.matches()) {
            LogEntry entry = new LogEntry();
            entry.setTimeStamp(LocalDateTime.parse(matcher.group(1), FORMATTER));
            entry.setLevel(matcher.group(2));
            entry.setClassName(matcher.group(3));
            entry.setMessage(matcher.group(4));
            return entry;
        }

        return null;
    }
}
