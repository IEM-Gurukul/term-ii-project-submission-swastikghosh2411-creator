# PCCCS495 – Term II Project

## Project Title
Student Management System

---

## Problem Statement (max 150 words)
Managing student records manually is error-prone and inefficient. This project solves that with a simple offline Java console application where you can add, search, update, and delete student records, with support for both Undergraduate and Graduate student types. No internet needed, no complicated setup. Data is saved to a CSV file so nothing is lost when you close the app. The focus was on applying core OOP principles in a clean layered architecture — Abstraction through interfaces, Inheritance through student subclasses, Polymorphism through overridden display methods, and Exception Handling with custom exceptions for invalid input and missing records. Input validation catches bad data before it reaches storage, and a summary report gives a quick overview of all students in the system.

---

## Target User
University administrators and faculty who need a lightweight, no-setup tool to manage student records — no database required, just run and use.

---

## Core Features
- Add Undergraduate or Graduate students with full details
- View all students in a formatted list
- Search student by ID and view complete profile
- Update any field of an existing student record
- Delete a student with confirmation prompt
- CSV file persistence — data survives after the app closes
- Summary report showing total count, type breakdown, and average GPA
- Input validation that catches empty fields, bad GPA range, and malformed IDs

---

## OOP Concepts Used
- Abstraction: Student is abstract — subclasses decide their type via getStudentType() and displayInfo()
- Encapsulation: All fields in Student, UndergraduateStudent, GraduateStudent are private with getters/setters
- Inheritance: UndergraduateStudent and GraduateStudent both extend Student and reuse all base fields
- Polymorphism: A single List<Student> holds both types — displayInfo() behaves differently per object at runtime
- Exception Handling: StudentNotFoundException, DuplicateStudentException, InvalidInputException — thrown at service layer, caught at UI layer
- Collections: ArrayList and LinkedHashMap used in StudentService and CsvStudentRepository

---

## Proposed Architecture Description
The app is structured around a clean layered separation across six packages:
- model/ — data layer: abstract Student, UndergraduateStudent, GraduateStudent
- exception/ — custom exceptions: StudentNotFoundException, DuplicateStudentException, InvalidInputException
- repository/ — storage contract: IStudentRepository interface + CsvStudentRepository implementation
- service/ — business logic: StudentService validates input and delegates to repository
- ui/ — ConsoleUI runs the menu, reads input, calls service, catches exceptions
- util/ — InputValidator handles all field-level validation

Each layer only talks to the one next to it. StudentService depends on IStudentRepository (not the concrete CSV class), so swapping storage backends requires changing one line.

---

## How to Run
1. Clone this repository
2. Navigate to the project root (the folder containing build.sh)
3. Make the build script executable (only needed once):
   chmod +x build.sh
4. Compile and run:
   ./build.sh
5. To run unit tests:
   ./build.sh test

A data/students.csv file is created automatically on first run — that is where all student records are saved.

---

## Git Discipline Notes
Minimum 10 meaningful commits required. Commit history for this project:
1. Initial commit: project scaffold with README, folder structure, and .gitignore
2. feat(model): add abstract Student base class with Encapsulation
3. feat(exception): add custom exceptions for robust error handling
4. feat(repository): add IStudentRepository interface and CsvStudentRepository
5. feat(service+util): add StudentService and InputValidator
6. feat(ui): add ConsoleUI with full CRUD menu and graceful error handling
7. feat(main): wire up Main entry point with dependency injection
8. test: add unit tests for StudentService covering all CRUD paths
9. chore: add build script and sample data CSV
10. docs: add DESIGN.md — architecture, OOP mapping, persistence format