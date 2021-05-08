/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import dao.AppointmentDao;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.User;
import schedule.Schedule;

/**
 * FXML Controller class
 *
 * @author Dawei Li
 */
public class AppointmentController {

    private Schedule schedule;
    private User user;
    private ObservableList<Appointment> appts;
    private DateTimeFormatter dtFormatter;
    
    @FXML private TableView<Appointment> apptsTable;
    @FXML private TableColumn<Appointment, Integer> colId;
    @FXML private TableColumn<Appointment, String> colTitle;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, String> colStart;
    @FXML private TableColumn<Appointment, String> colEnd;
    @FXML private TableColumn<Appointment, String> colCustomerName;
    @FXML private TableColumn<Appointment, String> colUserName;
    @FXML private RadioButton monthToggle;
    @FXML private RadioButton weekToggle;
    @FXML private RadioButton allToggle;
    @FXML private ToggleGroup toggleGroup;
    
    public void setup(Schedule schedule, User user) {
        this.schedule = schedule;
        this.user = user;
        this.dtFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        
        setupAppts();
    }
    
    /**
     * Retrieve appointments of the current user from the database.
     * Populate the appointment table.
     */
    private void setupAppts() {
        AppointmentDao dao = new AppointmentDao();
        appts = dao.getUserAppts(user.getId());
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDT"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDT"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        
        apptsTable.getItems().setAll(appts);
        
        toggleGroup = new ToggleGroup();
        monthToggle.setToggleGroup(toggleGroup);
        weekToggle.setToggleGroup(toggleGroup);
        allToggle.setToggleGroup(toggleGroup);
    }
    
    @FXML
    private void handleCreateAppt(ActionEvent event) {
        this.schedule.showChangeAppointmentScreen(schedule, user);
    }
    
    @FXML
    private void handleEditAppt(ActionEvent event) {
        Appointment selectedAppt = apptsTable.getSelectionModel().getSelectedItem();
        if (selectedAppt != null) {
            schedule.showChangeAppointmentScreen(schedule, user, selectedAppt);
        }
    }
    
    @FXML
    private void handleDeleteAppt(ActionEvent event) {
        Appointment selectedAppt = apptsTable.getSelectionModel().getSelectedItem();
        if (selectedAppt != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Please confirm");
            alert.setHeaderText("Are you sure that you want to delete the selected appointment?");
            
            alert.showAndWait()
            .filter(res -> res == ButtonType.OK)
            .ifPresent(res -> {
                AppointmentDao dao = new AppointmentDao();
                dao.deleteAppt(selectedAppt.getId());
                setupAppts();
            });
        }
    }
    
    @FXML
    private void viewByMonth(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAfter = now.plusMonths(1);
        FilteredList<Appointment> filteredAppts = new FilteredList<>(appts);
        // Use lambda expression to simplify code.
        filteredAppts.setPredicate(appt -> {
            LocalDateTime apptLdt = LocalDateTime.parse(appt.getStartDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalDateTime();
            return apptLdt.isAfter(now.minusDays(1)) && apptLdt.isBefore(oneMonthAfter);
        });
        apptsTable.getItems().setAll(filteredAppts);
    }
    
    @FXML
    private void viewByWeek(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAfter = now.plusWeeks(1);
        FilteredList<Appointment> filteredAppts = new FilteredList<>(appts);
        // Use lambda expression to simplify code.
        filteredAppts.setPredicate(appt -> {
            LocalDateTime apptLdt = LocalDateTime.parse(appt.getStartDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalDateTime();
            return apptLdt.isAfter(now.minusDays(1)) && apptLdt.isBefore(oneWeekAfter);
        });
        apptsTable.getItems().setAll(filteredAppts);
    }
    
    @FXML
    private void viewAll(ActionEvent event) {
        apptsTable.getItems().setAll(appts);
    }
    
    @FXML
    private void handleSelectCustomer(ActionEvent event) {
        this.schedule.showCustomerScreen(schedule, user);
    }
    
    @FXML
    private void handleSelectReports(ActionEvent event) {
        this.schedule.showReportScreen(schedule, user);
    }
}
