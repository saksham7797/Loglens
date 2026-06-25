package com.saksham.loglens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.loglens.model.LogAnalysisResult;

public interface LogAnalysisRepository extends JpaRepository<LogAnalysisResult, Long>{
    Optional<LogAnalysisResult> findByErrorPatternHash(String errorPatternHash);
}
