package com.sms.model;

/**
 * Represents an Undergraduate student.
 * Demonstrates Inheritance — extends Student.
 */
public class UndergraduateStudent extends Student {

    private int currentYear;   // 1–4
    private String major;

    public UndergraduateStudent(String studentId, String name, int age,
                                 String email, double gpa,
                                 int currentYear, String major) {
        super(studentId, name, age, email, gpa);
        this.currentYear = currentYear;
        this.major = major;
    }

    public int    getCurrentYear() { return currentYear; }
    public String getMajor()       { return major; }
    public void   setCurrentYear(int currentYear) { this.currentYear = currentYear; }
    public void   setMajor(String major)          { this.major = major; }

    /**
     * Polymorphic display — specific to undergrad info.
     */
    @Override
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│        UNDERGRADUATE STUDENT DETAILS         │");
        System.out.println("├─────────────────────────────────────────────┤");
        System.out.printf( "│  ID      : %-33s│%n", getStudentId());
        System.out.printf( "│  Name    : %-33s│%n", getName());
        System.out.printf( "│  Age     : %-33d│%n", getAge());
        System.out.printf( "│  Email   : %-33s│%n", getEmail());
        System.out.printf( "│  GPA     : %-33.2f│%n", getGpa());
        System.out.printf( "│  Year    : Year %-28d│%n", currentYear);
        System.out.printf( "│  Major   : %-33s│%n", major);
        System.out.println("└─────────────────────────────────────────────┘");
    }

    @Override
    public String getStudentType() { return "UG"; }
}
