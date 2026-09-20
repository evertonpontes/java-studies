package com.schoolmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Student {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private LocalDateTime createdAt;

    public Student() {
    }

    public Student(String name, String email, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public Student(Long id, String name, String email, LocalDate birthDate, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
