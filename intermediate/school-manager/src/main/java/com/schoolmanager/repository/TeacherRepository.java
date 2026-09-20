package com.schoolmanager.repository;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Teacher;

public interface TeacherRepository {
    Teacher save(Teacher teacher);

    Optional<Teacher> findById(Long id);

    List<Teacher> findAll();

    void update(Teacher teacher);

    void deleteById(Long id);
}
