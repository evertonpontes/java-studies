package com.schoolmanager.config;

import com.schoolmanager.application.StudentApplication;
import com.schoolmanager.repository.StudentRepository;
import com.schoolmanager.repository.StudentRepositoryImpl;
import com.schoolmanager.service.StudentService;

public class ApplicationConfig {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final StudentApplication studentApplication;

    public ApplicationConfig() {
        studentRepository = new StudentRepositoryImpl();

        studentService = new StudentService(
                studentRepository
        );

        studentApplication = new StudentApplication(
                studentService
        );
    }

    public StudentApplication studentApplication() {
        return studentApplication;
    }
}
