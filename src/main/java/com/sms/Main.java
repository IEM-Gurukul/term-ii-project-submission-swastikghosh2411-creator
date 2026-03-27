package com.sms;

import com.sms.repository.CsvStudentRepository;
import com.sms.repository.IStudentRepository;
import com.sms.service.StudentService;
import com.sms.ui.ConsoleUI;

/**
 * Application entry point.
 * Wires together Repository → Service → UI.
 */
public class Main {

    public static void main(String[] args) {
        // Dependency injection — swap CsvStudentRepository for any other
        // IStudentRepository implementation without changing anything else.
        IStudentRepository repository = new CsvStudentRepository("data/students.csv");
        StudentService     service    = new StudentService(repository);
        ConsoleUI          ui         = new ConsoleUI(service);
        ui.start();
    }
}
