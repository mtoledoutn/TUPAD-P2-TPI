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
    
    /** JAVADOC AQUÍ */
    @Override
    public void insertar(FichaBibliografica fichaBibliografica) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setFichaParameters(stmt, fichaBibliografica);
            stmt.executeUpdate();
            setGeneratedId(stmt, fichaBibliografica);
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public void insertTx(FichaBibliografica fichaBibliografica, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setFichaParameters(stmt, fichaBibliografica);
            stmt.executeUpdate();
            setGeneratedId(stmt, fichaBibliografica);
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public void actualizar(FichaBibliografica fichaBibliografica) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, fichaBibliografica.getIsbn());
            stmt.setString(2, fichaBibliografica.getClasificacionDewey());
            stmt.setString(3, fichaBibliografica.getEstanteria());
            stmt.setString(4, fichaBibliografica.getIdioma());
            stmt.setInt(5, fichaBibliografica.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar la ficha con ID: " + fichaBibliografica.getId());
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró ficha con ID: " + id);
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public FichaBibliografica getById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

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
    @Override
    public List<FichaBibliografica> getAll() throws SQLException {
        List<FichaBibliografica> fichas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                fichas.add(mapResultSetToFicha(rs));
            }
        }
        return fichas;
    }
    
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
