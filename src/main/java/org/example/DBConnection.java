package org.example;

import java.sql.*;

public class DBConnection {
    private final String url;
    private final String user;
    private final String pass;


    public DBConnection(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection Connect(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(this.url, this.user, this.pass);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;

    }

}