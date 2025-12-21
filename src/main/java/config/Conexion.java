package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://shinkansen.proxy.rlwy.net:35711/railway?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "sexXqOPUEQlPrMbwgMYjoXWgJllPbAdZ";
    
    // Variable estática para guardar la conexión única
    private static Connection instance;

    public static Connection getConexion() {
        try {
            // Verificamos si la conexión no existe o se cerró
            if (instance == null || instance.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Nueva conexión creada exitosamente.");
            }
            // Si ya existe, devolvemos la misma sin reconectar
            return instance;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}