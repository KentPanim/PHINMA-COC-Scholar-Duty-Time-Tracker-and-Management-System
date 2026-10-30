/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DutyScheduleService {
  



    public boolean addSchedule(
            int assignmentId,
            String dayOrDate,
            String scheduleType,
            String startTime,
            String endTime) {

        String sql = "INSERT INTO duty_schedules "
                + "(assignment_id, day_of_week, schedule_date, "
                + "schedule_type, start_time, end_time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {

            
            java.sql.Date scheduleDate = null;
            String dayOfWeek = null;

            if (dayOrDate != null && !dayOrDate.trim().isEmpty() && dayOrDate.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                java.time.format.DateTimeFormatter dateFormatter =
                        java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");

                java.time.LocalDate date =
                        java.time.LocalDate.parse(dayOrDate, dateFormatter);

                scheduleDate = java.sql.Date.valueOf(date);
            } else {
                dayOfWeek = dayOrDate;
            }
DateTimeFormatter timeFormatter =
        DateTimeFormatter.ofPattern("h:mm a");

LocalTime start = null;
LocalTime end = null;

if (startTime != null && !startTime.trim().isEmpty()) {
    start = LocalTime.parse(
            startTime.trim().toUpperCase(), timeFormatter);
}

if (endTime != null && !endTime.trim().isEmpty()) {
    end = LocalTime.parse(
            endTime.trim().toUpperCase(), timeFormatter);
}
            pst.setInt(1, assignmentId);
            pst.setString(2, dayOfWeek);

            if (scheduleDate != null) {
                pst.setDate(3, scheduleDate);
            } else {
                pst.setNull(3, java.sql.Types.DATE);
            }

            pst.setString(4, scheduleType);
            if (start != null) {
    pst.setTime(5, java.sql.Time.valueOf(start));
} else {
    pst.setNull(5, java.sql.Types.TIME);
}

if (end != null) {
    pst.setTime(6, java.sql.Time.valueOf(end));
} else {
    pst.setNull(6, java.sql.Types.TIME);
}

            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<DutySchedule> findAllSchedules() {

    List<DutySchedule> schedules = new ArrayList<>();

    String sql = "SELECT "
            + "ds.schedule_id, "
            + "s.student_id, "
            + "CONCAT(s.first_name, ' ', s.middle_initial, ' ', s.last_name) AS scholar_name, "
            + "ds.schedule_type, "
            + "CASE "
            + "WHEN ds.schedule_date IS NOT NULL "
            + "THEN DATE_FORMAT(ds.schedule_date, '%m/%d/%Y') "
            + "ELSE ds.day_of_week "
            + "END AS day_or_date, "
            + "TIME_FORMAT(ds.start_time, '%h:%i %p') AS start_time, "
            + "TIME_FORMAT(ds.end_time, '%h:%i %p') AS end_time "
            + "FROM duty_schedules ds "
            + "INNER JOIN assignments a ON ds.assignment_id = a.assignment_id "
            + "INNER JOIN scholars s ON a.scholar_id = s.scholar_id "
            + "ORDER BY ds.schedule_id DESC";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()
    ) {

        while (rs.next()) {

            DutySchedule schedule = new DutySchedule(
                    rs.getInt("schedule_id"),
                    rs.getString("student_id"),
                    rs.getString("scholar_name"),
                    rs.getString("schedule_type"),
                    rs.getString("day_or_date"),
                    rs.getString("start_time"),
                    rs.getString("end_time")
            );

            schedules.add(schedule);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return schedules;
}
}


