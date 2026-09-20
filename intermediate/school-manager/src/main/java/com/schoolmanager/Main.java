package com.schoolmanager;

import java.util.List;

import com.schoolmanager.model.Enrollment;
import com.schoolmanager.repository.EnrollmentRepositoryImpl;

public class Main {

    public static void main(String[] args) {

        EnrollmentRepositoryImpl repository = new EnrollmentRepositoryImpl();

        Enrollment enrollment = new Enrollment(
                1L,
                1L
        );

        List<Enrollment> enrollments = repository.findAll();

        for (Enrollment s : enrollments) {
            System.out.println("--------------------------");
            System.out.println("Enrollment found: ");
            System.out.println("ID: " + s.getId());
            System.out.println("Enrollment found: ");
            System.out.println("ID: " + s.getId());
            System.out.println("STUDENT_ID: " + s.getStudentId());
            System.out.println("CLASSROOM_ID: " + s.getClassroomId());
            System.out.println("ENROLLED_AT: " + s.getEnrolledAt());
        }
    }
}
