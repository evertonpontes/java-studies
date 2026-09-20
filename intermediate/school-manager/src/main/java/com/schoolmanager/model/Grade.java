package com.schoolmanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Grade {

    private Long id;
    private Long enrollmentId;
    private BigDecimal value;
    private String assessment;
    private LocalDateTime createdAt;

    public Grade() {
    }

    public Grade(Long enrollmentId, BigDecimal value, String assessment) {
        this.enrollmentId = enrollmentId;
        this.value = value;
        this.assessment = assessment;
    }

    public Grade(Long id, Long enrollmentId, BigDecimal value, String assessment, LocalDateTime createdAt) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.value = value;
        this.assessment = assessment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getAssessment() {
        return assessment;
    }
    
    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
