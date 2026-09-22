package com.schoolmanager.application;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Grade;
import com.schoolmanager.service.GradeService;

public class GradeApplication {

    private final GradeService gradeService;

    public GradeApplication(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public Grade create(Grade grade) {
        return gradeService.save(grade);
    }

    public Optional<Grade> findById(Long id) {
        return gradeService.findById(id);
    }

    public List<Grade> findAll() {
        return gradeService.findAll();
    }

    public void update(Grade grade) {
        gradeService.update(grade);
    }

    public void delete(Long id) {
        gradeService.deleteById(id);
    }
}
