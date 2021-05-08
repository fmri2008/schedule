/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utils.DBConnection;

/**
 *
 * @author Dawei Li
 */
public class CustomerDao {
    private ObservableList<Customer> customers;
    
    public CustomerDao() {
        this.customers = FXCollections.observableArrayList();
    }
    
    /**
     * Get all customers from the database
     * @return ObservableList<Customer> a list of all customers
     */
    public ObservableList<Customer> getCustomers() {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT customer.customerId, customer.customerName, address.address, address.postalCode, city.city, country.country, address.phone " 
                + "FROM customer, city, country, address "
                + "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("customer.customerId");
                String name = resultSet.getString("customer.customerName");
                String address = resultSet.getString("address.address");
                String postCode = resultSet.getString("address.postalCode");
                String city = resultSet.getString("city.city");
                String country = resultSet.getString("country.country");
                String phone = resultSet.getString("address.phone");
                customers.add(new Customer(id, name, address, postCode, city, country, phone));
            }
        } catch (SQLException e) {
            System.out.print("Error with SQL statement in CustomerDao.getCustomers()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /** Add a new customer record to the customer table.
     * @param name
     * @param addressId
     * @param userName
     */
    public void addCustomer(String name, int addressId, String userName) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "INSERT INTO customer " + 
                "(customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES (default, ?, ?, '1', CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)"
            );
            ps.setString(1, name);
            ps.setInt(2, addressId);
            ps.setString(3, userName);
            ps.setString(4, userName);
            int result = ps.executeUpdate();
            System.out.println(result == 1 ? "addCustomer success" : "addCustomer failure");
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CustomerDao.addCustomer()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Update a customer record in the database.
     * @param id
     * @param name
     * @param addressId
     * @param userName 
     */
    public void updateCustomer(int id, String name, int addressId, String userName) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "UPDATE customer SET " +
                "customerName = ?, addressId = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? " +
                "WHERE customerId = ?"
            );
            ps.setString(1, name);
            ps.setInt(2, addressId);
            ps.setString(3, userName);
            ps.setInt(4, id);
            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CustomerDao.updateCustomer()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Delete a customer record from the customer table in the database.
     * @param id 
     */
    public void deleteCustomer(int id) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "DELETE FROM customer WHERE customerId = ?"
            );
            ps.setInt(1, id);
            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CustomerDao.deleteCustomer()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get customer id given a customer name.
     * @param name
     * @return 
     */
    public int getCustomerId(String name) {
        int id = -1;
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT customerId FROM customer WHERE customerName = ?"
            );
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("customerId");
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CustomerDao.getCustomerId()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
