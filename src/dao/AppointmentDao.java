/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.ApptReport;
import utils.DBConnection;

/**
 *
 * @author Dawei Li
 */
public class AppointmentDao {

    private ZoneId localZoneId;
    private DateTimeFormatter dtFormatter;
    
    public AppointmentDao() {
        this.localZoneId = ZoneId.systemDefault();
        this.dtFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    }
    
    /**
     * Retrieve all appointments for one user identified by id.
     * @return ObservableList<Appointment> a list of all appointments of one user in the database.
     */
    public ObservableList<Appointment> getUserAppts(int userId)  {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try{           
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT appointment.appointmentId, appointment.title, appointment.type, appointment.start, appointment.end, "
                + "appointment.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment , customer WHERE appointment.customerId = customer.customerId AND appointment.userId = ?"
            );
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("appointment.appointmentId");
                String title = resultSet.getString("appointment.title");
                String type = resultSet.getString("appointment.type");
                
                Timestamp startTsUTC = resultSet.getTimestamp("appointment.start");
                ZonedDateTime startZdtUTC = startTsUTC.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime startZdtLocal = startZdtUTC.withZoneSameInstant(localZoneId);
                String startZdtLocalString = startZdtLocal.format(dtFormatter);
                
                Timestamp endTime = resultSet.getTimestamp("appointment.end");
                ZonedDateTime endZdtUTC = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime endZdtLocal = endZdtUTC.withZoneSameInstant(localZoneId);
                String endZdtLocalString = endZdtLocal.format(dtFormatter);
                
                int customerId = resultSet.getInt("appointment.customerId");
                String customerName = resultSet.getString("customer.customerName");
                String userName = resultSet.getString("appointment.createdBy");
                appts.add(new Appointment(id, title, type, startZdtLocalString, endZdtLocalString, userId, customerId, customerName, userName));
            }
        } catch (SQLException e) {
            System.out.print("Error with SQL statement in AppointmentDao.getCurrUserAppts()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appts;
    }
    
    /**
     * Get all appointments of a certain customer, given the customer id.
     * @param customerId
     * @return 
     */
    public ObservableList<Appointment> getCustomerAppts(int customerId) {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try{           
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT appointment.appointmentId, appointment.title, appointment.type, appointment.start, appointment.end, "
                + "appointment.userId, appointment.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment , customer WHERE appointment.customerId = customer.customerId AND appointment.customerId = ?"
            );
            ps.setInt(1, customerId);
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("appointment.appointmentId");
                String title = resultSet.getString("appointment.title");
                String type = resultSet.getString("appointment.type");
                
                Timestamp startTsUTC = resultSet.getTimestamp("appointment.start");
                ZonedDateTime startZdtUTC = startTsUTC.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime startZdtLocal = startZdtUTC.withZoneSameInstant(localZoneId);
                String startZdtLocalString = startZdtLocal.format(dtFormatter);
                
                Timestamp endTime = resultSet.getTimestamp("appointment.end");
                ZonedDateTime endZdtUTC = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime endZdtLocal = endZdtUTC.withZoneSameInstant(localZoneId);
                String endZdtLocalString = endZdtLocal.format(dtFormatter);
                
                int userId = resultSet.getInt("appointment.userId");
                String customerName = resultSet.getString("customer.customerName");
                String userName = resultSet.getString("appointment.createdBy");
                appts.add(new Appointment(id, title, type, startZdtLocalString, endZdtLocalString, userId, customerId, customerName, userName));
            }
        } catch (SQLException e) {
            System.out.print("Error with SQL statement in AppointmentDao.getCurrUserAppts()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appts;
    }
    
    /**
     * Retrieve all appointments for all users.
     * The result is used to detect overlap between a new appointment and existing appointments.
     * @return 
     */
    public ObservableList<Appointment> getAllUserAppts() {
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        try{           
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT appointment.appointmentId, appointment.title, appointment.type, appointment.start, appointment.end, "
                + "appointment.userId, appointment.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment , customer WHERE appointment.customerId = customer.customerId"
            );
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("appointment.appointmentId");
                String title = resultSet.getString("appointment.title");
                String type = resultSet.getString("appointment.type");
                
                Timestamp startTsUTC = resultSet.getTimestamp("appointment.start");
                ZonedDateTime startZdtUTC = startTsUTC.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime startZdtLocal = startZdtUTC.withZoneSameInstant(localZoneId);
                String startZdtLocalString = startZdtLocal.format(dtFormatter);
                
                Timestamp endTime = resultSet.getTimestamp("appointment.end");
                ZonedDateTime endZdtUTC = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime endZdtLocal = endZdtUTC.withZoneSameInstant(localZoneId);
                String endZdtLocalString = endZdtLocal.format(dtFormatter);
                        
                int userId = resultSet.getInt("appointment.userId");
                int customerId = resultSet.getInt("appointment.customerId");
                String customerName = resultSet.getString("customer.customerName");
                String userName = resultSet.getString("appointment.createdBy");
                allAppts.add(new Appointment(id, title, type, startZdtLocalString, endZdtLocalString, userId, customerId, customerName, userName));
            }
        } catch (SQLException e) {
            System.out.print("Error with SQL statement in AppointmentDao.getCurrUserAppts()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allAppts;
    }
    
    /**
     * Add an appointment record to the appointment table
     * @param title
     * @param type
     * @param startZdtUTC
     * @param endZdtUTC
     * @param userId
     * @param userName
     * @param customerId
     * @param customerName 
     */
    public void addAppt(String title, String type, ZonedDateTime startZdtUTC, ZonedDateTime endZdtUTC, int userId, String userName, int customerId, String customerName) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "INSERT INTO appointment " +
                "(appointmentId, customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)"
            );
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setString(3, title);
            ps.setString(4, "");
            ps.setString(5, "");
            ps.setString(6, userName);
            ps.setString(7, type);
            ps.setString(8, "");
            ps.setTimestamp(9, Timestamp.valueOf(startZdtUTC.toLocalDateTime()));
            ps.setTimestamp(10, Timestamp.valueOf(endZdtUTC.toLocalDateTime()));
            ps.setString(11, userName);
            ps.setString(12, userName);
            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Error with SQL statement in AppointmentDao.addAppt()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Update an existing appointment record.
     * @param id
     * @param title
     * @param type
     * @param startZdtUTC
     * @param endZdtUTC
     * @param userId
     * @param userName
     * @param customerId
     * @param customerName 
     */
    public void updateAppt(int id, String title, String type, ZonedDateTime startZdtUTC, ZonedDateTime endZdtUTC, int userId, String userName, int customerId, String customerName) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "UPDATE appointment SET "+ 
                "customerId = ?, userId= ?, title = ?, contact = ?, type = ?, start = ?, end = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? " +
                "WHERE appointmentId = ?"
            );
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setString(3, title);
            ps.setString(4, userName);
            ps.setString(5, type);
            ps.setTimestamp(6, Timestamp.valueOf(startZdtUTC.toLocalDateTime()));
            ps.setTimestamp(7, Timestamp.valueOf(endZdtUTC.toLocalDateTime()));
            ps.setString(8, userName);
            ps.setInt(9, id);
            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Error with SQL statement in AppointmentDao.updateAppt()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Delete an appointment record from the database.
     * @param id 
     */
    public void deleteAppt(int id) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "DELETE FROM appointment WHERE appointmentId = ?"
            );
            ps.setInt(1, id);
            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in AppointmentDao.deleteAppt()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Delete appointments associated with a certain customer.
     * Used before deleting a customer.
     * @param customerId 
     */
    public void deleteApptsOfCustomer(int customerId) {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "DELETE FROM appointment WHERE customerId = ?"
            );
            ps.setInt(1, customerId);
            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in AppointmentDao.deleteAppt()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the amount of appointments for each type and month.
     * Used for report.
     * @return 
     */
    public ObservableList<ApptReport> getApptsByMonthType() {
        ObservableList<ApptReport> apptsByMonthType = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT MONTHNAME(`start`) AS \'month\', type, COUNT(*) AS \'amount\' " +
                "FROM appointment GROUP BY MONTHNAME(`start`), type"
            );
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String month = resultSet.getString("month");
                String type = resultSet.getString("type");
                String amount = resultSet.getString("amount");
                apptsByMonthType.add(new ApptReport(month, type, amount));
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in AppointmentDao.getApptsByMonthType()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apptsByMonthType;
    }
}
