/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import dao.AppointmentDao;
import dao.CustomerDao;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.User;
import schedule.Schedule;

/**
 * FXML Controller class
 *
 * @author Dawei Li
 */
public class CustomerController {

    private Schedule schedule;
    private User user;
    private ObservableList<Customer> customers;
    
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<?, ?> colId;
    @FXML private TableColumn<?, ?> colName;
    @FXML private TableColumn<?, ?> colAddress;
    @FXML private TableColumn<?, ?> colPostCode;
    @FXML private TableColumn<?, ?> colCity;
    @FXML private TableColumn<?, ?> colCountry;
    @FXML private TableColumn<?, ?> colPhone;
    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button ApptsBtn;
    @FXML private Button reportsBtn;

    public void setup(Schedule schedule, User user) {
        this.schedule = schedule;
        this.user = user;
        setupCustomers();
    }
    
    /** 
     * Retrieve all customer data from the database.
     * Populate the customer table.
     */
    private void setupCustomers() {
        CustomerDao customerDao = new CustomerDao();
        customers = customerDao.getCustomers();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPostCode.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        customerTable.getItems().setAll(customers);
    }
    
    @FXML
    private void handleCreateCustomer(ActionEvent event) {
        this.schedule.showChangeCustomerScreen(schedule, user);
    }

    @FXML
    private void handleDeleteCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Please confirm");
            alert.setHeaderText("Are you sure that you want to delete the selected customer and all associated appointments?");
            alert.showAndWait()
            .filter(res -> res == ButtonType.OK)
            .ifPresent(res -> {
                // Before deleting the customer, delete all associated appointments from the appointment table.
                AppointmentDao apptDao = new AppointmentDao();
                apptDao.deleteApptsOfCustomer(selectedCustomer.getId());

                CustomerDao customerDao = new CustomerDao();
                customerDao.deleteCustomer(selectedCustomer.getId());
                setupCustomers();
            });
        }
    }

    @FXML
    private void handleEditCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            this.schedule.showChangeCustomerScreen(schedule, user, selectedCustomer);
        }
    }

    @FXML
    private void handleSelectAppts(ActionEvent event) {
        this.schedule.showAppointmentScreen(schedule, user);
    }

    @FXML
    private void handleSelectReports(ActionEvent event) {
        this.schedule.showReportScreen(schedule, user);
    }
    
}
