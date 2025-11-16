package progra2.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria singleton para gestionar conexiones JDBC a MySQL.
 * Encapsula la configuración de la base de datos y proporciona
 * un punto centralizado para obtener conexiones.
 * 
 * La configuración puede ser sobrescrita mediante propiedades del sistema:
 * - Ddb.url: URL de conexión JDBC
 * - Ddb.user: Usuario de la base de datos
 * - Ddb.password: Contraseña del usuario
 */
public final class DatabaseConnection {
    
    /** URL de conexión JDBC. Configurable via -Ddb.url */
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/dbtpi3");

    /** Usuario de la base de datos. Configurable via -Ddb.user */
    private static final String USER = System.getProperty("db.user", "root");

    /** Contraseña del usuario. Configurable via -Ddb.password */
    private static final String PASSWORD = System.getProperty("db.password", "");
    
    /**
     * Bloque estático que carga el driver JDBC y valida la configuración.
     * Se ejecuta al cargar la clase por primera vez.
     * 
     * @throws ExceptionInInitializerError si el driver no se encuentra o la configuración es inválida
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            validateConfiguration();
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Error: No se encontro el driver de base de datos " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError("Error en la configuracion de la base de datos " + e.getMessage());
        }
    }
    
    /**
     * Constructor privado para prevenir instanciación.
     * Esta clase solo debe usarse de forma estática.
     * 
     * @throws UnsupportedOperationException siempre que se intente instanciar
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada");
    }
    
    /**
     * Obtiene una nueva conexión a la base de datos.
     * Cada llamada genera una conexión independiente que debe cerrarse manualmente.
     * 
     * @return conexión JDBC activa a la base de datos
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Valida que los parámetros de configuración sean válidos.
     * Verifica que URL y USER no estén vacíos, y que PASSWORD no sea null.
     * 
     * @throws IllegalStateException si algún parámetro es inválido
     */
    private static void validateConfiguration() {
        if (URL == null || URL.trim().isEmpty()) {
            throw new IllegalStateException("La URL de la base de datos no esta configurada");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new IllegalStateException("El usuario de la base de datos no esta configurado");
        }
        // PASSWORD puede ser vacío (común en MySQL local con usuario root sin contraseña)
        // Solo validamos que no sea null
        if (PASSWORD == null) {
            throw new IllegalStateException("La contraseña de la base de datos no esta configurada");
        }
    }
    
}
