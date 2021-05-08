/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DBConnection;

/**
 *
 * @author Dawei Li
 */
public class AddressDao {
    
    /**
     * Add an address record to the database.
     * @param address
     * @param cityId
     * @param postCode
     * @param phone
     * @param userName 
     */
    public void addAddress(String address, int cityId, String postCode, String phone, String userName) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "INSERT INTO address " +
                "(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES (default, ?, '', ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)"
            );
            ps.setString(1, address);
            ps.setInt(2, cityId);
            ps.setString(3, postCode);
            ps.setString(4, phone);
            ps.setString(5, userName);
            ps.setString(6, userName);
            int result = ps.executeUpdate();
            System.out.println(result == 1 ? "addAddress success" : "addAddress failure");
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in AddressDao.addAddress()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
