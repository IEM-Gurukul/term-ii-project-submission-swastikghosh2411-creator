# Student Management System

A console-based Java application demonstrating core OOP principles including Abstraction, Encapsulation, Inheritance, Polymorphism, Exception Handling, and Collections.

## Author
**Swastik Ghosh** | IEM Gurukul | Term II OOP Project

## Features
- Add, view, update, and delete students
- Support for Undergraduate and Graduate student types (Inheritance & Polymorphism)
- File-based persistence using CSV storage
- Custom exception handling
- Role-based architecture (Admin / Student)
- Full unit test coverage for core services

## Project Structure
```
src/
├── main/java/com/sms/
│   ├── model/          # Student, UndergraduateStudent, GraduateStudent
│   ├── repository/     # IStudentRepository, CsvStudentRepository
│   ├── service/        # StudentService
│   ├── exception/      # Custom exceptions
│   ├── ui/             # ConsoleUI
│   └── util/           # InputValidator, FileUtil
└── test/java/com/sms/  # Unit tests
```

## OOP Concepts Used
| Concept | Where Applied |
|---|---|
| Encapsulation | All model classes with private fields + getters/setters |
| Abstraction | `IStudentRepository` interface, abstract `Student` base |
| Inheritance | `UndergraduateStudent`, `GraduateStudent` extend `Student` |
| Polymorphism | `displayInfo()` overridden in each student subclass |
| Exception Handling | Custom `StudentNotFoundException`, `DuplicateStudentException` |
| Collections | `ArrayList`, `HashMap` used in service layer |

## Running the Project
```bash
javac -d out src/main/java/com/sms/**/*.java
java -cp out com.sms.Main
```

## Data Persistence
Student data is stored in `data/students.csv`. The file is auto-created on first run.
