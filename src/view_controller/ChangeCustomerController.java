/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import dao.AddressDao;
import dao.CityDao;
import dao.CustomerDao;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Customer;
import model.User;
import schedule.Schedule;

/**
 * FXML Controller class
 *
 * @author Dawei Li
 */public class ChangeCustomerController {

     private Schedule schedule;
     private User user;
     private Customer selectedCustomer;
     
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField postCodeField;
    @FXML private ComboBox<String> cityComboBox;
    @FXML private ComboBox<String> countryComboBox;
    @FXML private Button saveCustomerBtn;
    @FXML private Button cancelCustomerBtn;

    /**
     * Called when rendering this screen
     * @param schedule
     * @param user
     * @param customer 
     */
    public void setup(Schedule schedule, User user, Customer customer) {
        this.schedule = schedule;
        this.user = user;
        this.selectedCustomer = customer;
        
        setupFields();
        if (selectedCustomer != null) {
            showCustomer();
        }
    }  

    /**
     * Setup all fields to default values
     */
    private void setupFields() {
        CityDao dao = new CityDao();
        ObservableList<String> cityNames = dao.getCityNames();
        cityComboBox.setItems(cityNames);
        
        // the items of countryComboBox are determined by the value of cityComboBox
        cityComboBox.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            if(newvalue != null) {
                ObservableList<String> countryNames = dao.getCountryNames(newvalue);
                countryComboBox.setItems(countryNames);
            }
        });
    }
    
    /** 
     * For "Edit", show the existing information of the selected customer.
     */
    private void showCustomer() {
        idField.setText(String.valueOf(selectedCustomer.getId()));
        nameField.setText(selectedCustomer.getName());
        phoneField.setText(selectedCustomer.getPhone());
        addressField.setText(selectedCustomer.getAddress());
        postCodeField.setText(selectedCustomer.getPostCode());
        cityComboBox.setValue(selectedCustomer.getCity());
        countryComboBox.setValue(selectedCustomer.getCountry());
    }
    
    @FXML
    private void handleSaveCustomer(ActionEvent event) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String postCode = postCodeField.getText();
        String city = cityComboBox.getSelectionModel().getSelectedItem();
        String country = countryComboBox.getSelectionModel().getSelectedItem();
        
        if (!inputIsValid(name, phone, address, postCode, city, country)) {
            return;
        }
        
        CityDao cityDao = new CityDao();
        ArrayList<String> fullAddresses = cityDao.getAllFullAddresses();
        String thisAddress = address + " " + city + " " + country;
        int addressId = -1;
        // If address + city + country does not exist in the address database, create a new address.
        if (!fullAddresses.contains(thisAddress)) {
            // Retrieve cityId based on the country and city selections.
            // We cannot decide cityId by city only, because the city table may include 
            // cities with the same name but in different countries and thus with different cityId.
            int cityId = cityDao.getCityId(city, country);
            // Add a new address
            AddressDao addressDao = new AddressDao();
            addressDao.addAddress(address, cityId, postCode, phone, user.getName());
        }
        addressId = cityDao.getAddressId(address, city, country);
        System.out.println("addressId = " + addressId);
        
        CustomerDao customerDao = new CustomerDao();
        if (selectedCustomer == null) {
           customerDao.addCustomer(name, addressId, user.getName());
        } else {
           customerDao.updateCustomer(selectedCustomer.getId(), name, addressId, user.getName());
        }
        schedule.showCustomerScreen(schedule, user);
    }

    @FXML
    private void handleCancelCustomer(ActionEvent event) {
        this.schedule.showCustomerScreen(schedule, user);
    }
    
    private boolean inputIsValid(String name, String phone, String address, String postCode, String city, String country) {
        String message = "";
        if (name.length() == 0) {
            message += "name is empty.\n";
        }
        if (phone.length() == 0) {
            message += "phone is empty.\n";
        }
        if (address.length() == 0) {
            message += "address is empty.\n";
        }
        if (postCode.length() == 0) {
            message += "postal code is empty.\n";
        }
        if (city == null) {
            message += "city is empty.\n";
        }
        if (country == null) {
            message += "country is empty.\n";
        }
        if (message.length() == 0) {
            return true;
        } 
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();    
        return false;
    }
}
