package com.schoolmanager;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Subject;
import com.schoolmanager.repository.SubjectRepositoryImpl;

public class Main {

    public static void main(String[] args) {

        SubjectRepositoryImpl repository = new SubjectRepositoryImpl();

        Subject subject = new Subject("Mathematics", null);

        Subject savedSubject = repository.save(subject);

        System.out.println("Subject saved successfully: " + savedSubject.getId());

        repository.findById(savedSubject.getId())
                .ifPresent(value -> {
                    System.out.println("Subject found: ");
                    System.out.println("ID: " + value.getId());
                    System.out.println("NAME: " + value.getName());
                    System.out.println("DESCRIPTION: " + value.getDescription());
                    System.out.println("CREATED_AT: " + value.getCreatedAt());
                });

        List<Subject> subjects = repository.findAll();

        for (Subject s : subjects) {
            System.out.println("--------------------------");
            System.out.println("ID: " + s.getId());
            System.out.println("NAME: " + s.getName());
            System.out.println("DESCRIPTION: " + s.getDescription());
            System.out.println("CREATED_AT: " + s.getCreatedAt());
        }

        repository.update(savedSubject);

        repository.findById(savedSubject.getId())
                .ifPresent(value -> {
                    System.out.println("Subject updated found: ");
                    System.out.println("ID: " + value.getId());
                    System.out.println("NAME: " + value.getName());
                    System.out.println("DESCRIPTION: " + value.getDescription());
                    System.out.println("CREATED_AT: " + value.getCreatedAt());
                });

        repository.deleteById(savedSubject.getId());

        Optional<Subject> deletedSubject = repository.findById(savedSubject.getId());

        System.out.println("Subject deleted exists: " + deletedSubject.isPresent());

    }
}
