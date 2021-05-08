/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.*;

/**
 *
 * @author Dawei Li
 */
public class DBQuery {
    
    private static Statement statement;
    
    /** Create Statement object
     * @param conn
     * @throws SQLException 
     */
    public static void setStatement(Connection conn) throws SQLException{
        statement = conn.createStatement();
    }
    
    /** Return Statement object
     * @return Statement
     */
    public static Statement getStatement() {
        return statement;
    }
}
