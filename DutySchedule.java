/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */
public class DutySchedule {
   

    private int scheduleId;
    private String studentId;
    private String scholarName;
    private String scheduleType;
    private String dayOrDate;
    private String startTime;
    private String endTime;

    public DutySchedule(
            int scheduleId,
            String studentId,
            String scholarName,
            String scheduleType,
            String dayOrDate,
            String startTime,
            String endTime) {

        this.scheduleId = scheduleId;
        this.studentId = studentId;
        this.scholarName = scholarName;
        this.scheduleType = scheduleType;
        this.dayOrDate = dayOrDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getScholarName() {
        return scholarName;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public String getDayOrDate() {
        return dayOrDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
    

