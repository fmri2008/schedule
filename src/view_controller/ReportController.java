/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import dao.AppointmentDao;
import dao.CustomerDao;
import dao.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.ApptReport;
import model.Customer;
import model.User;
import schedule.Schedule;

/**
 * FXML Controller class
 *
 * @author fmri2
 */
public class ReportController {

    private Schedule schedule;
    private User user;
    private ObservableList<ApptReport> apptReports;
    private ObservableList<Appointment> apptOneUser;
    private ObservableList<User> users;
    private ObservableList<Customer> customers;
    private ObservableList<Appointment> apptOneCustomer;
    
    @FXML private TabPane tabPane;
    
    @FXML private Tab apptReportTab;
    @FXML private TableView<ApptReport> apptReportTable;
    @FXML private TableColumn<?, ?> colMonth;
    @FXML private TableColumn<?, ?> colApptType;
    @FXML private TableColumn<?, ?> colAmount;
    
    @FXML private Tab scheduleReportTab;
    @FXML private ComboBox<String> userComboBox;
    @FXML private TableView<Appointment> apptTable;
    @FXML private TableColumn<?, ?> colTitle;
    @FXML private TableColumn<?, ?> colType;
    @FXML private TableColumn<?, ?> colCustomer;
    @FXML private TableColumn<?, ?> colStart;
    @FXML private TableColumn<?, ?> colEnd;
    
    @FXML private Tab CustomerReportTab;
    @FXML private ComboBox<String> customerComboBox;
    @FXML private TableView<Appointment> apptTableCustomerTab;
    @FXML private TableColumn<?, ?> colTitleCustomerTab;
    @FXML private TableColumn<?, ?> colTypeCustomerTab;
    @FXML private TableColumn<?, ?> colUserCustomerTab;
    @FXML private TableColumn<?, ?> colStartCustomerTab;
    @FXML private TableColumn<?, ?> colEndCustomerTab;
    
    @FXML private Button ApptsBtn;
    @FXML private Button customerBtn;
    
    public void setup(Schedule schedule, User user) {
        this.schedule = schedule;
        this.user = user;
        
        setupApptReportTab();
        setupScheduleReportTab();
        setupCustomerReportTab();
    }
    
    private void setupApptReportTab() {
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colApptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        AppointmentDao dao = new AppointmentDao();
        apptReports = dao.getApptsByMonthType();
        apptReportTable.getItems().setAll(apptReports);
    }
    
    private void setupScheduleReportTab() {
        // Setup user ComboBox
        UserDao userDao = new UserDao();
        users = userDao.getAllUsers();
        ObservableList<String> userNames = FXCollections.observableArrayList();
        for (User user : users ){
            userNames.add(user.getName());
        }
        userComboBox.setItems(userNames);
        
        // Populate the appointment table based on the selection in the user ComboBox
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDT"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDT"));
        
        userComboBox.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue != null) {
                int userId = userDao.getUserId(userComboBox.getValue());
                AppointmentDao apptDao = new AppointmentDao();
                apptOneUser = apptDao.getUserAppts(userId);
                apptTable.getItems().setAll(apptOneUser);
            }
        });
    }
    
    /**
     * The customer report shows all appointments of a selected customer.
     */
    private void setupCustomerReportTab() {
        // Setup customer ComboBox
        CustomerDao customerDao = new CustomerDao();
        customers = customerDao.getCustomers();
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (Customer customer : customers ){
            customerNames.add(customer.getName());
        }
        customerComboBox.setItems(customerNames);
        
        // Populate the appointment table based on the selection in the customer ComboBox
        colTitleCustomerTab.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTypeCustomerTab.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUserCustomerTab.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colStartCustomerTab.setCellValueFactory(new PropertyValueFactory<>("startDT"));
        colEndCustomerTab.setCellValueFactory(new PropertyValueFactory<>("endDT"));
        
        customerComboBox.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue != null) {
                int customerId = customerDao.getCustomerId(customerComboBox.getValue());
                AppointmentDao apptDao = new AppointmentDao();
                apptOneCustomer = apptDao.getCustomerAppts(customerId);
                apptTableCustomerTab.getItems().setAll(apptOneCustomer);
            }
        });
    }
        
    @FXML
    private void handleSelectAppts(ActionEvent event) {
        this.schedule.showAppointmentScreen(schedule, user);
    }

    @FXML
    private void handleSelectReports(ActionEvent event) {
        this.schedule.showCustomerScreen(schedule, user);
    }
}
