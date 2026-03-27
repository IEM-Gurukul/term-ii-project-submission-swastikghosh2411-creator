package com.sms;

import com.sms.exception.DuplicateStudentException;
import com.sms.exception.InvalidInputException;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.GraduateStudent;
import com.sms.model.Student;
import com.sms.model.UndergraduateStudent;
import com.sms.repository.IStudentRepository;
import com.sms.service.StudentService;

import java.util.*;

/**
 * Manual unit tests for StudentService (no JUnit dependency required).
 * Demonstrates testing strategy: each test is isolated and self-describing.
 *
 * Run via: java -cp out com.sms.StudentServiceTest
 */
public class StudentServiceTest {

    // ---- In-memory stub repository for isolated testing ----
    static class InMemoryRepository implements IStudentRepository {
        final Map<String, Student> store = new LinkedHashMap<>();

        @Override public void addStudent(Student s) throws DuplicateStudentException {
            if (store.containsKey(s.getStudentId())) throw new DuplicateStudentException(s.getStudentId());
            store.put(s.getStudentId(), s);
        }
        @Override public List<Student> getAllStudents() { return new ArrayList<>(store.values()); }
        @Override public Optional<Student> findById(String id) { return Optional.ofNullable(store.get(id)); }
        @Override public void updateStudent(Student s) throws StudentNotFoundException {
            if (!store.containsKey(s.getStudentId())) throw new StudentNotFoundException(s.getStudentId());
            store.put(s.getStudentId(), s);
        }
        @Override public void deleteStudent(String id) throws StudentNotFoundException {
            if (!store.remove(id, store.get(id))) throw new StudentNotFoundException(id);
        }
        @Override public List<Student> searchByName(String frag) {
            List<Student> r = new ArrayList<>();
            for (Student s : store.values())
                if (s.getName().toLowerCase().contains(frag.toLowerCase())) r.add(s);
            return r;
        }
    }

    // ---- Test runner helpers ----
    static int passed = 0, failed = 0;

    static void assertTrue(String test, boolean condition) {
        if (condition) { System.out.println("  [PASS] " + test); passed++; }
        else           { System.out.println("  [FAIL] " + test); failed++; }
    }

    static void assertThrows(String test, Class<? extends Exception> expected, ThrowingRunnable r) {
        try { r.run(); System.out.println("  [FAIL] " + test + " (no exception thrown)"); failed++; }
        catch (Exception e) {
            if (expected.isInstance(e)) { System.out.println("  [PASS] " + test); passed++; }
            else { System.out.println("  [FAIL] " + test + " (wrong exception: " + e.getClass().getSimpleName() + ")"); failed++; }
        }
    }

    @FunctionalInterface interface ThrowingRunnable { void run() throws Exception; }

    // ---- Tests ----

    static void testAddAndRetrieve() throws Exception {
        System.out.println("\n[TEST] addStudent + getStudentById");
        StudentService svc = new StudentService(new InMemoryRepository());
        Student s = new UndergraduateStudent("S001", "Alice Roy", 20, "alice@iem.edu", 8.5, 2, "CS");
        svc.addStudent(s);
        Student found = svc.getStudentById("S001");
        assertTrue("student ID matches", "S001".equals(found.getStudentId()));
        assertTrue("student name matches", "Alice Roy".equals(found.getName()));
    }

    static void testDuplicateThrows() {
        System.out.println("\n[TEST] addStudent throws DuplicateStudentException");
        StudentService svc = new StudentService(new InMemoryRepository());
        assertThrows("duplicate ID raises exception", DuplicateStudentException.class, () -> {
            svc.addStudent(new UndergraduateStudent("S002", "Bob Sen", 21, "bob@iem.edu", 7.0, 1, "ECE"));
            svc.addStudent(new UndergraduateStudent("S002", "Bob Sen", 21, "bob@iem.edu", 7.0, 1, "ECE"));
        });
    }

