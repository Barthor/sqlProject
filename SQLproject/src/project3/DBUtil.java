/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author abmat
 */
public class DBUtil
{
    private String JDBC_DRIVER;
    private String DB_URL;
    
    //Connection
    private Connection conn = null;
    
    private String USER;
    private String PASSW;
    
    public void setDriver(String driver) {
        this.JDBC_DRIVER = driver;
    }
    public String getDriver() {
        return this.JDBC_DRIVER;
    }
    public void setDbURL(String url) {
        this.DB_URL = url;
    }
    public String getDbURL() {
        return this.DB_URL;
    }
    public void setUser(String name) {
        this.USER = name;
    }
    public String getUser() {
        return this.USER;
    }
    public void setPassw(String passw) {
        this.PASSW = passw;
    }
    public String getPassw() {
        return this.PASSW;
    }
    public Connection getConnection() {
        return this.conn;
    }
    //Establlish Connection to selected database
    public void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(this.getDriver());
        } catch (ClassNotFoundException e) {
            System.out.println("No Driver");
            throw e;
        }
        
        try {
            this.conn = DriverManager.getConnection(this.getDbURL(), this.getUser(), this.getPassw());
        } catch (SQLException e) {
            
            System.out.println("Connection Failed" + e);
            throw e;
        }
    }
    
    //Disconnect from Database
    public void dbDisconnect() throws SQLException {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }
    
    
}
