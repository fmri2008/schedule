/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dawei Li
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postCode;
    private String city;
    private String country;
    private String phone;
    
    public Customer(int id, String name, String address, String postCode, String city, String country, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.city = city;
        this.country = country;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }
    
    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }
    
}
