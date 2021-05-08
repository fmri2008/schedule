/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Dawei Li
 */
public class DBConnection {
    
    // JDBC URL
    private static final String PROTOCOL = "JDBC";
    private static final String VENDOR = ":mysql:";
    private static final String IP = "//wgudb.ucertify.com/U05vQc";
    private static final String URL = PROTOCOL + VENDOR + IP;
    
    // JDBC driver and connection interface reference
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    private static final String USERNAME = ""; // username and password are deleted
    private static final String PASSWORD = "";
    
    public static Connection startConnection() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection successful");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void closeConnection() {
        try {
        conn.close();
        System.out.println("Connection closed");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static Connection getDBConnection() {
        return conn;
    }
    
}
