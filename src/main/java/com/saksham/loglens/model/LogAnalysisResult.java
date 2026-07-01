    package com.saksham.loglens.model;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    @Entity
    @Table(name="analysis_result")
    public class LogAnalysisResult {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long id;

        private Long batchId;
        private String errorPatternHash;

        @Column(columnDefinition = "TEXT")
        private String aiExplanation;
        
        @Column(columnDefinition = "TEXT")
        private String fixExplanation;
    }
