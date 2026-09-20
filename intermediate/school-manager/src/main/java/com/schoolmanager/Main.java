package com.schoolmanager;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Classroom;
import com.schoolmanager.repository.ClassroomRepositoryImpl;

public class Main {

    public static void main(String[] args) {

        ClassroomRepositoryImpl repository = new ClassroomRepositoryImpl();

        Classroom classroom = new Classroom(
                "A",
                1L,
                3L
        );

        Classroom savedClassroom = repository.save(classroom);

        System.out.println("Classroom saved successfully: " + savedClassroom.getId());

        repository.findById(savedClassroom.getId())
                .ifPresent(value -> {
                    System.out.println("Classroom found: ");
                    System.out.println("ID: " + value.getId());
                    System.out.println("NAME: " + value.getName());
                    System.out.println("TEACHER_ID: " + value.getTeacherId());
                    System.out.println("SUBJECT_ID: " + value.getSubjectId());
                    System.out.println("CREATED_AT: " + value.getCreatedAt());
                });

        Classroom classroom2 = new Classroom(
                "B",
                1L,
                3L
        );

        Classroom savedClassroom2 = repository.save(classroom2);

        Classroom classroom3 = new Classroom(
                "C",
                1L,
                3L
        );

        Classroom savedClassroom3 = repository.save(classroom3);

        List<Classroom> Classrooms = repository.findAll();

        for (Classroom s : Classrooms) {
            System.out.println("--------------------------");
            System.out.println("Classroom found: ");
            System.out.println("ID: " + s.getId());
            System.out.println("NAME: " + s.getName());
            System.out.println("TEACHER_ID: " + s.getTeacherId());
            System.out.println("SUBJECT_ID: " + s.getSubjectId());
            System.out.println("CREATED_AT: " + s.getCreatedAt());
        }

        savedClassroom.setName("A (Updated)");
        savedClassroom.setTeacherId(2L);

        repository.update(savedClassroom);

        repository.findById(savedClassroom.getId())
                .ifPresent(value -> {
                    System.out.println("Classroom found: ");
                    System.out.println("ID: " + value.getId());
                    System.out.println("NAME: " + value.getName());
                    System.out.println("TEACHER_ID: " + value.getTeacherId());
                    System.out.println("SUBJECT_ID: " + value.getSubjectId());
                    System.out.println("CREATED_AT: " + value.getCreatedAt());
                });

        repository.deleteById(savedClassroom3.getId());

        Optional<Classroom> deletedClassroom3 = repository.findById(savedClassroom3.getId());

        System.out.println("Classroom deleted exists: " + deletedClassroom3.isPresent());

    }
}
