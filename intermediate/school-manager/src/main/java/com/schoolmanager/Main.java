package com.schoolmanager;

import java.time.LocalDate;

import com.schoolmanager.model.Student;
import com.schoolmanager.repository.StudentRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        
        StudentRepositoryImpl studentRepository = new StudentRepositoryImpl();

        Student student = new Student(
            "Student to delete",
            "delete@example.com",
            LocalDate.of(2000, 1, 1)
        );

        Student savedStudent = studentRepository.save(student);

        System.out.println(
            "Created student with ID: "
                + savedStudent.getId()
        );

        studentRepository.deleteById(savedStudent.getId());

        System.out.println("Student deleted successfully!");

        boolean exists = studentRepository
            .findById(savedStudent.getId())
            .isPresent();

        System.out.println("Student exists: " + exists);
    }
}