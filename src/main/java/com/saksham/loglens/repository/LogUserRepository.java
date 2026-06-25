package com.saksham.loglens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.loglens.model.LogUser;


public interface LogUserRepository extends JpaRepository<LogUser, Long>{
    Optional<LogUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
