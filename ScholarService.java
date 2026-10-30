/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author kent panim
 */
public class ScholarService {
    
    
    public boolean addScholar(Scholar scholar) {

    String userSql =
            "INSERT INTO users (login_id, password, role) "
          + "VALUES (?, ?, 'Scholar')";

    String scholarSql =
            "INSERT INTO scholars "
          + "(user_id, student_id, last_name, first_name, "
          + "middle_initial, email, course, program) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    Connection conn = null;

    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        
        PreparedStatement userPst =
                conn.prepareStatement(
                        userSql,
                        Statement.RETURN_GENERATED_KEYS
                );

        String loginId = scholar.getStudentId();

        
        String password = scholar.getStudentId();

        userPst.setString(1, loginId);
        userPst.setString(2, password);

        userPst.executeUpdate();

       
        ResultSet keys = userPst.getGeneratedKeys();

        if (!keys.next()) {
            conn.rollback();
            return false;
        }

        int userId = keys.getInt(1);

     
        PreparedStatement scholarPst =
                conn.prepareStatement(scholarSql);

        scholarPst.setInt(1, userId);
        scholarPst.setString(2, scholar.getStudentId());
        scholarPst.setString(3, scholar.getLastName());
        scholarPst.setString(4, scholar.getFirstName());
        scholarPst.setString(5, scholar.getMiddleInitial());
        scholarPst.setString(6, scholar.getEmail());
        scholarPst.setString(7, scholar.getCourse());
        scholarPst.setString(8, scholar.getProgram());

        scholarPst.executeUpdate();

        conn.commit();

        return true;

    } catch (SQLException e) {

        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException rollbackError) {
            rollbackError.printStackTrace();
        }

        e.printStackTrace();
        return false;
    }

}
    public java.util.List<Scholar> findAllScholars() {

    java.util.List<Scholar> scholars = new java.util.ArrayList<>();

    String sql = "SELECT scholar_id, student_id, last_name, first_name, "
            + "middle_initial, email, course, program "
            + "FROM scholars";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery()
    ) {

        while (rs.next()) {

            Scholar scholar = new Scholar(
                rs.getInt("scholar_id"),
                rs.getString("student_id"),
                rs.getString("last_name"),
                rs.getString("first_name"),
                rs.getString("middle_initial"),
                rs.getString("email"),
                rs.getString("course"),
                rs.getString("program")
            );

            scholars.add(scholar);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return scholars;
}
    public boolean updateScholar(Scholar scholar) {

    String sql = "UPDATE scholars SET "
            + "last_name = ?, "
            + "first_name = ?, "
            + "middle_initial = ?, "
            + "email = ?, "
            + "course = ?, "
            + "program = ? "
            + "WHERE student_id = ?";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql)
    ) {

        pst.setString(1, scholar.getLastName());
        pst.setString(2, scholar.getFirstName());
        pst.setString(3, scholar.getMiddleInitial());
        pst.setString(4, scholar.getEmail());
        pst.setString(5, scholar.getCourse());
        pst.setString(6, scholar.getProgram());
        pst.setString(7, scholar.getStudentId());

        int rows = pst.executeUpdate();

        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public boolean deleteScholar(String studentId) {

    String sql = "DELETE FROM scholars WHERE student_id = ?";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql)
    ) {

        pst.setString(1, studentId);

        int rows = pst.executeUpdate();

        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public boolean studentIdExists(String studentId) {

    String sql = "SELECT student_id FROM scholars WHERE student_id = ?";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql)
    ) {

        pst.setString(1, studentId);

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            return rs.next();
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}
