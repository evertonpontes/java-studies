package com.schoolmanager.repository;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Grade;

public interface GradeRepository {

    Grade save(Grade grade);

    Optional<Grade> findById(Long id);

    List<Grade> findAll();

    List<Grade> search(String field, String value);

    void update(Grade grade);

    void deleteById(Long id);
}
