package com.sms.util;

import com.sms.exception.InvalidInputException;

/**
 * Utility class for validating user input.
 * Demonstrates Exception Handling with specific validation rules.
 */
public class InputValidator {

    private InputValidator() {}

    public static void validateStudentId(String id) throws InvalidInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be empty.");
        }
        if (!id.matches("[A-Za-z0-9_-]{3,15}")) {
            throw new InvalidInputException(
                "Student ID must be 3–15 alphanumeric characters (hyphens/underscores allowed). Got: " + id);
        }
    }

    public static void validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty.");
        }
        if (name.trim().length() < 2) {
            throw new InvalidInputException("Name must be at least 2 characters.");
        }
    }

    public static void validateAge(int age) throws InvalidInputException {
        if (age < 16 || age > 100) {
            throw new InvalidInputException("Age must be between 16 and 100. Got: " + age);
        }
    }

    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
    }

    public static void validateGpa(double gpa) throws InvalidInputException {
        if (gpa < 0.0 || gpa > 10.0) {
            throw new InvalidInputException("GPA must be between 0.0 and 10.0. Got: " + gpa);
        }
    }

    public static void validateYear(int year) throws InvalidInputException {
        if (year < 1 || year > 4) {
            throw new InvalidInputException("Year must be between 1 and 4. Got: " + year);
        }
    }

    public static int parseIntSafe(String input, String fieldName) throws InvalidInputException {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new InvalidInputException(fieldName + " must be a whole number. Got: '" + input + "'");
        }
    }

    public static double parseDoubleSafe(String input, String fieldName) throws InvalidInputException {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            throw new InvalidInputException(fieldName + " must be a decimal number. Got: '" + input + "'");
        }
    }
}
