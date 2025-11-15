package progra2.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.Models.FichaBibliografica;

/** JAVADOC AQUÍ */
public class FichaBibliograficaDAO implements GenericDAO<FichaBibliografica> {
    
    /** JAVADOC AQUÍ */
    private static final String INSERT_SQL = "INSERT INTO ficha_bibliografica (isbn, clasificacion_dewey, estanteria, idioma) VALUES (?, ?, ?, ?)";
    /** JAVADOC AQUÍ */
    private static final String UPDATE_SQL = "UPDATE ficha_bibliografica SET isbn = ?, clasificacion_dewey = ?, estanteria = ?, idioma = ? WHERE id = ?";
    /** JAVADOC AQUÍ */
    private static final String DELETE_SQL = "UPDATE ficha_bibliografica SET eliminado = TRUE WHERE id = ?";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM ficha_bibliografica WHERE id = ? AND eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_ALL_SQL = "SELECT * FROM ficha_bibliografica WHERE eliminado = FALSE";
    /** JAVADOC AQUI */ 
    private static final String EXISTS_ISBN_SQL = "SELECT COUNT(*) FROM ficha_bibliografica WHERE isbn = ? AND eliminado = FALSE";
    
    //Metodos con conexion propia
    @Override
    public void insertar(FichaBibliografica fichaBibliografica) throws SQLException{
        try (Connection conn = DatabaseConnection.getConnection()){
            insertar(fichaBibliografica, conn);
        }
    }
    @Override
    public void actualizar(FichaBibliografica ficha) throws SQLException{
        try (Connection conn = DatabaseConnection.getConnection()){
            actualizar(ficha, conn);
        }
    }
    
    @Override
    public void eliminar(int id) throws SQLException{
        try (Connection conn = DatabaseConnection.getConnection()){
            eliminar(id,conn);
        }
    }
    
    @Override
    public FichaBibliografica getById(int id) throws SQLException{
        try (Connection conn = DatabaseConnection.getConnection()){
            return getById(id, conn);
        }
    }
    
    @Override
    public List<FichaBibliografica> getAll() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()){
        return getAll(conn);
     }
    }
    
    
    // Metodos con conexion externa (original)
    
    /**
     * inserta una ficha usando una conexion externa (para transacciones).
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
    
    /** JAVADOC AQUÍ */
    
    //Actualiza una ficha usando una conexion externa (para transacciones)
    
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
    
    /** JAVADOC AQUÍ */
    // Elimina una ficha usando una conexion externa (para transacciones).
    public void eliminar(int id, Connection conn) throws SQLException {
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró ficha con ID: " + id);
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    // Obtiene una ficha por ID usando una conexion externa.
    public FichaBibliografica getById(int id, Connection conn) throws SQLException {
             try(PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFicha(rs);
                }
            }
        }
        return null;
    }
    
    /** JAVADOC AQUÍ */
    // Obtiene todas las fichas usando una conexion externa.
    public List<FichaBibliografica> getAll(Connection conn) throws SQLException {
        List<FichaBibliografica> fichas = new ArrayList<>();
        
        try (
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                fichas.add(mapResultSetToFicha(rs));
            }
        }
        return fichas;
    }
    
    // Metodos de validacion
    
    /**
     * Verifica si ya existe una ficha con el ISBN dado.
     * @param isbn el ISBN a verificar
     * @return true si exise, false en caso contrario
     */
    
    public boolean existeISBN(String isbn) throws SQLException{
        if (isbn == null || isbn.trim().isEmpty()){
            return false;
        }
        
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(EXISTS_ISBN_SQL)){
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()){
                return rs.next() && rs.getInt(1) > 0;
            }
        }
        
    }
    
    
    //Verifica si existe un ISBN diferente al de la ficha actual (para actualizaciones)
    
    public boolean existeISBNExceptoId(String isbn , int idActual) throws SQLException{
        if(isbn == null || isbn.trim().isEmpty()){
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM ficha_bibliografica WHERE isbn = ? AND id != ? AND eliminado = FALSE";
        
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, isbn);
            stmt.setInt(2, idActual);
            
            try(ResultSet rs = stmt.executeQuery()){
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    
    //metodos auxiliares privados
    
    /** JAVADOC AQUÍ */
    private void setFichaParameters(PreparedStatement stmt, FichaBibliografica fichaBibliografica) throws SQLException {
        stmt.setString(1, fichaBibliografica.getIsbn());
        stmt.setString(2, fichaBibliografica.getClasificacionDewey());
        stmt.setString(3, fichaBibliografica.getEstanteria());
        stmt.setString(4, fichaBibliografica.getIdioma());
    }
    
    /** JAVADOC AQUÍ */
    private void setGeneratedId(PreparedStatement stmt, FichaBibliografica fichaBibliografica) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                fichaBibliografica.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La inserción de la ficha falló, no se obtuvo ID generado");
            }
        }
    }
    
    /** JAVADOC AQUÍ */
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
