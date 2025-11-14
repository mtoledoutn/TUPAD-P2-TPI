package progra2.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.Models.Libro;
import progra2.Models.FichaBibliografica;

/** JAVADOC AQUÍ */
public class LibroDAO implements GenericDAO<Libro> {
    
    /** JAVADOC AQUÍ */
    private static final String INSERT_SQL = "INSERT INTO libro (titulo, autor, editorial, anio_edicion, ficha_bibliografica_id) VALUES (?, ?, ?, ?, ?)";
    /** JAVADOC AQUÍ */
    private static final String UPDATE_SQL = "UPDATE libro SET titulo = ?, autor = ?, editorial = ?, anio_edicion = ?, ficha_bibliografica_id = ? WHERE id = ?";
    /** JAVADOC AQUÍ */
    private static final String DELETE_SQL = "UPDATE libro SET eliminado = TRUE WHERE id = ?";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_ID_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE l.id = ? AND l.eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_TITULO_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(l.titulo) LIKE UPPER(?) AND l.eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_AUTOR_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(l.autor) LIKE UPPER(?) AND l.eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_EDITORIAL_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(l.editorial) = UPPER(?) AND l.eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_ANIO_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE l.anio_edicion = ? AND l.eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_BY_IDIOMA_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(f.idioma) = UPPER(?) AND l.eliminado = FALSE";
    /** JAVADOC AQUÍ */
    private static final String SELECT_ALL_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE l.eliminado = FALSE";
    
    
    /** JAVADOC AQUÍ */
    @Override
    public void insertar(Libro libro) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setLibrosParameters(stmt, libro);
            stmt.executeUpdate();
            setGeneratedId(stmt,libro);
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public void insertTx(Libro libro, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setLibrosParameters(stmt, libro);
            stmt.executeUpdate();
            setGeneratedId(stmt, libro);
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public void actualizar(Libro libro) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setLibrosParameters(stmt, libro);
            stmt.setInt(6, libro.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar libro con ID:" + libro.getId());
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setInt(1, id);
            
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontro libro con ID: " + id); 
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public Libro getById(int id) throws SQLException {
        try(Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibro(rs);
                }
            }
        }
        return null;
    }
    
    /** JAVADOC AQUÍ */
    public List<Libro> getByTitulo(String titulo) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_TITULO_SQL)) {
            
            stmt.setString(1, "%" + titulo + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /** JAVADOC AQUÍ */
    public List<Libro> getByAutor(String autor) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_AUTOR_SQL)) {
            
            stmt.setString(1, "%" + autor + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /** JAVADOC AQUÍ */
    public List<Libro> getByEditorial(String editorial) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EDITORIAL_SQL)) {
            
            stmt.setString(1, editorial);
            
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /** JAVADOC AQUÍ */
    public List<Libro> getByAnioEdicion(int anio) throws SQLException {
    List<Libro> libros = new ArrayList<>();
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ANIO_SQL)) {
            
            stmt.setInt(1, anio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /** JAVADOC AQUÍ */
    public List<Libro> getByIdioma(String idioma) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_IDIOMA_SQL)) {
            
            stmt.setString(1,idioma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public List<Libro> getAll() throws SQLException {
        List<Libro> libros = new ArrayList<>();
          
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        }
        return libros;
    }
    
    
    // ========================== Métodos Auxiliares ==========================
    
    /** JAVADOC AQUÍ */
    private void setLibrosParameters(PreparedStatement stmt, Libro libro) throws SQLException {
        stmt.setString(1, libro.getTitulo());
        stmt.setString(2, libro.getAutor());
        stmt.setString(3, libro.getEditorial());
        
        
        //Manejar anio_edicion nulleable
        if(libro.getAnioEdicion() != null){
            stmt.setInt(4, libro.getAnioEdicion());
        }else{
            stmt.setInt(4, java.sql.Types.INTEGER);
        }
        
        //Manejar fichaBibliografica_id nulleable
        if (libro.getFichaBibliografica() != null && libro.getFichaBibliografica().getId() > 0) {
            stmt.setInt(5, libro.getFichaBibliografica().getId());
        }else{
            stmt.setInt(5, java.sql.Types.INTEGER);
        }
    }
    
    
    /** JAVADOC AQUÍ */  
    private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException {
        try(ResultSet Keys = stmt.getGeneratedKeys()) {
            if (Keys.next()) {
                libro.setId(Keys.getInt(1));
            } else {
                throw new SQLException("No puedo obtener el ID generado del libro.");
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        FichaBibliografica ficha = null;
        // Solo crear ficha si existe en la BD
        int fichaId = rs.getInt("ficha_id");
        if (!rs.wasNull() && fichaId > 0) { //Verifical si no es null
            ficha = new FichaBibliografica (
                null,
                rs.getString("clasificacion_dewey"),
                rs.getString("estanteria"),
                rs.getString("idioma"),
                fichaId,
                rs.getBoolean("ficha_eliminado")
        );
        
        }
       
        // Construir y retornar el libro
        return new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("editorial"),
                (Integer)rs.getInt("anio_edicion"), //puede ser null
                ficha //puede ser null
        );
    }
    
}
