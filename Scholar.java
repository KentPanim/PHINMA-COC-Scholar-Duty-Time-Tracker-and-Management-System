/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */


public class Scholar {

    private int scholarId;
    private String studentId;
    private String lastName;
    private String firstName;
    private String middleInitial;
    private String email;
    private String course;
    private String program;

    public Scholar() {
    }

    public Scholar(int scholarId, String studentId,
                   String lastName, String firstName,
                   String middleInitial, String email,
                   String course, String program) {

        this.scholarId = scholarId;
        this.studentId = studentId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.email = email;
        this.course = course;
        this.program = program;
    }

    public int getScholarId() {
        return scholarId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    public String getProgram() {
        return program;
    }

    public void setScholarId(int scholarId) {
        this.scholarId = scholarId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
