/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import dao.AppointmentDao;
import dao.CustomerDao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Customer;
import model.User;
import schedule.Schedule;

/**
 * FXML Controller class
 *
 * @author Dawei Li
 */
public class ChangeAppointmentController {

    private Schedule schedule;
    private User user;
    private Appointment selectedAppt;
    private ObservableList<Appointment> allAppts;
    private DateTimeFormatter dtFormatter;
    private ObservableList<String> types;
    private ObservableList<Customer> customers;
    private ObservableList<LocalTime> timeSlots;
        
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TextField idField;
    @FXML private TextField titleField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private Button saveApptButton;
    @FXML private Button cancelApptButton;

    public void setup(Schedule schedule, User user, Appointment appt) {
        this.schedule = schedule;
        this.user = user;
        this.selectedAppt = appt;
        AppointmentDao dao = new AppointmentDao();
        allAppts = dao.getAllUserAppts();
        this.dtFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        
        setupFields();
        if (selectedAppt != null) {
            showAppt();
        }
    }
    
    /**
     * Setup all fields to default values
     */
    private void setupFields() {
        types = FXCollections.observableArrayList();
        types.addAll("Consultation", "Troubleshooting", "Support", "Other");
        typeComboBox.setItems(types);
        
        // customer table
        CustomerDao dao = new CustomerDao();
        customers = dao.getCustomers();
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTable.getItems().setAll(customers);
        
        datePicker.setValue(LocalDate.now());
        // time comboboxes are limit to 8 am - 17 pm local time.
        LocalTime t = LocalTime.of(8, 0);
        timeSlots = FXCollections.observableArrayList();
        while (!t.equals(LocalTime.of(17, 30))) {
            timeSlots.add(t);
            t = t.plusMinutes(30);
        }
        startTimeComboBox.setItems(timeSlots);
        endTimeComboBox.setItems(timeSlots);
    }
    
    /** 
     * For "Edit", show the existing information of the selected appointment.
     */
    private void showAppt() {
        idField.setText(String.valueOf(selectedAppt.getId()));
        titleField.setText(selectedAppt.getTitle());
        typeComboBox.setValue(selectedAppt.getType());
        datePicker.setValue(LocalDateTime.parse(selectedAppt.getStartDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalDate());
        startTimeComboBox.getSelectionModel().select(LocalDateTime.parse(selectedAppt.getStartDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalTime());
        endTimeComboBox.getSelectionModel().select(LocalDateTime.parse(selectedAppt.getEndDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalTime());
    }

    @FXML
    private void handleSaveAppt(ActionEvent event) {
        String title = titleField.getText();
        String type = typeComboBox.getSelectionModel().getSelectedItem();
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        LocalDate localDate = datePicker.getValue();
        LocalTime localStartTime = startTimeComboBox.getSelectionModel().getSelectedItem();
        LocalTime localEndTime = endTimeComboBox.getSelectionModel().getSelectedItem(); 
        
        // validate against empty inputs
        String message = validateInput(title, type, customer, localDate, localStartTime, localEndTime);
        if (message.length() != 0) {
            showAlert(message);
            return;
        }
        
        LocalDateTime startLdt = LocalDateTime.of(localDate, localStartTime);
        LocalDateTime endLdt = LocalDateTime.of(localDate, localEndTime);
        int customerId = customer.getId();
        String customerName = customer.getName();
        int userId = user.getId();
        String userName = user.getName();
        
        // validate time interval
        message += validateTime(userId, customerId, localDate, localStartTime, localEndTime, startLdt, endLdt);
        if (message.length() != 0) {
            showAlert(message);
            return;
        }
        
        // For legal time interval, convert to UTC to be saved to the database.
        ZonedDateTime startZdtLocal = startLdt.atZone(ZoneId.systemDefault());
        ZonedDateTime endZdtLocal = endLdt.atZone(ZoneId.systemDefault());
        ZonedDateTime startZdtUTC = startZdtLocal.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endZdtUTC = endZdtLocal.withZoneSameInstant(ZoneId.of("UTC"));

        AppointmentDao dao = new AppointmentDao();
        // Create a new appointment if no appointment was selected. 
        // Otherwise update the selected appointment.
        if (selectedAppt == null) {
            dao.addAppt(title, type, startZdtUTC, endZdtUTC, userId, userName, customerId, customerName);
        } else {
            int id = Integer.valueOf(idField.getText());
            dao.updateAppt(id, title, type, startZdtUTC, endZdtUTC, userId, userName, customerId, customerName);
        }
        schedule.showAppointmentScreen(schedule, user);
    }

    @FXML
    private void handleCancelAppt(ActionEvent event) {
        schedule.showAppointmentScreen(schedule, user);
    }
    
    private String validateInput(String title, String type, Customer customer, LocalDate localDate,
            LocalTime localStartTime, LocalTime localEndTime) {
        String message = "";
        if (title.length() == 0) {
            message += "title is empty.\n";
        }
        if (type == null) {
            message += "type is empty.\n";
        }
        if (customer == null) {
            message += "customer is empty.\n";
        }
        if (localDate == null) {
            message += "date is empty.\n";
        }
        if (localStartTime == null) {
            message += "start time is empty.\n";
        }
        if (localEndTime == null) {
            message += "end time is empty.\n";
        }
        
        return message;
    }
    
    private String validateTime(int userId, int customerId, LocalDate localDate, LocalTime localStartTime, LocalTime localEndTime, LocalDateTime startLdt, LocalDateTime endLdt) {
        String message = "";
        
        // date cannot be a previous date
        if (localDate.isBefore(LocalDate.now())) {
            message += "The entered date is a previous date.\n";
        }
        
        // end time cannot be earlier than start time
        if (localStartTime.isAfter(localEndTime)) {
            message += "The entered end time is not after the entered start time.\n";
        }
        
        // appointment cannot overlap with existing appointments of the same user or customer
        for (Appointment appt : allAppts) {
            LocalDateTime currApptStartLdt = LocalDateTime.parse(appt.getStartDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime currApptEndLdt = LocalDateTime.parse(appt.getEndDT(), dtFormatter).atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (startLdt.isBefore(currApptEndLdt) && endLdt.isAfter(currApptStartLdt)) {
                if (appt.getCustomerId() == customerId && appt.getUserId() == userId) {
                    message += "The entered appointment conflicts with an existing appointment of the same user and same customer.\n";
                } else if (appt.getCustomerId() == customerId) {
                    message += "The entered appointment conflicts with an existing appointment of the same customer.\n";
                } else if (appt.getUserId() == userId) {
                    message += "The entered appointment conflicts with an existing appointment of the same user.\n";
                }
            }
        }
        return message;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
        return;
    }
}
