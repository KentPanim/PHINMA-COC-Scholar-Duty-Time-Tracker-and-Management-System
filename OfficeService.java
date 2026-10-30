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


    

public class OfficeService {

    public List<Office> findAllOffices() {

        List<Office> officeList = new ArrayList<>();

        String sql = "SELECT office_id, office_name, description "
                + "FROM offices";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {

                Office office = new Office(
                    rs.getInt("office_id"),
                    rs.getString("office_name"),
                    rs.getString("description")
                );

                officeList.add(office);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return officeList;
    }
}

    
    
    

