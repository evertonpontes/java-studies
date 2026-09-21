package com.schoolmanager.repository;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Grade;

public interface GradeRepository {

    Grade save(Grade grade);

    Optional<Grade> findById(Long id);

    List<Grade> findAll();

    void update(Grade grade);

    void deleteById(Long id);
}
