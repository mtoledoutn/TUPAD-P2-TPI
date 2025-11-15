package progra2.Config;

import java.sql.Connection;
import java.sql.SQLException;

/** JAVADOC AQUÍ */
public class TransactionManager implements AutoCloseable {
    
    /** JAVADOC AQUÍ */
    private Connection conn;
    /** JAVADOC AQUÍ */
    private boolean transactionActive;
    
    /** JAVADOC AQUÍ */
    public TransactionManager(Connection conn) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("La conexion no puede ser null");
        }
        this.conn = conn;
        this.transactionActive = false;
    }
    
    /** JAVADOC AQUÍ */
    public Connection getConnection() {
        return conn;
    }
    
    /** JAVADOC AQUÍ */
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
    
    /** JAVADOC AQUÍ */
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
    
    /** JAVADOC AQUÍ */
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
    
    /** JAVADOC AQUÍ */
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
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    public boolean isTransactionActive() {
        return transactionActive;
    }
    
}
