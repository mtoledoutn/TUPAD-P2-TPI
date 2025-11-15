package progra2.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.Models.Libro;
import progra2.Models.FichaBibliografica;

/**
 * DAO para operaciones CRUD de Libro en la base de datos.
 * Maneja la persistencia y recuperación de libros con sus fichas bibliográficas asociadas.
 */
public class LibroDAO implements GenericDAO<Libro> {
    
    /** JAVADOC AQUÍ */
    private static final String INSERT_SQL =
            "INSERT INTO libro (titulo, autor, editorial, anio_edicion, ficha_bibliografica_id) VALUES (?, ?, ?, ?, ?)";
    
    /** JAVADOC AQUÍ */
    private static final String UPDATE_SQL =
            "UPDATE libro SET titulo = ?, autor = ?, editorial = ?, anio_edicion = ?, ficha_bibliografica_id = ? WHERE id = ?";
    
    /** JAVADOC AQUÍ */
    private static final String DELETE_SQL =
            "UPDATE libro SET eliminado = TRUE WHERE id = ?";
    
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
    
    //Metodos cn conexion propia
    
    @Override
    public void insertar(Libro libro) throws SQLException{
        try(Connection conn = DatabaseConnection.getConnection()){
            insertar(libro, conn);
        }
    }
    
    @Override
    public void actualizar(Libro libro) throws SQLException{
        try(Connection conn = DatabaseConnection.getConnection()){
            actualizar (libro , conn);
        }
    }
    
    @Override
    public void eliminar(int id) throws SQLException{
        try(Connection conn = DatabaseConnection.getConnection()){
            eliminar(id, conn);
        }
    }
    
    
    @Override
    public Libro getById(int id) throws SQLException{
        try(Connection conn = DatabaseConnection.getConnection()){
            return getById(id,conn);
        }
    }
    
    @Override
    public List<Libro> getAll() throws SQLException{
        try(Connection conn = DatabaseConnection.getConnection()){
            return getAll(conn);
        }
    }
    
    // Metodos con conexion Externa (Para transacciones)
    
    //inserta un libro usando conexion externa (para transacciones)
    public void insertar(Libro libro, Connection conn) throws SQLException {
        try (
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setLibroParameters(stmt, libro);
            stmt.executeUpdate();
            setGeneratedId(stmt, libro);
        }
    }
    
    /** JAVADOC AQUÍ */
    //actualiza libro usando una conexion externa(para transsaciones)
    public void actualizar(Libro libro, Connection conn) throws SQLException {
        try (
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setLibroParameters(stmt, libro);
            stmt.setInt(6, libro.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar libro con ID: " + libro.getId());
            }
        }
    }
    
    /** JAVADOC AQUÍ */
    //elimina un libro usando una conexion externa(para transsaciones)
    public void eliminar(int id, Connection conn) throws SQLException {
        try (
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setInt(1, id);
            
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontro libro con ID: " + id); 
            }
        }
    }
    
    
    //Metodos de busqueda
    
    /** JAVADOC AQUÍ */
   //obtiene un libro por ID usando una conexion externa
    public Libro getById(int id, Connection conn) throws SQLException {
        try (
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
            
            try (ResultSet rs = stmt.executeQuery()) {
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
            
            stmt.setString(1, idioma);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /** JAVADOC AQUÍ */
    
    public List<Libro> getAll(Connection conn) throws SQLException {
        List<Libro> libros = new ArrayList<>();
          
        try (
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        }
        return libros;
    }
    
    
    // ========================== Métodos Auxiliares ==========================
    
    /** Configura los parámetros del PreparedStatement con los datos del libro. */
    private void setLibroParameters(PreparedStatement stmt, Libro libro) throws SQLException {
        stmt.setString(1, libro.getTitulo());
        stmt.setString(2, libro.getAutor());
        stmt.setString(3, libro.getEditorial());
        
        // Manejar anio_edicion nullable
        if (libro.getAnioEdicion() != null) {
            stmt.setInt(4, libro.getAnioEdicion());
        } else {
            stmt.setNull(4, java.sql.Types.INTEGER);
        }
        
        // Manejar ficha_bibliografica_id nullable
        if (libro.getFichaBibliografica() != null && libro.getFichaBibliografica().getId() > 0) {
            stmt.setInt(5, libro.getFichaBibliografica().getId());
        } else {
            stmt.setNull(5, java.sql.Types.BIGINT);
        }
    }
    
    /** Obtiene el ID generado por la BD y lo asigna al objeto libro. */  
    private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                libro.setId(keys.getInt(1));
            } else {
                throw new SQLException("No se pudo obtener el ID generado del libro.");
            }
        }
    }
    
    /** Mapea un ResultSet a un objeto Libro con su FichaBibliografica asociada. */
    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        FichaBibliografica ficha = null;
        
        // Solo crear ficha si existe en la BD
        int fichaId = rs.getInt("ficha_id");
        if (!rs.wasNull() && fichaId > 0) {
            ficha = new FichaBibliografica(
                rs.getString("isbn"),
                rs.getString("clasificacion_dewey"),
                rs.getString("estanteria"),
                rs.getString("idioma"),
                fichaId,
                rs.getBoolean("ficha_eliminado")
            );
        }
        
        // Manejar anio_edicion nullable
        Integer anioEdicion = null;
        int anioValue = rs.getInt("anio_edicion");
        if (!rs.wasNull()) {
            anioEdicion = anioValue;
        }
        
        return new Libro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("editorial"),
            anioEdicion,
            ficha
        );
    }
    
}
