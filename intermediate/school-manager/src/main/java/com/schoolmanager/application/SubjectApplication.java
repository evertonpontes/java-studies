package com.schoolmanager.application;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Subject;
import com.schoolmanager.service.SubjectService;

public class SubjectApplication {

    private final SubjectService subjectService;

    public SubjectApplication(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public Subject create(Subject subject) {
        return subjectService.save(subject);
    }

    public Optional<Subject> findById(Long id) {
        return subjectService.findById(id);
    }

    public List<Subject> findAll() {
        return subjectService.findAll();
    }

    public void update(Subject subject) {
        subjectService.update(subject);
    }

    public void delete(Long id) {
        subjectService.deleteById(id);
    }
}
