package com.schoolmanager.application;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Teacher;
import com.schoolmanager.service.TeacherService;

public class TeacherApplication {

    private final TeacherService teacherService;

    public TeacherApplication(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    public Teacher create(Teacher teacher) {
        return teacherService.save(teacher);
    }

    public Optional<Teacher> findById(Long id) {
        return teacherService.findById(id);
    }

    public List<Teacher> findAll() {
        return teacherService.findAll();
    }

    public void update(Teacher teacher) {
        teacherService.update(teacher);
    }

    public void delete(Long id) {
        teacherService.deleteById(id);
    }
}
