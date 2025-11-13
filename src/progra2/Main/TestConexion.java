package progra2.Main;

import progra2.Config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

/** JAVADOC AQUÍ */
public class TestConexion {
    
    public static void main(String[] args){
        System.out.println("Probando conexion a la base de datos...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexion exitosa!");
                System.out.println("Base de datos: " + conn.getCatalog());
                System.out.println("URL: " + conn.getMetaData().getURL());
                conn.close();
                System.out.println("Conexion cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error de conexion!");
            System.out.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
