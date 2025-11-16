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
    
    /** Query SQL para insertar un nuevo libro. */
    private static final String INSERT_SQL =
            "INSERT INTO libro (titulo, autor, editorial, anio_edicion, ficha_bibliografica_id) VALUES (?, ?, ?, ?, ?)";
    
    /** Query SQL para actualizar un libro existente. */
    private static final String UPDATE_SQL =
            "UPDATE libro SET titulo = ?, autor = ?, editorial = ?, anio_edicion = ?, ficha_bibliografica_id = ? WHERE id = ?";
    
    /** Query SQL para eliminación lógica de un libro. */
    private static final String DELETE_SQL =
            "UPDATE libro SET eliminado = TRUE WHERE id = ?";
    
    /** Query SQL para buscar un libro por ID con JOIN a ficha bibliográfica. */
    private static final String SELECT_BY_ID_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE l.id = ? AND l.eliminado = FALSE";
    
    /** Query SQL para buscar libros por título (búsqueda parcial case-insensitive). */
    private static final String SELECT_BY_TITULO_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(l.titulo) LIKE UPPER(?) AND l.eliminado = FALSE";
    
    /** Query SQL para buscar libros por autor (búsqueda parcial case-insensitive). */
    private static final String SELECT_BY_AUTOR_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(l.autor) LIKE UPPER(?) AND l.eliminado = FALSE";
    
    /** Query SQL para buscar libros por editorial (búsqueda parcial case-insensitive). */
    private static final String SELECT_BY_EDITORIAL_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(l.editorial) LIKE UPPER(?) AND l.eliminado = FALSE";
    
    /** Query SQL para buscar libros por año de edición exacto. */
    private static final String SELECT_BY_ANIO_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE l.anio_edicion = ? AND l.eliminado = FALSE";
    
    /** Query SQL para buscar libros por idioma (desde la ficha bibliográfica). */
    private static final String SELECT_BY_IDIOMA_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE UPPER(f.idioma) = UPPER(?) AND l.eliminado = FALSE";
    
    /** Query SQL para obtener todos los libros no eliminados con sus fichas. */
    private static final String SELECT_ALL_SQL =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anio_edicion, " +
            "f.id AS ficha_id, f.eliminado AS ficha_eliminado, f.isbn, f.clasificacion_dewey, f.estanteria, f.idioma " +
            "FROM libro l " +
            "LEFT JOIN ficha_bibliografica f ON l.ficha_bibliografica_id = f.id " +
            "WHERE l.eliminado = FALSE";
    
    
    // ===================== Métodos con conexion propia =====================
    
    /**
     * Inserta un nuevo libro en la base de datos.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param libro el libro a insertar
     * @throws SQLException si hay error en la inserción
     */
    @Override
    public void insertar(Libro libro) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            insertar(libro, conn);
        }
    }
    
    /**
     * Actualiza un libro existente en la base de datos.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param libro el libro con datos actualizados
     * @throws SQLException si hay error en la actualización
     */
    @Override
    public void actualizar(Libro libro) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            actualizar(libro, conn);
        }
    }
    
    /**
     * Elimina lógicamente un libro (soft delete).
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param id identificador del libro a eliminar
     * @throws SQLException si hay error en la eliminación
     */
    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            eliminar(id, conn);
        }
    }
    
    /**
     * Obtiene un libro por su ID, incluyendo su ficha bibliográfica si tiene.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param id identificador del libro
     * @return el libro encontrado o null si no existe
     * @throws SQLException si hay error en la consulta
     */
    @Override
    public Libro getById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return getById(id, conn);
        }
    }
    
    /**
     * Obtiene todos los libros no eliminados con sus fichas bibliográficas.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @return lista de libros
     * @throws SQLException si hay error en la consulta
     */
    @Override
    public List<Libro> getAll() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return getAll(conn);
        }
    }
    
    // =========== Métodos con conexion Externa (Para transacciones) ===========
    
    /**
     * Inserta un libro usando una conexión externa (para transacciones).
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param libro el libro a insertar
     * @param conn conexión de base de datos externa
     * @throws SQLException si hay error en la inserción
     */
    public void insertar(Libro libro, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setLibroParameters(stmt, libro);
            stmt.executeUpdate();
            setGeneratedId(stmt, libro);
        }
    }
    
    /**
     * Actualiza un libro usando una conexión externa (para transacciones).
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param libro el libro con datos actualizados
     * @param conn conexión de base de datos externa
     * @throws SQLException si hay error en la actualización
     */
    public void actualizar(Libro libro, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setLibroParameters(stmt, libro);
            stmt.setInt(6, libro.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar libro con ID: " + libro.getId());
            }
        }
    }
    
    /**
     * Elimina lógicamente un libro usando una conexión externa.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param id identificador del libro
     * @param conn conexión de base de datos externa
     * @throws SQLException si hay error en la eliminación
     */
    public void eliminar(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setInt(1, id);
            
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontro libro con ID: " + id); 
            }
        }
    }
    
    
    // ========================= Metodos de busqueda =========================
    
    /**
     * Obtiene un libro por su ID usando una conexión externa.
     * Incluye un LEFT JOIN con la ficha bibliográfica.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param id identificador del libro
     * @param conn conexión de base de datos externa
     * @return el libro encontrado con su ficha o null si no existe
     * @throws SQLException si hay error en la consulta
     */
    public Libro getById(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibro(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Busca libros cuyo título contenga el texto especificado (case-insensitive).
     * 
     * @param titulo texto a buscar en el título
     * @return lista de libros que coinciden
     * @throws SQLException si hay error en la consulta
     */
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
    
    /**
     * Busca libros cuyo autor contenga el texto especificado (case-insensitive).
     * 
     * @param autor texto a buscar en el autor
     * @return lista de libros que coinciden
     * @throws SQLException si hay error en la consulta
     */
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
    
    /**
     * Busca libros cuya editorial contenga el texto especificado (case-insensitive).
     * 
     * @param editorial texto a buscar en la editorial
     * @return lista de libros que coinciden
     * @throws SQLException si hay error en la consulta
     */
    public List<Libro> getByEditorial(String editorial) throws SQLException {
        List<Libro> libros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EDITORIAL_SQL)) {

            stmt.setString(1, "%" + editorial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        }
        return libros;
    }
    
    /**
     * Busca libros publicados en un año específico.
     * 
     * @param anio año de edición a buscar
     * @return lista de libros publicados en ese año
     * @throws SQLException si hay error en la consulta
     */
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
    
    /**
     * Busca libros por idioma (basado en la ficha bibliográfica).
     * 
     * @param idioma idioma a buscar (comparación exacta case-insensitive)
     * @return lista de libros en ese idioma
     * @throws SQLException si hay error en la consulta
     */
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
    
    /**
     * Obtiene todos los libros usando una conexión externa.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param conn conexión de base de datos externa
     * @return lista de todos los libros no eliminados
     * @throws SQLException si hay error en la consulta
     */
    public List<Libro> getAll(Connection conn) throws SQLException {
        List<Libro> libros = new ArrayList<>();
          
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        }
        return libros;
    }
    
    
    // ========================== Métodos Auxiliares ==========================
    
    /**
     * Configura los parámetros del PreparedStatement con los datos del libro.
     * Maneja valores nullable para año de edición y ficha bibliográfica.
     * 
     * @param stmt el PreparedStatement a configurar
     * @param libro el libro con los datos a insertar/actualizar
     * @throws SQLException si hay error al configurar los parámetros
     */
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
    
    /**
     * Obtiene el ID generado por la base de datos y lo asigna al objeto libro.
     * 
     * @param stmt el PreparedStatement que ejecutó la inserción
     * @param libro el libro al que asignar el ID generado
     * @throws SQLException si no se pudo obtener el ID generado
     */
    private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                libro.setId(keys.getInt(1));
            } else {
                throw new SQLException("No se pudo obtener el ID generado del libro.");
            }
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Libro con su FichaBibliografica asociada.
     * Si el libro no tiene ficha asociada, el campo será null.
     * Maneja correctamente los valores nullable del ResultSet.
     * 
     * @param rs el ResultSet posicionado en una fila válida
     * @return objeto Libro completamente poblado
     * @throws SQLException si hay error al leer los datos del ResultSet
     */
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
