/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */
public class Assignment {
    
    

    private int assignmentId;
    private String studentId;
    private String scholarName;
    private String staffName;
    private String officeName;
    private int assignedHours;
    private String status;

    public Assignment() {
    }

    public Assignment(int assignmentId, String studentId,
                      String scholarName, String staffName,
                      String officeName, int assignedHours,
                      String status) {

        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.scholarName = scholarName;
        this.staffName = staffName;
        this.officeName = officeName;
        this.assignedHours = assignedHours;
        this.status = status;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getScholarName() {
        return scholarName;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public int getAssignedHours() {
        return assignedHours;
    }

    public String getStatus() {
        return status;
    }
}
    

