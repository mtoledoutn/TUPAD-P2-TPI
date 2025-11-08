/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author martin
 */
public final class DatabaseConnection {
    /** URL de conexión JDBC. Configurable via -Ddb.url */
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/dbtpi3");

    /** Usuario de la base de datos. Configurable via -Ddb.user */
    private static final String USER = System.getProperty("db.user", "root");

    /** Contraseña del usuario. Configurable via -Ddb.password */
    private static final String PASSWORD = System.getProperty("db.password", "");

    
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
    
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada");
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    
    private static void validateConfiguration() {
        if (URL == null || URL.trim().isEmpty()) {
            throw new IllegalStateException("La URL de la base de datos no está configurada");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new IllegalStateException("El usuario de la base de datos no está configurado");
        }
        // PASSWORD puede ser vacío (común en MySQL local con usuario root sin contraseña)
        // Solo validamos que no sea null
        if (PASSWORD == null) {
            throw new IllegalStateException("La contraseña de la base de datos no está configurada");
        }
    }
}
