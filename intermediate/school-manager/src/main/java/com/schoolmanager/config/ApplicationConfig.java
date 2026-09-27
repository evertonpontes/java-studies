package com.schoolmanager.config;

import com.schoolmanager.application.StudentApplication;
import com.schoolmanager.application.SubjectApplication;
import com.schoolmanager.application.TeacherApplication;
import com.schoolmanager.repository.StudentRepository;
import com.schoolmanager.repository.StudentRepositoryImpl;
import com.schoolmanager.repository.SubjectRepository;
import com.schoolmanager.repository.SubjectRepositoryImpl;
import com.schoolmanager.repository.TeacherRepository;
import com.schoolmanager.repository.TeacherRepositoryImpl;
import com.schoolmanager.service.StudentService;
import com.schoolmanager.service.SubjectService;
import com.schoolmanager.service.TeacherService;

public class ApplicationConfig {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final StudentApplication studentApplication;

    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final TeacherApplication teacherApplication;

    private final SubjectRepository subjectRepository;
    private final SubjectService subjectService;
    private final SubjectApplication subjectApplication;

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

        subjectRepository = new SubjectRepositoryImpl();

        subjectService = new SubjectService(
                subjectRepository
        );

        subjectApplication = new SubjectApplication(
                subjectService
        );
    }

    public StudentApplication studentApplication() {
        return studentApplication;
    }

    public TeacherApplication teacherApplication() {
        return teacherApplication;
    }

    public SubjectApplication subjectApplication() {
        return subjectApplication;
    }
}
