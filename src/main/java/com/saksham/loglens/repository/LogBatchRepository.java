package com.saksham.loglens.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.loglens.model.LogBatch;


public interface LogBatchRepository extends JpaRepository<LogBatch, Long>{
    
}