/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */
import GUI.LoginGUI;

public class ScholarDutyTimeTracker1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        


        DatabaseConnection.getConnection();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginGUI().setVisible(true);
            }
        });
    }
}
        
        
   
    

