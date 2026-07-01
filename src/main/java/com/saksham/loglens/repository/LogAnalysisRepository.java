package com.saksham.loglens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.saksham.loglens.model.LogAnalysisResult;

public interface LogAnalysisRepository extends JpaRepository<LogAnalysisResult, Long>{
    Optional<LogAnalysisResult> findByErrorPatternHash(String errorPatternHash);
    @Modifying
    @Transactional
    @Query("DELETE FROM LogAnalysisResult lar WHERE lar.batchId IN (SELECT lb.batchId FROM LogBatch lb WHERE lb.logUser.userId = :userId)")
    void deleteAllByUserId(Long userId);
}
