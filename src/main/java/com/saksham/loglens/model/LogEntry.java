package com.saksham.loglens.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="logs")
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    
    private LocalDateTime timeStamp;
    private String level;
    private String className;
    
    @Column(columnDefinition="TEXT")
    private String message;     

    @ManyToOne()
    @JoinColumn(name="batch_id")
    private LogBatch logBatch;
}
