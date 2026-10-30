/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author kent panim
 */
public class AssignmentService {
    
 

    public List<Assignment> findAllAssignments() {

        List<Assignment> assignmentList = new ArrayList<>();

        String sql = "SELECT "
                + "a.assignment_id, "
                + "s.student_id, "
                + "CONCAT(s.first_name, ' ', s.middle_initial, ' ', s.last_name) AS scholar_name, "
                + "CONCAT(st.first_name, ' ', st.last_name) AS staff_name, "
                + "o.office_name, "
                + "a.assigned_hours, "
                + "a.status "
                + "FROM assignments a "
                + "INNER JOIN scholars s ON a.scholar_id = s.scholar_id "
                + "INNER JOIN staff st ON a.staff_id = st.staff_id "
                + "INNER JOIN offices o ON a.office_id = o.office_id";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {

                Assignment assignment = new Assignment(
                    rs.getInt("assignment_id"),
                    rs.getString("student_id"),
                    rs.getString("scholar_name"),
                    rs.getString("staff_name"),
                    rs.getString("office_name"),
                    rs.getInt("assigned_hours"),
                    rs.getString("status")
                );

                assignmentList.add(assignment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignmentList;
    }
    
    public boolean addAssignment(
        int scholarId,
        int staffId,
        int officeId,
        int assignedHours,
        String startDate,
        String endDate,
        String status) {
        

    String sql = "INSERT INTO assignments "
            + "(scholar_id, staff_id, office_id, assigned_hours, "
            + "start_date, end_date, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql)
    ) {
        DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("M/d/yyyy");

LocalDate start = LocalDate.parse(startDate, formatter);
LocalDate end = LocalDate.parse(endDate, formatter);



        pst.setInt(1, scholarId);
        pst.setInt(2, staffId);
        pst.setInt(3, officeId);
        pst.setInt(4, assignedHours);
        pst.setDate(5, Date.valueOf(start));
        pst.setDate(6, Date.valueOf(end));
        pst.setString(7, status);

        pst.executeUpdate();

        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public int findActiveAssignmentIdByScholarId(int scholarId) {

    String sql = "SELECT assignment_id "
            + "FROM assignments "
            + "WHERE scholar_id = ? "
            + "AND status = 'Active' "
            + "LIMIT 1";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql)
    ) {

        pst.setInt(1, scholarId);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getInt("assignment_id");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return 0;
}
}
    

