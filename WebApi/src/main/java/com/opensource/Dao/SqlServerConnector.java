package com.opensource.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlServerConnector {

    private final static String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=ChallengeOneDB;integratedSecurity=false;";

    public static Connection getConnection() {
        try {

            Connection cnn = DriverManager.getConnection(connectionString, "sa", "sa1234");
            return cnn;
        } catch (SQLException ex) {
            return null;
        }

    }
}
