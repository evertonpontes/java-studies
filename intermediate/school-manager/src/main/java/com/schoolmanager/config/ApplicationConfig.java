package com.schoolmanager.config;

import com.schoolmanager.application.ClassroomApplication;
import com.schoolmanager.application.EnrollmentApplication;
import com.schoolmanager.application.GradeApplication;
import com.schoolmanager.application.StudentApplication;
import com.schoolmanager.application.SubjectApplication;
import com.schoolmanager.application.TeacherApplication;
import com.schoolmanager.repository.ClassroomRepository;
import com.schoolmanager.repository.ClassroomRepositoryImpl;
import com.schoolmanager.repository.EnrollmentRepository;
import com.schoolmanager.repository.EnrollmentRepositoryImpl;
import com.schoolmanager.repository.GradeRepository;
import com.schoolmanager.repository.GradeRepositoryImpl;
import com.schoolmanager.repository.StudentRepository;
import com.schoolmanager.repository.StudentRepositoryImpl;
import com.schoolmanager.repository.SubjectRepository;
import com.schoolmanager.repository.SubjectRepositoryImpl;
import com.schoolmanager.repository.TeacherRepository;
import com.schoolmanager.repository.TeacherRepositoryImpl;
import com.schoolmanager.service.ClassroomService;
import com.schoolmanager.service.EnrollmentService;
import com.schoolmanager.service.GradeService;
import com.schoolmanager.service.StudentService;
import com.schoolmanager.service.SubjectService;
import com.schoolmanager.service.TeacherService;

public class ApplicationConfig {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final StudentApplication studentApplication;

    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final TeacherApplication teacherApplication;

    private final SubjectRepository subjectRepository;
    private final SubjectService subjectService;
    private final SubjectApplication subjectApplication;

    private final ClassroomRepository classroomRepository;
    private final ClassroomService classroomService;
    private final ClassroomApplication classroomApplication;

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;
    private final EnrollmentApplication enrollmentApplication;

    private final GradeRepository gradeRepository;
    private final GradeService gradeService;
    private final GradeApplication gradeApplication;

    public ApplicationConfig() {
        studentRepository = new StudentRepositoryImpl();
        studentService = new StudentService(studentRepository);
        studentApplication = new StudentApplication(studentService);

        teacherRepository = new TeacherRepositoryImpl();
        teacherService = new TeacherService(teacherRepository);
        teacherApplication = new TeacherApplication(teacherService);

        subjectRepository = new SubjectRepositoryImpl();
        subjectService = new SubjectService(subjectRepository);
        subjectApplication = new SubjectApplication(subjectService);

        classroomRepository = new ClassroomRepositoryImpl();
        classroomService = new ClassroomService(classroomRepository);
        classroomApplication = new ClassroomApplication(classroomService);

        enrollmentRepository = new EnrollmentRepositoryImpl();
        enrollmentService = new EnrollmentService(
                enrollmentRepository,
                studentRepository,
                classroomRepository);
        enrollmentApplication = new EnrollmentApplication(enrollmentService);

        gradeRepository = new GradeRepositoryImpl();
        gradeService = new GradeService(
                gradeRepository,
                enrollmentRepository);
        gradeApplication = new GradeApplication(gradeService);
    }

    public StudentApplication studentApplication() {
        return studentApplication;
    }

    public TeacherApplication teacherApplication() {
        return teacherApplication;
    }

    public SubjectApplication subjectApplication() {
        return subjectApplication;
    }

    public ClassroomApplication classroomApplication() {
        return classroomApplication;
    }

    public EnrollmentApplication enrollmentApplication() {
        return enrollmentApplication;
    }

    public GradeApplication gradeApplication() {
        return gradeApplication;
    }
}
