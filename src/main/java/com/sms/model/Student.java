package com.sms.model;

// Encapsulation: all fields are private at the top)
public abstract class Student {

    private String studentId;
    private String name;
    private int age;
    private String email;
    private double gpa;

    public Student(String studentId, String name, int age, String email, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.email = email;
        this.gpa = gpa;
    }

    // --- Getters ---
    public String getStudentId() { return studentId; }
    public String getName()      { return name; }
    public int    getAge()       { return age; }
    public String getEmail()     { return email; }
    public double getGpa()       { return gpa; }

    // --- Setters ---
    public void setName(String name)    { this.name = name; }
    public void setAge(int age)         { this.age = age; }
    public void setEmail(String email)  { this.email = email; }
    public void setGpa(double gpa)      { this.gpa = gpa; }

    
    public abstract void displayInfo();

    /**
     * Returns the student type string for CSV serialization.
     */
    public abstract String getStudentType();

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Name: %-20s | Age: %2d | GPA: %.2f | Email: %s",
                getStudentType(), studentId, name, age, gpa, email);
    }
}
