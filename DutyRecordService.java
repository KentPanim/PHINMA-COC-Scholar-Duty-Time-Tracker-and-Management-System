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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DutyRecordService {
    





    public List<DutyRecord> findAllRecords() {

        List<DutyRecord> records = new ArrayList<>();

        String sql = "SELECT "
                + "dr.record_id, "
                + "s.student_id, "
                + "CONCAT(s.first_name, ' ', s.middle_initial, ' ', s.last_name) AS scholar_name, "
                + "DATE_FORMAT(dr.duty_date, '%m/%d/%Y') AS duty_date, "
                + "TIME_FORMAT(dr.time_in, '%h:%i %p') AS time_in, "
                + "TIME_FORMAT(dr.time_out, '%h:%i %p') AS time_out, "
                + "dr.daily_hours, "
                + "dr.attendance_status, "
                + "dr.verification_status "
                + "FROM duty_records dr "
                + "INNER JOIN assignments a "
                + "ON dr.assignment_id = a.assignment_id "
                + "INNER JOIN scholars s "
                + "ON a.scholar_id = s.scholar_id "
                + "ORDER BY dr.duty_date DESC, dr.record_id DESC";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {

                DutyRecord record = new DutyRecord(
                        rs.getInt("record_id"),
                        rs.getString("student_id"),
                        rs.getString("scholar_name"),
                        rs.getString("duty_date"),
                        rs.getString("time_in"),
                        rs.getString("time_out"),
                        rs.getDouble("daily_hours"),
                        rs.getString("attendance_status"),
                        rs.getString("verification_status")
                );

                records.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }
}
    

