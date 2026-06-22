package com.saksham.loglens.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="log_batch")
public class LogBatch {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long batchId;
    
    private String fileName;
    private LocalDateTime uploadTime;
    private String status;

    @ManyToOne()
    @JoinColumn(name="user_Id")
    private LogUser logUser;
    
    @OneToMany(mappedBy="logBatch")
    private List<LogEntry> logEntries;
    
    @OneToMany(mappedBy="logBatch")
    private List<LogSummary> logSummarys;

}