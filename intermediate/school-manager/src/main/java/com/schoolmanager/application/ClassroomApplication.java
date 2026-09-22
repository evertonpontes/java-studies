package com.schoolmanager.application;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Classroom;
import com.schoolmanager.service.ClassroomService;

public class ClassroomApplication {

    private final ClassroomService classroomService;

    public ClassroomApplication(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public Classroom create(Classroom classroom) {
        return classroomService.save(classroom);
    }

    public Optional<Classroom> findById(Long id) {
        return classroomService.findById(id);
    }

    public List<Classroom> findAll() {
        return classroomService.findAll();
    }

    public void update(Classroom classroom) {
        classroomService.update(classroom);
    }

    public void delete(Long id) {
        classroomService.deleteById(id);
    }
}
