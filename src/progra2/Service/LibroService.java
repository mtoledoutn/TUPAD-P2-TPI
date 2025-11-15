package progra2.Service;

import java.sql.Connection;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.DAO.LibroDAO;
import progra2.Models.FichaBibliografica;
import progra2.Models.Libro;

/**
 * Servicio de negocio para la gestión de libros.
 * Encapsula validaciones de reglas de negocio antes de delegar al DAO.
 */
public class LibroService implements GenericService<Libro> {
    private final LibroDAO libroDAO;
    private final FichaBibliograficaService fichaBibliograficaService;

    public LibroService(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
        this.fichaBibliograficaService = null;
    }
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * @param libroDAO DAO para operaciones de persistencia
     * @param fichaBibliograficaService servicio para validar fichas bibliográficas
     */
    public LibroService(LibroDAO libroDAO, FichaBibliograficaService fichaBibliograficaService) {
        if (libroDAO == null) {
            throw new IllegalArgumentException("LibroDAO no puede ser null");
        }
        if (fichaBibliograficaService == null) {
            throw new IllegalArgumentException("fichaBibliograficaService no puede ser null");
        }
        this.libroDAO = libroDAO;
        this.fichaBibliograficaService = fichaBibliograficaService;
    }
    
    // ======================= Metodos sin transsacion =======================
    
    @Override
    public void insertar(Libro libro) throws Exception {
        validarLibroParaInsercion(libro);
        normalizarLibro(libro);
        libroDAO.insertar(libro);
    }

    @Override
    public void actualizar(Libro libro) throws Exception {
        validarLibroParaActualizacion(libro);
        normalizarLibro(libro);
        libroDAO.actualizar(libro);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        libroDAO.eliminar(id);
    }

