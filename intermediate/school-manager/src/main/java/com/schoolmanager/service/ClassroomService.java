package com.schoolmanager.service;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Classroom;
import com.schoolmanager.repository.ClassroomRepository;

public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Classroom save(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Optional<Classroom> findById(Long id) {
        return classroomRepository.findById(id);
    }

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public void update(Classroom classroom) {
        classroomRepository.update(classroom);
    }

    public void deleteById(Long id) {
        classroomRepository.deleteById(id);
    }
}
