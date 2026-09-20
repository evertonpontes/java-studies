package com.schoolmanager.repository;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Subject;

public interface SubjectRepository {

    Subject save(Subject subject);

    Optional<Subject> findById(Long id);

    List<Subject> findAll();

    void update(Subject subject);

    void deleteById(Long id);
}
