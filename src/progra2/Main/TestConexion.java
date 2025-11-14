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
        System.out.println("Probando conexion a la base de datos...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexion exitosa!\n");
                
                // Información de la conexión
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Informacion de la conexion:");
                System.out.println("- Usuario conectado: " + meta.getUserName());
                System.out.println("- Base de datos: " + conn.getCatalog());
                System.out.println("- URL: " + conn.getMetaData().getURL());
                System.out.println("- Driver: " + meta.getDriverName() + " v" + meta.getDriverVersion());
            }
        } catch (SQLException e) {
            System.out.println("Resultado: No se pudo conectar a la base de datos.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
    
}