    static void testDeleteStudent() throws Exception {
        System.out.println("\n[TEST] deleteStudent");
        StudentService svc = new StudentService(new InMemoryRepository());
        svc.addStudent(new GraduateStudent("G001", "Priya Das", 24, "priya@iem.edu", 9.0, "AI Ethics", "Dr. Ghosh"));
        svc.deleteStudent("G001");
        assertTrue("student removed from list", svc.getAllStudents().isEmpty());
    }

    static void testDeleteNotFoundThrows() {
        System.out.println("\n[TEST] deleteStudent throws StudentNotFoundException");
        StudentService svc = new StudentService(new InMemoryRepository());
        assertThrows("deleting absent ID raises exception", StudentNotFoundException.class,
                () -> svc.deleteStudent("GHOST99"));
    }

    static void testUpdateStudent() throws Exception {
        System.out.println("\n[TEST] updateStudent");
        StudentService svc = new StudentService(new InMemoryRepository());
        svc.addStudent(new UndergraduateStudent("S003", "Rahul Bose", 22, "rahul@iem.edu", 6.5, 3, "Mech"));
        UndergraduateStudent updated = new UndergraduateStudent("S003", "Rahul Bose", 22, "rahul@iem.edu", 7.8, 3, "Mech");
        svc.updateStudent(updated);
        assertTrue("GPA updated correctly", svc.getStudentById("S003").getGpa() == 7.8);
    }

    static void testUpdateNotFoundThrows() {
        System.out.println("\n[TEST] updateStudent throws StudentNotFoundException");
        StudentService svc = new StudentService(new InMemoryRepository());
        assertThrows("updating absent ID raises exception", StudentNotFoundException.class,
                () -> svc.updateStudent(new UndergraduateStudent("X999", "Ghost", 20, "g@x.com", 5.0, 1, "CS")));
    }

    static void testSearchByName() throws Exception {
        System.out.println("\n[TEST] searchByName");
        StudentService svc = new StudentService(new InMemoryRepository());
        svc.addStudent(new UndergraduateStudent("S004", "Ananya Sharma", 19, "a@iem.edu", 9.2, 1, "CS"));
        svc.addStudent(new GraduateStudent("G002", "Sharma Kumar", 25, "sk@iem.edu", 8.8, "NLP", "Dr. Roy"));
        List<Student> results = svc.searchByName("sharma");
        assertTrue("search returns 2 results", results.size() == 2);
    }

    static void testInvalidGpaThrows() {
        System.out.println("\n[TEST] addStudent throws InvalidInputException for bad GPA");
        StudentService svc = new StudentService(new InMemoryRepository());
        assertThrows("GPA > 10 raises exception", InvalidInputException.class,
                () -> svc.addStudent(new UndergraduateStudent("S005", "Test User", 20, "t@t.com", 11.5, 1, "CS")));
    }

    static void testInvalidEmailThrows() {
        System.out.println("\n[TEST] addStudent throws InvalidInputException for bad email");
        StudentService svc = new StudentService(new InMemoryRepository());
        assertThrows("malformed email raises exception", InvalidInputException.class,
                () -> svc.addStudent(new UndergraduateStudent("S006", "Test User", 20, "not-an-email", 7.0, 1, "CS")));
    }

    // ---- Main ----

    public static void main(String[] args) throws Exception {
        System.out.println("══════════════════════════════════════");
        System.out.println("     StudentService Unit Tests         ");
        System.out.println("══════════════════════════════════════");

        testAddAndRetrieve();
        testDuplicateThrows();
        testDeleteStudent();
        testDeleteNotFoundThrows();
        testUpdateStudent();
        testUpdateNotFoundThrows();
        testSearchByName();
        testInvalidGpaThrows();
        testInvalidEmailThrows();

        System.out.println("\n══════════════════════════════════════");
        System.out.printf("  Results: %d passed, %d failed%n", passed, failed);
        System.out.println("══════════════════════════════════════");

        if (failed > 0) System.exit(1);
    }
}
