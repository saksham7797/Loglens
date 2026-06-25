package com.saksham.loglens.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.loglens.model.LogSummary;

public interface LogSummaryRepository extends JpaRepository<LogSummary, Long>{
    
}
