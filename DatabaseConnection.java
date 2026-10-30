/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




/**
 *
 * @author kent panim
 */
public class DatabaseConnection {
    

    private static final String URL =
            "jdbc:mysql://localhost:3308/scholar_duty_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected!");
            return connection;

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}