    @Override
    public Libro getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        return libroDAO.getById(id);
    }

    @Override
    public List<Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }

    
    // ========================= Metodos de busqueda =========================
    
    public List<Libro> buscarPorTitulo(String titulo) throws Exception {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo de busqueda no puede estar vacio");
        }
        return libroDAO.getByTitulo(titulo);
    }

    public List<Libro> buscarPorAutor(String autor) throws Exception {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor de busqueda no puede estar vacio");
        }
        return libroDAO.getByAutor(autor);
    }

    public List<Libro> buscarPorEditorial(String editorial) throws Exception {
        if (editorial == null || editorial.trim().isEmpty()) {
            throw new IllegalArgumentException("La editorial de busqueda no puede estar vacia");
        }
        return libroDAO.getByEditorial(editorial);
    }

    public List<Libro> buscarPorAnioPublicacion(int anio) throws Exception {
        int anioActual = java.time.Year.now().getValue();
        if (anio < 1000 || anio > anioActual) {
            throw new IllegalArgumentException("El anio debe estar entre 1000 y " + anioActual);
        }
        return libroDAO.getByAnioEdicion(anio);
    }
    
    public List<Libro> buscarPorIdioma(String idioma) throws Exception {
        if (idioma == null || idioma.trim().isEmpty()) {
            throw new IllegalArgumentException("El idioma de busqueda no puede estar vacio");
        }
        return libroDAO.getByIdioma(idioma);
    }
    
    
    // ====================== Metodos con Transacciones ======================
    
    /**
     * Inserta un libro con su ficha bibliográfica en una TRANSACCIÓN ATÓMICA.
     * Si algo falla, se hace rollback de TODO.
     * 
     * @param libro el libro a insertar
     * @param ficha la ficha bibliográfica (puede ser null si no tiene)
     * @throws Exception si hay error en validación o inserción
     */
    public void insertarLibroConFicha(Libro libro, FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            // 1. Obtener conexión y configurar transacción
            conn = DatabaseConnection.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            
            // 2. Si hay ficha, insertarla primero
            if (ficha != null) {
                fichaBibliograficaService.insertar(ficha, conn);
                libro.setFichaBibliografica(ficha);
                System.out.println("Ficha creada con ID: " + ficha.getId());
            }
            
            // 3. Validar e insertar el libro
            validarLibroParaInsercion(libro);
            normalizarLibro(libro);
            libroDAO.insertar(libro, conn);
            System.out.println("Libro creado con ID: " + libro.getId());
            
            // 4. Si todo salió bien, confirmar transacción
            conn.commit();
            System.out.println("Transaccion completada exitosamente");
            
        } catch (Exception e) {
            // 5. Si algo falló, revertir TODO
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Error: Se revirtieron todos los cambios (rollback)");
                } catch (Exception rollbackEx) {
                    System.err.println("Error adicional al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new Exception("Error al crear libro con ficha: " + e.getMessage(), e);
            
        } finally {
            // 6. Restaurar autoCommit y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Actualiza un libro y su ficha bibliográfica en una TRANSACCIÓN ATÓMICA.
     * 
     * @param libro el libro con los datos actualizados
     * @param actualizarFicha true si también se debe actualizar la ficha
     * @throws Exception si hay error
     */
    public void actualizarLibroConFicha(Libro libro, boolean actualizarFicha) throws Exception {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            conn = DatabaseConnection.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            
            // 1. Actualizar ficha si es necesario
            if (actualizarFicha && libro.getFichaBibliografica() != null) {
                fichaBibliograficaService.actualizar(libro.getFichaBibliografica(), conn);
                System.out.println("Ficha actualizada");
            }
            
            // 2. Actualizar libro
            validarLibroParaActualizacion(libro);
            normalizarLibro(libro);
            libroDAO.actualizar(libro, conn);
            System.out.println("Libro actualizado");
            
            // 3. Confirmar
            conn.commit();
            System.out.println("Transaccion completada exitosamente");
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Error: Se revirtieron todos los cambios (rollback)");
                } catch (Exception rollbackEx) {
                    System.err.println("Error adicional al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new Exception("Error al actualizar libro: " + e.getMessage(), e);
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }
    
    // ==================== Métodos de Validación Privados ====================
    
    /** Normaliza los campos de texto del libro convirtiéndolos a mayúsculas. */
    private void normalizarLibro(Libro libro) {
        if (libro.getTitulo() != null) {
            libro.setTitulo(libro.getTitulo().trim().toUpperCase());
        }
        if (libro.getAutor() != null) {
            libro.setAutor(libro.getAutor().trim().toUpperCase());
        }
        if (libro.getEditorial() != null) {
            libro.setEditorial(libro.getEditorial().trim().toUpperCase());
        }
    }
    
    /** Valida todas las reglas de negocio para insertar un libro nuevo. */
    private void validarLibroParaInsercion(Libro libro) throws Exception {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        
        // Validar título (obligatorio)
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo del libro es obligatorio");
        }
        if (libro.getTitulo().length() > 150) {
            throw new IllegalArgumentException("El titulo no puede exceder 150 caracteres");
        }
        
        // Validar autor (obligatorio)
        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor del libro es obligatorio");
        }
        if (libro.getAutor().length() > 120) {
            throw new IllegalArgumentException("El autor no puede exceder 120 caracteres");
        }
        
        // Validar editorial (opcional, pero con longitud máxima)
        if (libro.getEditorial() != null && libro.getEditorial().length() > 100) {
            throw new IllegalArgumentException("La editorial no puede exceder 100 caracteres");
        }
        
        // Validar año de edición (opcional, pero debe ser razonable)
        if (libro.getAnioEdicion() != null) {
            int anioActual = java.time.Year.now().getValue();
            if (libro.getAnioEdicion() < 1000) {
                throw new IllegalArgumentException("El anio de edicion debe ser mayor o igual a 1000");
            }
            if (libro.getAnioEdicion() > anioActual) {
                throw new IllegalArgumentException("El anio de edicion no puede ser posterior al anio actual (" + anioActual + ")");
            }
        }
    }
    
    /** Valida todas las reglas de negocio para actualizar un libro existente. */
    private void validarLibroParaActualizacion(Libro libro) throws Exception {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        
        // Validar que el ID sea válido
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser un numero positivo mayor a cero");
        }
        
        // Verificar que el libro existe
        Libro libroExistente = libroDAO.getById(libro.getId());
        if (libroExistente == null) {
            throw new IllegalArgumentException("No existe un libro con ID: " + libro.getId());
        }
        
        // Aplicar las mismas validaciones que en inserción
        validarLibroParaInsercion(libro);
    }
    
}
