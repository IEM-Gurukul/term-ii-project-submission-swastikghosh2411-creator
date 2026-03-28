package com.sms.repository;

import com.sms.exception.DuplicateStudentException;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.GraduateStudent;
import com.sms.model.Student;
import com.sms.model.UndergraduateStudent;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CsvStudentRepository implements IStudentRepository {

    private final String filePath;
    private final Map<String, Student> store = new LinkedHashMap<>();

    public CsvStudentRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }

    // ------------------------------------------------------------------ CRUD

    @Override
    public void addStudent(Student student) throws DuplicateStudentException {
        if (store.containsKey(student.getStudentId())) {
            throw new DuplicateStudentException(student.getStudentId());
        }
        store.put(student.getStudentId(), student);
        saveToFile();
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Student> findById(String studentId) {
        return Optional.ofNullable(store.get(studentId));
    }

    @Override
    public void updateStudent(Student student) throws StudentNotFoundException {
        if (!store.containsKey(student.getStudentId())) {
            throw new StudentNotFoundException(student.getStudentId());
        }
        store.put(student.getStudentId(), student);
        saveToFile();
    }

    @Override
    public void deleteStudent(String studentId) throws StudentNotFoundException {
        if (!store.containsKey(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        store.remove(studentId);
        saveToFile();
    }

    @Override
    public List<Student> searchByName(String nameFragment) {
        String lower = nameFragment.toLowerCase();
        return store.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------- Persistence

    private void saveToFile() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Student s : store.values()) {
                bw.write(serialize(s));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Could not save data: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Student s = deserialize(line);
                if (s != null) store.put(s.getStudentId(), s);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Could not load data: " + e.getMessage());
        }
    }

    private String serialize(Student s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s.getStudentType()).append(",")
          .append(escape(s.getStudentId())).append(",")
          .append(escape(s.getName())).append(",")
          .append(s.getAge()).append(",")
          .append(escape(s.getEmail())).append(",")
          .append(s.getGpa());

        if (s instanceof UndergraduateStudent) {
            UndergraduateStudent ug = (UndergraduateStudent) s;
            sb.append(",").append(ug.getCurrentYear())
              .append(",").append(escape(ug.getMajor()));
        } else if (s instanceof GraduateStudent) {
            GraduateStudent pg = (GraduateStudent) s;
            sb.append(",").append(escape(pg.getResearchTopic()))
              .append(",").append(escape(pg.getSupervisor()));
        }
        return sb.toString();
    }

    private Student deserialize(String line) {
        String[] p = line.split(",", -1);
        try {
            String type    = p[0];
            String id      = unescape(p[1]);
            String name    = unescape(p[2]);
            int    age     = Integer.parseInt(p[3]);
            String email   = unescape(p[4]);
            double gpa     = Double.parseDouble(p[5]);

            if ("UG".equals(type)) {
                int    year  = Integer.parseInt(p[6]);
                String major = unescape(p[7]);
                return new UndergraduateStudent(id, name, age, email, gpa, year, major);
            } else if ("PG".equals(type)) {
                String topic = unescape(p[6]);
                String sup   = unescape(p[7]);
                return new GraduateStudent(id, name, age, email, gpa, topic, sup);
            }
        } catch (Exception e) {
            System.err.println("[WARN] Skipping malformed record: " + line);
        }
        return null;
    }

    /** Escape commas inside field values. */
    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace(",", "\\,");
    }

    private String unescape(String s) {
        return s == null ? "" : s.replace("\\,", ",").replace("\\\\", "\\");
    }
}
