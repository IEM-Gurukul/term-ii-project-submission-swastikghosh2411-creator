package com.sms.repository;

import com.sms.exception.DuplicateStudentException;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface — enforces a contract for all storage implementations.
 * Demonstrates Abstraction via interface.
 * Any storage backend (CSV, SQLite, in-memory) can implement this.
 */
public interface IStudentRepository {

    /**
     * Persists a new student record.
     * @throws DuplicateStudentException if the ID already exists
     */
    void addStudent(Student student) throws DuplicateStudentException;

    /**
     * Returns all students.
     */
    List<Student> getAllStudents();

    /**
     * Finds a student by their unique ID.
     */
    Optional<Student> findById(String studentId);

    /**
     * Replaces an existing student record.
     * @throws StudentNotFoundException if the ID does not exist
     */
    void updateStudent(Student student) throws StudentNotFoundException;

    /**
     * Removes a student by ID.
     * @throws StudentNotFoundException if the ID does not exist
     */
    void deleteStudent(String studentId) throws StudentNotFoundException;

    /**
     * Searches students by partial name match (case-insensitive).
     */
    List<Student> searchByName(String nameFragment);
}
