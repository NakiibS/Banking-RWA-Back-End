package com.RWA.rwa.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/rwa_db?useGeneratedKeys=true&generateSimpleParameterMetadata=true";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    public static Connection getConnection()throws SQLException{
        return DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
    }
}
