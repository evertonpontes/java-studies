package com.schoolmanager.application;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Student;
import com.schoolmanager.service.StudentService;

public class StudentApplication {

    private final StudentService studentService;

    public StudentApplication(StudentService studentService) {
        this.studentService = studentService;
    }

    public Student create(Student student) {
        return studentService.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentService.findById(id);
    }

    public List<Student> findAll() {
        return studentService.findAll();
    }

    public void update(Student student) {
        studentService.update(student);
    }

    public void delete(Long id) {
        studentService.deleteById(id);
    }
}
