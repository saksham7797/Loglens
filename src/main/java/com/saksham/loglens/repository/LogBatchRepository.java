package com.saksham.loglens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.saksham.loglens.model.LogBatch;


public interface LogBatchRepository extends JpaRepository<LogBatch, Long>{
    @Modifying
    @Transactional
    @Query("DELETE FROM LogBatch lb WHERE lb.logUser.userId = :userId")
    void deleteAllByUserId(Long userId);
}