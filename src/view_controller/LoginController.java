/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import dao.AppointmentDao;
import dao.UserDao;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.TimeZone;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import model.Appointment;
import model.User;
import schedule.*;
import utils.Log;

/**
 * FXML Controller class
 *
 * @author Dawei Li
 */
public class LoginController{
    private DateTimeFormatter dtFormatter;
    
    private Schedule schedule;
    private User user;
    private ObservableList<Appointment> appts;
    
    @FXML private TextField userText;
    @FXML private TextField passwordText;
    @FXML private Label timezoneLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button loginBtn;
    @FXML private Button cancelBtn;
    
    public LoginController() {
        this.user = new User();
        this.dtFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    }
    
    /**
     * Initialize the login screen
     * @param schedule 
     */
    public void setup(Schedule schedule) {
        this.schedule = schedule;
        
        String timeZone = TimeZone.getDefault().getDisplayName();
        timezoneLabel.setText(schedule.getRb().getString("timezone") + timeZone);
        usernameLabel.setText(schedule.getRb().getString("username"));
        passwordLabel.setText(schedule.getRb().getString("password"));
        loginBtn.setText(schedule.getRb().getString("login"));
        cancelBtn.setText(schedule.getRb().getString("cancel"));
    }

    @FXML
    private void handleLoginAction(ActionEvent event) throws IOException {
        String userName = userText.getText();
        String password = passwordText.getText();
        if (userName.length() == 0 || password.length() == 0) {
            showMessage(Alert.AlertType.INFORMATION, schedule.getRb().getString("login_empty_title"), schedule.getRb().getString("login_empty"));
        } else {
            user = verifyUser(userName, password);
            if (user == null) {
                showMessage(Alert.AlertType.INFORMATION, schedule.getRb().getString("login_failure_title"), schedule.getRb().getString("login_failure"));
                return;
            }
            retrieveAppts();
            showAlert();
            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime zdtUTC = now.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
            logTimestamp("User id " + String.valueOf(user.getId()) + " name " + user.getName() + " logged in at time " + zdtUTC + "\n");
            showMessage(Alert.AlertType.INFORMATION, schedule.getRb().getString("login_success_title"), schedule.getRb().getString("login_success"));
            this.schedule.showAppointmentScreen(schedule, user);
        }
    }
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(schedule.getRb().getString("quit_title"));
        alert.setHeaderText(schedule.getRb().getString("quit"));
        alert.showAndWait()
        .filter(res -> res == ButtonType.OK)
        .ifPresent(res -> {
            Platform.exit();
            System.exit(0);
        });
    }
    
    private User verifyUser(String userName, String password) {
        UserDao userDao = new UserDao();
        return userDao.getUser(userName, password);
    }
    
    /** Provide an alert if there is an appointment within 15 minutes of the user’s log-in.
     */
    private void showAlert() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime within15Min = now.plusMinutes(15);
        for (Appointment appt : appts) {
            LocalDateTime localStartDT = LocalDateTime.parse(appt.getStartDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (localStartDT.isAfter(now) && localStartDT.isBefore(within15Min)) {
                showMessage(Alert.AlertType.INFORMATION, schedule.getRb().getString("appointment_reminder_title"), schedule.getRb().getString("appointment_reminder"));
                return;
            }
        }
    }
    
    /** Retrieve from the database all appointments for the current user.
     */
    private void retrieveAppts() {
        AppointmentDao apptDao = new AppointmentDao();
        appts = apptDao.getUserAppts(user.getId());
    }
    
    private void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    
    private void logTimestamp(String message) throws IOException {
        Log.logTimestamp(message);
    }
}
