package com.schoolmanager.service;

import java.util.List;
import java.util.Optional;

import com.schoolmanager.model.Subject;
import com.schoolmanager.repository.SubjectRepository;

public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public void update(Subject subject) {
        subjectRepository.update(subject);
    }

    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }
}
