package com.example.sales_management_system_with_gst_return2;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    String db = "salesmanagementdata";
    String username = "root";
    String password = "Spoon@63";

    public Connection CONN() {
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionString = "jdbc:mysql://localhost/"+db;
            conn = DriverManager.getConnection(connectionString, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

}
