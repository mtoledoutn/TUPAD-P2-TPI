package progra2.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.Models.FichaBibliografica;

/**
 * DAO (Data Access Object) para operaciones CRUD de FichaBibliografica.
 * Maneja la persistencia y recuperación de fichas bibliográficas en la base de datos.
 * 
 * Proporciona dos conjuntos de métodos:
 * - Métodos con conexión propia: para uso independiente.
 * - Métodos con conexión externa: para uso en transacciones.
 * 
 * Implementa soft delete (eliminación lógica) mediante el campo 'eliminado'.
 */
public class FichaBibliograficaDAO implements GenericDAO<FichaBibliografica> {
    
    /** Query SQL para insertar una nueva ficha bibliográfica. */
    private static final String INSERT_SQL = "INSERT INTO ficha_bibliografica (isbn, clasificacion_dewey, estanteria, idioma) VALUES (?, ?, ?, ?)";
    
    /** Query SQL para actualizar una ficha bibliográfica existente. */
    private static final String UPDATE_SQL = "UPDATE ficha_bibliografica SET isbn = ?, clasificacion_dewey = ?, estanteria = ?, idioma = ? WHERE id = ?";
    
    /** Query SQL para realizar eliminación lógica de una ficha. */
    private static final String DELETE_SQL = "UPDATE ficha_bibliografica SET eliminado = TRUE WHERE id = ?";
    
    /** Query SQL para buscar una ficha por ID (solo no eliminadas). */
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM ficha_bibliografica WHERE id = ? AND eliminado = FALSE";
    
    /** Query SQL para obtener todas las fichas no eliminadas. */
    private static final String SELECT_ALL_SQL = "SELECT * FROM ficha_bibliografica WHERE eliminado = FALSE";
    
    /** Query SQL para verificar existencia de un ISBN. */
    private static final String EXISTS_ISBN_SQL = "SELECT COUNT(*) FROM ficha_bibliografica WHERE isbn = ? AND eliminado = FALSE";
    
    /** Query SQL para verificar existencia de ISBN excluyendo un ID específico. */
    private static final String EXISTS_ISBN_EXCEPT_ID_SQL = "SELECT COUNT(*) FROM ficha_bibliografica WHERE isbn = ? AND id != ? AND eliminado = FALSE";
    
    
    // ===================== Métodos con conexion propia =====================
    
