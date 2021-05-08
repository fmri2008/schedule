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
public class ApptReport {
    private String month;
    private String type;
    private String amount;
    
    public ApptReport(String month, String type, String amount) {
        this.month = month;
        this.type = type;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }
}
