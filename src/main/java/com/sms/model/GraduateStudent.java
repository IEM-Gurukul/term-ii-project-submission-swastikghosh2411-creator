package com.sms.model;

/**
 * Represents a Graduate (postgraduate) student.
 * Demonstrates Inheritance — extends Student.
 */
public class GraduateStudent extends Student {

    private String researchTopic;
    private String supervisor;

    public GraduateStudent(String studentId, String name, int age,
                            String email, double gpa,
                            String researchTopic, String supervisor) {
        super(studentId, name, age, email, gpa);
        this.researchTopic = researchTopic;
        this.supervisor = supervisor;
    }

    public String getResearchTopic() { return researchTopic; }
    public String getSupervisor()    { return supervisor; }
    public void setResearchTopic(String researchTopic) { this.researchTopic = researchTopic; }
    public void setSupervisor(String supervisor)       { this.supervisor = supervisor; }

    /**
     * Polymorphic display — specific to graduate info.
     */
    @Override
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│          GRADUATE STUDENT DETAILS            │");
        System.out.println("├─────────────────────────────────────────────┤");
        System.out.printf( "│  ID         : %-30s│%n", getStudentId());
        System.out.printf( "│  Name       : %-30s│%n", getName());
        System.out.printf( "│  Age        : %-30d│%n", getAge());
        System.out.printf( "│  Email      : %-30s│%n", getEmail());
        System.out.printf( "│  GPA        : %-30.2f│%n", getGpa());
        System.out.printf( "│  Research   : %-30s│%n", researchTopic);
        System.out.printf( "│  Supervisor : %-30s│%n", supervisor);
        System.out.println("└─────────────────────────────────────────────┘");
    }

    @Override
    public String getStudentType() { return "PG"; }
}
