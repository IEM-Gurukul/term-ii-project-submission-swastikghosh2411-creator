package com.sms.ui;

import com.sms.exception.DuplicateStudentException;
import com.sms.exception.InvalidInputException;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.GraduateStudent;
import com.sms.model.Student;
import com.sms.model.UndergraduateStudent;
import com.sms.service.StudentService;
import com.sms.util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface.
 * Reads input, delegates to StudentService, handles all exceptions gracefully.
 */
public class ConsoleUI {

    private final StudentService service;
    private final Scanner scanner;

    public ConsoleUI(StudentService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudentFlow();
                case "2" -> viewAllStudents();
                case "3" -> searchStudentFlow();
                case "4" -> updateStudentFlow();
                case "5" -> deleteStudentFlow();
                case "6" -> service.printSummaryReport();
                case "0" -> {
                    System.out.println("\nGoodbye! Data has been saved to file.");
                    running = false;
                }
                default -> System.out.println("[!] Invalid choice. Please try again.");
            }
        }
    }

    // ------------------------------------------------------------------ ADD

    private void addStudentFlow() {
        System.out.println("\n--- ADD STUDENT ---");
        System.out.print("Type (1=Undergraduate, 2=Graduate): ");
        String typeChoice = scanner.nextLine().trim();

        try {
            System.out.print("Student ID  : "); String id    = scanner.nextLine().trim();
            System.out.print("Name        : "); String name  = scanner.nextLine().trim();
            System.out.print("Age         : "); int    age   = InputValidator.parseIntSafe(scanner.nextLine(), "Age");
            System.out.print("Email       : "); String email = scanner.nextLine().trim();
            System.out.print("GPA (0-10)  : "); double gpa   = InputValidator.parseDoubleSafe(scanner.nextLine(), "GPA");

            Student student;
            if ("1".equals(typeChoice)) {
                System.out.print("Year (1-4)  : "); int    year  = InputValidator.parseIntSafe(scanner.nextLine(), "Year");
                System.out.print("Major       : "); String major = scanner.nextLine().trim();
                InputValidator.validateYear(year);
                student = new UndergraduateStudent(id, name, age, email, gpa, year, major);
            } else if ("2".equals(typeChoice)) {
                System.out.print("Research Topic : "); String topic = scanner.nextLine().trim();
                System.out.print("Supervisor     : "); String sup   = scanner.nextLine().trim();
                student = new GraduateStudent(id, name, age, email, gpa, topic, sup);
            } else {
                System.out.println("[!] Invalid student type. Returning to menu.");
                return;
            }
            service.addStudent(student);

        } catch (InvalidInputException e) {
            System.out.println("[ERROR] Validation failed: " + e.getMessage());
        } catch (DuplicateStudentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // --------------------------------------------------------------- VIEW ALL

    private void viewAllStudents() {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("\nNo students found in the system.");
            return;
        }
        System.out.println("\n--- ALL STUDENTS (" + students.size() + ") ---");
        students.forEach(s -> System.out.println(s.toString()));
    }

    // --------------------------------------------------------------- SEARCH

    private void searchStudentFlow() {
        System.out.println("\n--- SEARCH STUDENT ---");
        System.out.print("Search by (1=ID, 2=Name): ");
        String mode = scanner.nextLine().trim();

        try {
            if ("1".equals(mode)) {
                System.out.print("Enter ID: ");
                String id = scanner.nextLine().trim();
                Student s = service.getStudentById(id);
                s.displayInfo();
            } else if ("2".equals(mode)) {
                System.out.print("Enter name fragment: ");
                String frag = scanner.nextLine().trim();
                List<Student> results = service.searchByName(frag);
                if (results.isEmpty()) {
                    System.out.println("No students found matching: " + frag);
                } else {
                    results.forEach(Student::displayInfo);
                }
            } else {
                System.out.println("[!] Invalid search mode.");
            }
        } catch (StudentNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // --------------------------------------------------------------- UPDATE

    private void updateStudentFlow() {
        System.out.println("\n--- UPDATE STUDENT ---");
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine().trim();

        try {
            Student existing = service.getStudentById(id);
            existing.displayInfo();
            System.out.println("(Press Enter to keep current value)");

            System.out.print("New Name [" + existing.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) existing.setName(name);

            System.out.print("New Age [" + existing.getAge() + "]: ");
            String ageStr = scanner.nextLine().trim();
            if (!ageStr.isEmpty()) existing.setAge(InputValidator.parseIntSafe(ageStr, "Age"));

            System.out.print("New Email [" + existing.getEmail() + "]: ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) existing.setEmail(email);

            System.out.print("New GPA [" + existing.getGpa() + "]: ");
            String gpaStr = scanner.nextLine().trim();
            if (!gpaStr.isEmpty()) existing.setGpa(InputValidator.parseDoubleSafe(gpaStr, "GPA"));

            if (existing instanceof UndergraduateStudent ug) {
                System.out.print("New Year [" + ug.getCurrentYear() + "]: ");
                String yearStr = scanner.nextLine().trim();
                if (!yearStr.isEmpty()) ug.setCurrentYear(InputValidator.parseIntSafe(yearStr, "Year"));

                System.out.print("New Major [" + ug.getMajor() + "]: ");
                String major = scanner.nextLine().trim();
                if (!major.isEmpty()) ug.setMajor(major);
            } else if (existing instanceof GraduateStudent pg) {
                System.out.print("New Research Topic [" + pg.getResearchTopic() + "]: ");
                String topic = scanner.nextLine().trim();
                if (!topic.isEmpty()) pg.setResearchTopic(topic);

                System.out.print("New Supervisor [" + pg.getSupervisor() + "]: ");
                String sup = scanner.nextLine().trim();
                if (!sup.isEmpty()) pg.setSupervisor(sup);
            }

            service.updateStudent(existing);

        } catch (StudentNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("[ERROR] Validation failed: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------- DELETE

    private void deleteStudentFlow() {
        System.out.println("\n--- DELETE STUDENT ---");
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();
        try {
            Student existing = service.getStudentById(id);
            System.out.println("About to delete: " + existing);
            System.out.print("Confirm? (yes/no): ");
            String confirm = scanner.nextLine().trim();
            if ("yes".equalsIgnoreCase(confirm)) {
                service.deleteStudent(id);
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (StudentNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // --------------------------------------------------------------- HELPERS

    private void printBanner() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      STUDENT MANAGEMENT SYSTEM v1.0      ║");
        System.out.println("║         IEM Gurukul — OOP Project         ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    private void printMainMenu() {
        System.out.println("\n┌── MAIN MENU ─────────────────────────────┐");
        System.out.println("│  1. Add Student                           │");
        System.out.println("│  2. View All Students                     │");
        System.out.println("│  3. Search Student                        │");
        System.out.println("│  4. Update Student                        │");
        System.out.println("│  5. Delete Student                        │");
        System.out.println("│  6. Summary Report                        │");
        System.out.println("│  0. Exit                                  │");
        System.out.println("└───────────────────────────────────────────┘");
        System.out.print("Choice: ");
    }
}
