# Design Document — Student Management System

## 1. Architecture Overview

```
┌──────────────┐     ┌──────────────────┐     ┌────────────────────────┐
│   ConsoleUI  │────▶│  StudentService  │────▶│  IStudentRepository    │
│  (ui layer)  │     │ (business logic) │     │      «interface»       │
└──────────────┘     └──────────────────┘     └────────────┬───────────┘
                                                            │
                                               ┌────────────▼───────────┐
                                               │  CsvStudentRepository  │
                                               │   (data/students.csv)  │
                                               └────────────────────────┘
```

## 2. OOP Concepts Applied

### Encapsulation
All fields in `Student`, `UndergraduateStudent`, and `GraduateStudent` are `private`.
Access is only via public getters and setters.

### Abstraction
- `Student` is an `abstract` class with abstract methods `displayInfo()` and `getStudentType()`.
- `IStudentRepository` is an `interface` defining the contract for all storage implementations.
  `StudentService` depends only on `IStudentRepository`, never on `CsvStudentRepository` directly.

### Inheritance
```
Student (abstract)
├── UndergraduateStudent  [adds: currentYear, major]
└── GraduateStudent       [adds: researchTopic, supervisor]
```

### Polymorphism
- `displayInfo()` is overridden in both subclasses to render type-specific layouts.
- `getStudentType()` returns `"UG"` or `"PG"` used in serialization.
- The service layer works with `Student` references, not concrete types.

### Exception Handling
| Exception | When Thrown |
|---|---|
| `StudentNotFoundException` | update/delete/search with an ID that doesn't exist |
| `DuplicateStudentException` | adding a student whose ID already exists |
| `InvalidInputException` | bad GPA, age out of range, malformed email, bad ID format |
| `NumberFormatException` (caught) | non-numeric input for age/GPA converted to `InvalidInputException` |

### Collections
- `LinkedHashMap<String, Student>` in `CsvStudentRepository` preserves insertion order.
- `ArrayList<Student>` returned from `getAllStudents()` and `searchByName()`.
- Java Streams used in `printSummaryReport()` for aggregations.

## 3. Persistence
Data is stored in `data/students.csv`. The format is:

```
TYPE,ID,NAME,AGE,EMAIL,GPA,EXTRA1,EXTRA2
UG,S001,Alice Roy,20,alice@iem.edu,8.5,2,Computer Science
PG,G001,Priya Das,24,priya@iem.edu,9.1,Deep Learning,Dr. Ghosh
```

Commas within field values are escaped as `\,`.

## 4. How to Run
```bash
chmod +x build.sh
./build.sh        # run the app
./build.sh test   # run the 9 unit tests
```
