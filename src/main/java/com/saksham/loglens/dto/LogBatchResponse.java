package com.saksham.loglens.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LogBatchResponse {
    private Long batchId;
    private String fileName;
    private String totalRecordsPassed;
    private String status;
}
