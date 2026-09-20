package com.schoolmanager.model;

import java.time.LocalDateTime;

public class Enrollment {

    private Long id;
    private Long studentId;
    private Long classroomId;
    private LocalDateTime enrolledAt;

    public Enrollment() {
    }

    public Enrollment(Long studentId, Long classroomId) {
        this.studentId = studentId;
        this.classroomId = classroomId;
    }

    public Enrollment(Long id, Long studentId, Long classroomId, LocalDateTime enrolledAt) {
        this.id = id;
        this.studentId = studentId;
        this.classroomId = classroomId;
        this.enrolledAt = enrolledAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
