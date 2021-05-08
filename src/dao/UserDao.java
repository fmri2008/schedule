/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utils.DBConnection;

/**
 *
 * @author Dawei Li
 */
public class UserDao {
    
    private ObservableList<User> users;
    
    public UserDao() {
        users = FXCollections.observableArrayList();
    }
    
    /**
     * Get users associated with a certain name and password.
     * Return null if no such combination exists.
     * @param userName
     * @param password
     * @return 
     */
    public User getUser(String userName, String password) {
        User user = new User();
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user.setName(userName);
                user.setPassword(password);
                user.setId(resultSet.getInt("userId"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in UserDao.getUser()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * Get all users from the database.
     * @return 
     */
    public ObservableList<User> getAllUsers() {
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT userId, userName, password FROM user"
            );
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                users.add(new User(userId, userName, password));
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in UserDao.getAllUsers()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Get user id given a user name.
     * @param name
     * @return 
     */
    public int getUserId(String name) {
        int id = -1;
        try {
            PreparedStatement ps = DBConnection.getDBConnection().prepareStatement(
                "SELECT userId FROM user WHERE userName = ?"
            );
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("userId");
            }
        } catch (SQLException e) {
            System.out.println("Error with SQL statement in UserDao.getAllUsers()");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
