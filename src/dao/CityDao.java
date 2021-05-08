/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;
import utils.DBConnection;

/**
 *
 * @author Dawei Li
 */
public class CityDao {
    private ObservableList<City> cities;
    
    public CityDao() {
        this.cities = FXCollections.observableArrayList();
    }
    
    /**
     * Get only city names
     * @return An ObservableList of String (city names)
     */
    public ObservableList<String> getCityNames() {
        ObservableList<String> cityNames = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT city FROM city"
            );
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("city");
                cityNames.add(name);
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CityDao.getCities()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityNames;
    }
    
    /**
     * Get country names of a city name. Because one city name can be shared by multiple
     * countries, this function returns a list of country names.
     * @param String cityName
     * @return ObservableList<String> countryName
     */
    public ObservableList<String> getCountryNames(String cityName) {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT country.country FROM " +
                "city, country WHERE city.countryId = country.countryId AND city.city = ?"
            );
            ps.setString(1, cityName);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String countryName = resultSet.getString("country.country");
                countryNames.add(countryName);
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CityDao.getCountryName()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryNames;
    }
    
    /**
     * Get all full addresses from the database.
     * The full addresses are composed of address + city + country
     * @return ArrayList<String> a list of all full addresses
     */
    public ArrayList<String> getAllFullAddresses() {
        ArrayList<String> fullAddresses = new ArrayList<>();
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT address.address, city.city, country.country " +
                "FROM address, city, country " +
                "WHERE address.cityId = city.cityId AND city.countryId = country.countryId"
            );
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String address = resultSet.getString("address.address");
                String city = resultSet.getString("city.city");
                String country = resultSet.getString("country.country");
                String fullAddress = address + " " + city + " " + country;
                fullAddresses.add(fullAddress);
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CityDao.getAllFullAddresses()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return fullAddresses;
    }
    
    /**
     * Given address, city, and country, return the address id.
     * Prerequisite: the combination of address, city, and country exists in the database.
     * @param address
     * @param city
     * @param country 
     */
    public int getAddressId(String address, String city, String country) {
        int addressId = -1;
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT address.addressId FROM address, city, country " +
                "WHERE address.address = ? AND city.city = ? AND country.country = ?"
            );
            ps.setString(1, address);
            ps.setString(2, city);
            ps.setString(3, country);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            addressId = resultSet.getInt("address.addressId");
        } catch(SQLException e) {
            System.out.println("Error with SQL statement in CityDao.getAddressId()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressId;
    }
    
    /**
     * Given city and country names, return the city id.
     * @param city
     * @param country
     * @return 
     */
    public int getCityId(String city, String country) {
        int cityId = -1;
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT city.cityId FROM city, country " +
                "WHERE city.city = ? AND country.country = ? AND city.countryId = country.countryId"
            );
            ps.setString(1, city);
            ps.setString(2, country);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            cityId = resultSet.getInt("city.cityId");
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in CityDao.getCityId()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityId;
    }
}
