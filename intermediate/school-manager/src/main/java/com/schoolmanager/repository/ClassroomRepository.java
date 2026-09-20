package com.schoolmanager.repository;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Classroom;

public interface ClassroomRepository {

    Classroom save(Classroom classroom);

    Optional<Classroom> findById(Long id);

    List<Classroom> findAll();

    void update(Classroom classroom);

    void deleteById(Long id);
}
