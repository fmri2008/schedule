/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import utils.*;
import view_controller.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.User;

/**
 *
 * @author Dawei Li
 */
public class Schedule extends Application {
    
    private Stage primaryStage;
    private static Connection conn;
    private ResourceBundle rb;
    
    @Override
    public void start(Stage stage) throws Exception {
//        Locale.setDefault(new Locale("cn"));
        this.primaryStage = stage;
        this.primaryStage.setTitle("Schedule App");
        rb = ResourceBundle.getBundle("utils/lang", Locale.getDefault());
        showLoginScreen();
    }

    public ResourceBundle getRb() {
        return this.rb;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DBConnection.startConnection();
        conn = DBConnection.getDBConnection();
        System.out.println("Connection successful.");
        
        DBQuery.setStatement(conn); // Create Statement object
        Statement statement = DBQuery.getStatement(); // Get Statement reference
        
        launch(args);
        DBConnection.closeConnection();
    }

    /**
     * Show the login screen.
     */
    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Schedule.class.getResource("/view_controller/Login.fxml"));
            
            AnchorPane loginScreen = loader.load();
            LoginController controller = loader.getController();
            controller.setup(this);
            
            Scene scene = new Scene(loginScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** Show the appointment screen.
     * @param schedule
     * @param user 
     */
    public void showAppointmentScreen(Schedule schedule, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Schedule.class.getResource("/view_controller/Appointment.fxml"));
            
            AnchorPane appointmentScreen = loader.load();
            AppointmentController controller = loader.getController();
            controller.setup(schedule, user);
            
            Scene scene = new Scene(appointmentScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Show the appointment change screen.
     * @param schedule
     * @param user
     * @param appt 
     */
    public void showChangeAppointmentScreen(Schedule schedule, User user, Appointment appt) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Schedule.class.getResource("/view_controller/ChangeAppointment.fxml"));
            
            AnchorPane changeAppointmentScreen = loader.load();
            ChangeAppointmentController controller = loader.getController();
            controller.setup(schedule, user, appt);
            
            Scene scene = new Scene(changeAppointmentScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showChangeAppointmentScreen(Schedule schedule, User user) {
        this.showChangeAppointmentScreen(schedule, user, null);
    }
    
    /**
     * Show the customer screen.
     * @param schedule
     * @param user 
     */
    public void showCustomerScreen(Schedule schedule, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Schedule.class.getResource("/view_controller/Customer.fxml"));
            
            AnchorPane customerScreen = loader.load();
            CustomerController controller = loader.getController();
            controller.setup(schedule, user);
            
            Scene scene = new Scene(customerScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Show the customer change screen.
     * @param schedule
     * @param user
     * @param customer 
     */
    public void showChangeCustomerScreen(Schedule schedule, User user, Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Schedule.class.getResource("/view_controller/ChangeCustomer.fxml"));
            
            AnchorPane changeCustomerScreen = loader.load();
            ChangeCustomerController controller = loader.getController();
            controller.setup(schedule, user, customer);
            
            Scene scene = new Scene(changeCustomerScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showChangeCustomerScreen(Schedule schedule, User user) {
        this.showChangeCustomerScreen(schedule, user, null);
    }
    
    /**
     * Show the report screen.
     * @param schedule
     * @param user 
     */
    public void showReportScreen(Schedule schedule, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Schedule.class.getResource("/view_controller/Report.fxml"));
            
            AnchorPane reportScreen = loader.load();
            ReportController controller = loader.getController();
            controller.setup(schedule, user);
            
            Scene scene = new Scene(reportScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
