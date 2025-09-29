/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmagenta.dao;

/**
 * Clase utilitaria para gestionar la conexión a la base de datos MySQL Cine_DB.
 * Proporciona métodos estáticos para obtener, cerrar y probar la conexión.
 * @author Alex Fernandez
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Cine_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "Admin";

    /**
     * Obtiene una conexión a la base de datos Cine_DB usando JDBC.
     * Carga el driver de MySQL si es necesario.
     * @return una instancia de Connection si es exitosa, null si falla
     */
    public static Connection getConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos Cine_DB");
            return conn;
        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo conectar a la base de datos. Por favor, verifica que el servidor esté activo y que los datos de acceso sean correctos.\nDetalles técnicos: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] No se encontró el controlador de MySQL. Por favor, asegúrate de que el conector JDBC esté instalado.\nDetalles técnicos: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cierra una conexión abierta a la base de datos.
     * @param conn la conexión a cerrar
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión cerrada exitosamente");
            } catch (SQLException e) {
                System.out.println("[ADVERTENCIA] Ocurrió un problema al cerrar la conexión con la base de datos.\nDetalles técnicos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Prueba la conexión a la base de datos Cine_DB.
     * @return true si la conexión es exitosa, false si falla
     */
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            closeConnection(conn);
            return true;
        }
        return false;
    }
}
