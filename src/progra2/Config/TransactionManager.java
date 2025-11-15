package progra2.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Administrador de transacciones que encapsula el manejo de commit/rollback.
 * Implementa AutoCloseable para usar con try-with-resources.
 */
public class TransactionManager implements AutoCloseable {
    
    /** JAVADOC AQUÍ */
    private Connection conn;
    /** JAVADOC AQUÍ */
    private boolean transactionActive;
    
    /**
     * Constructor que recibe una conexión ya establecida.
     * @param conn conexión a la base de datos
     * @throws SQLException si la conexión es inválida
     */
    public TransactionManager(Connection conn) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("La conexion no puede ser null");
        }
        this.conn = conn;
        this.transactionActive = false;
    }
    
    /**
     * Obtiene la conexión administrada.
     * @return conexión de base de datos
     */
    public Connection getConnection() {
        return conn;
    }
    
    /**
     * Inicia una transacción deshabilitando autocommit.
     * @throws SQLException si hay error al configurar la conexión
     */
    public void startTransaction() throws SQLException {
        if (conn == null) {
            throw new SQLException("No se puede iniciar la transaccion: conexion no disponible");
        }
        if (conn.isClosed()) {
            throw new SQLException("No se puede iniciar la transaccion: conexion cerrada");
        }
        conn.setAutoCommit(false);
        transactionActive = true;
    }
    
    /**
     * Confirma todos los cambios de la transacción activa.
     * @throws SQLException si hay error al hacer commit
     */
    public void commit() throws SQLException {
        if (conn == null) {
            throw new SQLException("Error al hacer commit: no hay conexion establecida");
        }
        if (!transactionActive) {
            throw new SQLException("No hay una transaccion activa para hacer commit");
        }
        conn.commit();
        transactionActive = false;
    }
    
    /**
     * Revierte todos los cambios de la transacción activa.
     * No lanza excepciones para permitir uso seguro en bloques catch.
     */
    public void rollback() {
        if (conn != null && transactionActive) {
            try {
                conn.rollback();
                transactionActive = false;
            } catch (SQLException e) {
                System.err.println("Error durante el rollback: " + e.getMessage());
            }
        }
    }
    
    /**
     * Cierra la transacción y la conexión, haciendo rollback si es necesario.
     * Se invoca automáticamente al usar try-with-resources.
     */
    @Override
    public void close() {
        if (conn != null) {
            try {
                if (transactionActive) {
                    rollback();
                }
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
    
    /**
     * Verifica si hay una transacción activa.
     * @return true si hay transacción en curso
     */
    public boolean isTransactionActive() {
        return transactionActive;
    }
    
}
