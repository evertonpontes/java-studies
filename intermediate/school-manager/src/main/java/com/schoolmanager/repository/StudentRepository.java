package com.schoolmanager.repository;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Student;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findById(Long id);

    List<Student> findAll();

    void update(Student student);

    void deleteById(Long id);
}