    /**
     * Inserta una nueva ficha bibliográfica en la base de datos.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param fichaBibliografica la ficha a insertar
     * @throws SQLException si hay error en la inserción
     */
    @Override
    public void insertar(FichaBibliografica fichaBibliografica) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            insertar(fichaBibliografica, conn);
        }
    }
    
    /**
     * Actualiza una ficha bibliográfica existente.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param ficha la ficha con datos actualizados
     * @throws SQLException si hay error en la actualización
     */
    @Override
    public void actualizar(FichaBibliografica ficha) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            actualizar(ficha, conn);
        }
    }
    
    /**
     * Elimina lógicamente una ficha bibliográfica (soft delete).
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param id identificador de la ficha a eliminar
     * @throws SQLException si hay error en la eliminación
     */
    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            eliminar(id, conn);
        }
    }
    
    /**
     * Obtiene una ficha bibliográfica por su ID.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param id identificador de la ficha
     * @return la ficha encontrada o null si no existe
     * @throws SQLException si hay error en la consulta
     */
    @Override
    public FichaBibliografica getById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return getById(id, conn);
        }
    }
    
    /**
     * Obtiene todas las fichas bibliográficas no eliminadas.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @return lista de fichas bibliográficas
     * @throws SQLException si hay error en la consulta
     */
    @Override
    public List<FichaBibliografica> getAll() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return getAll(conn);
        }
    }
    
    
    // ================ Métodos con conexion externa (original) ================
    
    /**
     * Inserta una ficha usando una conexion externa (para transacciones).
     * 
     * @param ficha la ficha a insertar
     * @param conn la conexion de base de datos (manejada externamente)
     */
    public void insertar(FichaBibliografica ficha, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setFichaParameters(stmt, ficha);
            stmt.executeUpdate();
            setGeneratedId(stmt, ficha);
        }
    }
    
    /**
     * Actualiza una ficha bibliográfica usando una conexión externa.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param ficha la ficha con datos actualizados
     * @param conn conexión de base de datos externa
     * @throws SQLException si hay error en la actualización
     */
    public void actualizar(FichaBibliografica ficha, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setFichaParameters(stmt, ficha);
            stmt.setInt(5, ficha.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar la ficha con ID: " + ficha.getId());
            }
        }
    }
    
    /**
     * Elimina lógicamente una ficha usando una conexión externa.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param id identificador de la ficha
     * @param conn conexión de base de datos externa
     * @throws SQLException si hay error en la eliminación
     */
    public void eliminar(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
                
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontro ficha con ID: " + id);
            }
        }
    }
    
    /**
     * Obtiene una ficha por ID usando una conexión externa.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param id identificador de la ficha
     * @param conn conexión de base de datos externa
     * @return la ficha encontrada o null si no existe
     * @throws SQLException si hay error en la consulta
     */
    public FichaBibliografica getById(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFicha(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Obtiene todas las fichas usando una conexión externa.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param conn conexión de base de datos externa
     * @return lista de fichas bibliográficas
     * @throws SQLException si hay error en la consulta
     */
    public List<FichaBibliografica> getAll(Connection conn) throws SQLException {
        List<FichaBibliografica> fichas = new ArrayList<>();
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                fichas.add(mapResultSetToFicha(rs));
            }
        }
        return fichas;
    }
    
    // ======================== Métodos de validacion ========================
    
    /**
     * Verifica si ya existe una ficha con el ISBN dado.
     * 
     * @param isbn el ISBN a verificar
     * @return true si exise, false en caso contrario
     */
    public boolean existeISBN(String isbn) throws SQLException {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_ISBN_SQL)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    /**
     * Verifica si existe una ficha con el ISBN especificado,
     * excluyendo un ID específico (útil para actualizaciones).
     * 
     * @param isbn el ISBN a verificar
     * @param idActual ID a excluir de la búsqueda
     * @return true si existe otra ficha con ese ISBN, false en caso contrario
     * @throws SQLException si hay error en la consulta
     */
    public boolean existeISBNExceptoId(String isbn , int idActual) throws SQLException {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_ISBN_EXCEPT_ID_SQL)) {
            
            stmt.setString(1, isbn);
            stmt.setInt(2, idActual);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    
    // ===================== Métodos auxiliares privados =====================
    
    /**
     * Configura los parámetros del PreparedStatement con los datos de la ficha.
     * 
     * @param stmt el PreparedStatement a configurar
     * @param fichaBibliografica la ficha con los datos
     * @throws SQLException si hay error al configurar los parámetros
     */
    private void setFichaParameters(PreparedStatement stmt, FichaBibliografica fichaBibliografica) throws SQLException {
        stmt.setString(1, fichaBibliografica.getIsbn());
        stmt.setString(2, fichaBibliografica.getClasificacionDewey());
        stmt.setString(3, fichaBibliografica.getEstanteria());
        stmt.setString(4, fichaBibliografica.getIdioma());
    }
    
    /**
     * Obtiene el ID generado automáticamente y lo asigna a la ficha.
     * 
     * @param stmt el PreparedStatement que ejecutó la inserción
     * @param fichaBibliografica la ficha a la que asignar el ID
     * @throws SQLException si no se pudo obtener el ID generado
     */
    private void setGeneratedId(PreparedStatement stmt, FichaBibliografica fichaBibliografica) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                fichaBibliografica.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La insercion de la ficha fallo, no se obtuvo ID generado");
            }
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto FichaBibliografica.
     * 
     * @param rs el ResultSet con los datos
     * @return objeto FichaBibliografica mapeado
     * @throws SQLException si hay error al leer los datos
     */
    private FichaBibliografica mapResultSetToFicha(ResultSet rs) throws SQLException {
        return new FichaBibliografica(
                rs.getString("isbn"),
                rs.getString("clasificacion_dewey"),
                rs.getString("estanteria"),
                rs.getString("idioma"),
                rs.getInt("id"),
                rs.getBoolean("eliminado")
        );
    }
    
}
