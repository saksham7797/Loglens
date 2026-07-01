package com.saksham.loglens.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.saksham.loglens.model.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long>{
    @Modifying
    @Transactional
    @Query("DELETE FROM LogEntry le WHERE le.logBatch.logUser.userId = :userId")
    void deleteAllByUserId(Long userId);
    
    List<LogEntry> findByLogBatch_BatchIdAndLevel(Long batchId, String level);
}
