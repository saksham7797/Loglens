package com.saksham.loglens.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAnalysisResultResponse {
    private String errorPatternHash;
    private String aiExplanation;
    private String fixExplanation;
}
