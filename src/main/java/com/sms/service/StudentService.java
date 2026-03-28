package com.sms.service;

import com.sms.exception.DuplicateStudentException;
import com.sms.exception.InvalidInputException;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.Student;
import com.sms.repository.IStudentRepository;
import com.sms.util.InputValidator;

import java.util.List;
import java.util.Optional;

/**
 * Service layer — sits between the UI and Repository.
 * Contains all business logic and validation.
 * Uses IStudentRepository for Abstraction (not tied to any concrete storage).
 */
public class StudentService {

    private final IStudentRepository repository;

    public StudentService(IStudentRepository repository) {
        this.repository = repository;
    }

    public void addStudent(Student student)
            throws DuplicateStudentException, InvalidInputException {
        InputValidator.validateStudentId(student.getStudentId());
        InputValidator.validateName(student.getName());
        InputValidator.validateAge(student.getAge());
        InputValidator.validateEmail(student.getEmail());
        InputValidator.validateGpa(student.getGpa());
        repository.addStudent(student);
        System.out.println("[OK] Student added: " + student.getStudentId());
    }

    public List<Student> getAllStudents() {
        return repository.getAllStudents();
    }

    /**
     * @throws StudentNotFoundException if ID is absent
     */
    public Student getStudentById(String id) throws StudentNotFoundException {
        Optional<Student> result = repository.findById(id);
        if (result.isEmpty()) {
            throw new StudentNotFoundException(id);
        }
        return result.get();
    }

    public void updateStudent(Student student)
            throws StudentNotFoundException, InvalidInputException {
        InputValidator.validateName(student.getName());
        InputValidator.validateAge(student.getAge());
        InputValidator.validateEmail(student.getEmail());
        InputValidator.validateGpa(student.getGpa());
        repository.updateStudent(student);
        System.out.println("[OK] Student updated: " + student.getStudentId());
    }

    public void deleteStudent(String studentId) throws StudentNotFoundException {
        repository.deleteStudent(studentId);
        System.out.println("[OK] Student deleted: " + studentId);
    }

    public List<Student> searchByName(String nameFragment) {
        return repository.searchByName(nameFragment);
    }

    /**
     * Computes a summary report across all students.
     */
    public void printSummaryReport() {
        List<Student> all = repository.getAllStudents();
        if (all.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }
        long ugCount = all.stream().filter(s -> s.getStudentType().equals("UG")).count();
        long pgCount = all.stream().filter(s -> s.getStudentType().equals("PG")).count();
        double avgGpa = all.stream().mapToDouble(Student::getGpa).average().orElse(0);
        double maxGpa = all.stream().mapToDouble(Student::getGpa).max().orElse(0);

        System.out.println("════════════════════════════════════════");
        System.out.println("          SYSTEM SUMMARY REPORT         ");
        System.out.println("════════════════════════════════════════");
        System.out.printf("  Total Students    : %d%n", all.size());
        System.out.printf("  Undergraduate     : %d%n", ugCount);
        System.out.printf("  Graduate          : %d%n", pgCount);
        System.out.printf("  Average GPA       : %.2f%n", avgGpa);
        System.out.printf("  Highest GPA       : %.2f%n", maxGpa);
        System.out.println("════════════════════════════════════════");
    }
}
