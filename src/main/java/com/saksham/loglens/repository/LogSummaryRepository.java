package com.saksham.loglens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.saksham.loglens.model.LogSummary;

public interface LogSummaryRepository extends JpaRepository<LogSummary, Long>{
    @Modifying
    @Transactional
    @Query("DELETE FROM LogSummary ls WHERE ls.logBatch.logUser.userId = :userId")
    void deleteAllByUserId(Long userId);
}
