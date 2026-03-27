package com.sms.exception;

/**
 * Thrown when an operation references a student ID that does not exist.
 */
public class StudentNotFoundException extends Exception {

    private final String studentId;

    public StudentNotFoundException(String studentId) {
        super("Student not found with ID: " + studentId);
        this.studentId = studentId;
    }

    public String getStudentId() { return studentId; }
}
