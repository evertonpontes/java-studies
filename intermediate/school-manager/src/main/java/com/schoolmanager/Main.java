package com.schoolmanager;

import java.util.Optional;

import com.schoolmanager.model.Teacher;
import com.schoolmanager.repository.TeacherRepository;
import com.schoolmanager.repository.TeacherRepositoryImpl;

public class Main {

    public static void main(String[] args) {

        TeacherRepository teacherRepository = new TeacherRepositoryImpl();

        teacherRepository.deleteById(3L);

        Optional<Teacher> teacher = teacherRepository.findById(3L);

        System.out.println("Teacher exists: " + teacher.isPresent());

    }
}
