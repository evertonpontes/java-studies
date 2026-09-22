package com.schoolmanager.service;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Teacher;
import com.schoolmanager.repository.TeacherRepository;

public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public void update(Teacher teacher) {
        teacherRepository.update(teacher);
    }

    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }
}
