/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */
public class Staff {
    
  

    private int staffId;
    private int userId;
    private String staffCode;
    private String firstName;
    private String lastName;
    private String email;
    private String office;

    public Staff() {
    }

    public Staff(int staffId, int userId, String staffCode,
                 String firstName, String lastName, String email, String office) {

        this.staffId = staffId;
        this.userId = userId;
        this.staffCode = staffCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.office = office;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
            }
    
    public String getOffice() {
        return office;
    }
}

    

