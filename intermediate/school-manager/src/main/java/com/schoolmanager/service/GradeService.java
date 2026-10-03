package com.schoolmanager.service;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Grade;
import com.schoolmanager.repository.EnrollmentRepository;
import com.schoolmanager.repository.GradeRepository;

public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeService(
            GradeRepository gradeRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.gradeRepository = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Grade save(Grade grade) {

        if (enrollmentRepository.findById(grade.getEnrollmentId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Enrollment not found: " + grade.getEnrollmentId()
            );
        }

        return gradeRepository.save(grade);
    }

    public Optional<Grade> findById(Long id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    public List<Grade> search(String field, String value) {
        return gradeRepository.search(field, value);
    }

    public void update(Grade grade) {

        if (enrollmentRepository.findById(grade.getEnrollmentId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Enrollment not found: " + grade.getEnrollmentId()
            );
        }

        gradeRepository.update(grade);
    }

    public void deleteById(Long id) {
        gradeRepository.deleteById(id);
    }
}
