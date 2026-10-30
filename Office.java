/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarduty;

/**
 *
 * @author kent panim
 */
public class Office {
   

    private int officeId;
    private String officeName;
    private String description;

    public Office() {
    }

    public Office(int officeId, String officeName, String description) {
        this.officeId = officeId;
        this.officeName = officeName;
        this.description = description;
    }

    public int getOfficeId() {
        return officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getDescription() {
        return description;
    }
}

