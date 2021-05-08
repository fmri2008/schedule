/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dawei Li
 */
public class Appointment {
    private int id;
    private String title;
    private String type;
    private String startDT;
    private String endDT;
    private int userId;
    private int customerId;
    private String customerName;
    private String userName;
    
    public Appointment(int id, String title, String type, String startDT, String endDT, int userId, int customerId, String customerName, String userName) {
            this.id = id;
            this.title = title;
            this.type = type;
            this.startDT = startDT;
            this.endDT = endDT;
            this.userId = userId;
            this.customerId = customerId;
            this.customerName = customerName;
            this.userName = userName;
        }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getStartDT() {
        return startDT;
    }

    public String getEndDT() {
        return endDT;
    }

    public int getUserId() {
        return userId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
    
    public String getUserName() {
        return userName;
    }
}
