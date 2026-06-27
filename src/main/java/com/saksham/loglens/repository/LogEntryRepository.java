package com.saksham.loglens.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.loglens.model.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long>{
    List<LogEntry> findByLogBatch_BatchIdAndLevel(Long batchId, String level);
}
