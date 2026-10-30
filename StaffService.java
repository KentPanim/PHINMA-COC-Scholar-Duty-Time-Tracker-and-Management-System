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
/**
 *
 * @author kent panim
 */
public class StaffService {
    
    
    


    public List<Staff> findAllStaff() {

    List<Staff> staffList = new ArrayList<>();

    String sql =
    "SELECT st.staff_id, st.user_id, st.staff_code, " +
    "st.first_name, st.last_name, st.email, " +
    "o.office_name " +
    "FROM staff st " +
    "LEFT JOIN offices o ON st.office_id = o.office_id " +
    "ORDER BY st.staff_id";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()
    ) {

        while (rs.next()) {

            Staff staff = new Staff(
                rs.getInt("staff_id"),
                rs.getInt("user_id"),
                rs.getString("staff_code"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("office_name")
            );

            staffList.add(staff);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return staffList;
}
}
