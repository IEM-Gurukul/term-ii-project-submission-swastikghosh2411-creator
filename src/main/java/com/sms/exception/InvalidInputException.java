package com.sms.exception;

/**
 * Thrown when user-supplied input fails validation
 * (e.g., invalid GPA range, malformed ID, non-numeric age).
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}
