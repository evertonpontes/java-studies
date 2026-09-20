package com.schoolmanager.model;

import java.time.LocalDateTime;

public class Classroom {
    private Long id;
    private String name;
    private Long teacherId;
    private Long subjectId;
    private LocalDateTime createdAt;

    public Classroom() {
    }

    public Classroom(String name, Long teacherId, Long subjectId) {
        this.name = name;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public Classroom(Long id, String name, Long teacherId, Long subjectId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
