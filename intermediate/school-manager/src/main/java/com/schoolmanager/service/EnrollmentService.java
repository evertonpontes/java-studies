package com.schoolmanager.service;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Enrollment;
import com.schoolmanager.repository.ClassroomRepository;
import com.schoolmanager.repository.EnrollmentRepository;
import com.schoolmanager.repository.StudentRepository;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            ClassroomRepository classroomRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    public Enrollment save(Enrollment enrollment) {

        if (studentRepository.findById(enrollment.getStudentId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Student not found: " + enrollment.getStudentId()
            );
        }

        if (classroomRepository.findById(enrollment.getClassroomId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Classroom not found: " + enrollment.getClassroomId()
            );
        }

        return enrollmentRepository.save(enrollment);
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public void update(Enrollment enrollment) {
        enrollmentRepository.update(enrollment);
    }

    public void deleteById(Long id) {
        enrollmentRepository.deleteById(id);
    }
}
