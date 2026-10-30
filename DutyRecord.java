/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */
public class DutyRecord {
    

    private int recordId;
    private String studentId;
    private String scholarName;
    private String dutyDate;
    private String timeIn;
    private String timeOut;
    private double dailyHours;
    private String attendanceStatus;
    private String verificationStatus;

    public DutyRecord(
            int recordId,
            String studentId,
            String scholarName,
            String dutyDate,
            String timeIn,
            String timeOut,
            double dailyHours,
            String attendanceStatus,
            String verificationStatus) {

        this.recordId = recordId;
        this.studentId = studentId;
        this.scholarName = scholarName;
        this.dutyDate = dutyDate;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.dailyHours = dailyHours;
        this.attendanceStatus = attendanceStatus;
        this.verificationStatus = verificationStatus;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getScholarName() {
        return scholarName;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public double getDailyHours() {
        return dailyHours;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }
}
    

