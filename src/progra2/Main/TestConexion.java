package progra2.Main;

import progra2.Config.DatabaseConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Clase de prueba para verificar la conexión con la base de datos.
 * Si la conexión es exitosa, muestra los metadatos de la misma.
 * Utiliza la configuración definida en DatabaseConnection.
 */
public class TestConexion {
    
    public static void main(String[] args){
        System.out.println("Probando conexion a la base de datos...\n");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexion exitosa!");
                
                // Información de la base de datos
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("- Usuario conectado: " + meta.getUserName());
                System.out.println("- Base de datos: " + conn.getCatalog());
                System.out.println("- URL: " + conn.getMetaData().getURL());
                System.out.println("- Driver: " + meta.getDriverName() + " v" + meta.getDriverVersion());
            }
        } catch (SQLException e) {
            System.out.println("Error de conexion!");
            System.out.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
