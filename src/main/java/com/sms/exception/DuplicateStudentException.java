package com.sms.exception;

/**
 * Thrown when trying to add a student whose ID already exists in the system.
 */
public class DuplicateStudentException extends Exception {

    private final String studentId;

    public DuplicateStudentException(String studentId) {
        super("A student with ID " + studentId + " already exists.");
        this.studentId = studentId;
    }

    public String getStudentId() { return studentId; }
}
