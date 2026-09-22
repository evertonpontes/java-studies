package com.schoolmanager.application;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Enrollment;
import com.schoolmanager.service.EnrollmentService;

public class EnrollmentApplication {

    private final EnrollmentService enrollmentService;

    public EnrollmentApplication(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public Enrollment create(Enrollment enrollment) {
        return enrollmentService.save(enrollment);
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollmentService.findById(id);
    }

    public List<Enrollment> findAll() {
        return enrollmentService.findAll();
    }

    public void update(Enrollment enrollment) {
        enrollmentService.update(enrollment);
    }

    public void delete(Long id) {
        enrollmentService.deleteById(id);
    }
}
