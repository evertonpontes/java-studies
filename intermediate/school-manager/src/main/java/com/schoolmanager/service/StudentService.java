package com.schoolmanager.service;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Student;
import com.schoolmanager.repository.StudentRepository;

public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void update(Student student) {
        studentRepository.update(student);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
