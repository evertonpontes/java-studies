package com.schoolmanager.config;

import com.schoolmanager.application.StudentApplication;
import com.schoolmanager.application.TeacherApplication;
import com.schoolmanager.repository.StudentRepository;
import com.schoolmanager.repository.StudentRepositoryImpl;
import com.schoolmanager.repository.TeacherRepository;
import com.schoolmanager.repository.TeacherRepositoryImpl;
import com.schoolmanager.service.StudentService;
import com.schoolmanager.service.TeacherService;

public class ApplicationConfig {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final StudentApplication studentApplication;

    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final TeacherApplication teacherApplication;

    public ApplicationConfig() {
        studentRepository = new StudentRepositoryImpl();

        studentService = new StudentService(
                studentRepository
        );

        studentApplication = new StudentApplication(
                studentService
        );

        teacherRepository = new TeacherRepositoryImpl();

        teacherService = new TeacherService(
                teacherRepository
        );

        teacherApplication = new TeacherApplication(
                teacherService
        );
    }

    public StudentApplication studentApplication() {
        return studentApplication;
    }

    public TeacherApplication teacherApplication() {
        return teacherApplication;
    }
}
