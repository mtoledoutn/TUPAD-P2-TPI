package progra2.Service;

import java.sql.Connection;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.Config.TransactionManager;
import progra2.DAO.LibroDAO;
import progra2.Models.FichaBibliografica;
import progra2.Models.Libro;

/**
 * Servicio de negocio para la gestión de libros.
 * Encapsula validaciones de reglas de negocio antes de delegar al DAO.
 */
public class LibroService implements GenericService<Libro> {
    
    /** DAO para operaciones de persistencia de libros. */
    private final LibroDAO libroDAO;
    /** Servicio para gestionar fichas bibliográficas asociadas. */
    private final FichaBibliograficaService fichaBibliograficaService;
    
    /**
     * Constructor básico que solo recibe el DAO de libros.
     * No permite operaciones con fichas bibliográficas.
     * 
     * @param libroDAO DAO para operaciones de persistencia
     */
    public LibroService(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
        this.fichaBibliograficaService = null;
    }
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
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
    
    // ======================= Métodos sin transsacion =======================
    
    /**
     * Inserta un nuevo libro en la base de datos.
     * Realiza validaciones de negocio antes de persistir.
     * 
     * @param libro el libro a insertar
     * @throws Exception si hay error en validación o inserción
     */
    @Override
    public void insertar(Libro libro) throws Exception {
        validarLibroParaInsercion(libro);
        normalizarLibro(libro);
        libroDAO.insertar(libro);
    }
    
    /**
     * Actualiza un libro existente en la base de datos.
     * Realiza validaciones de negocio antes de persistir.
     * 
     * @param libro el libro con datos actualizados
     * @throws Exception si hay error en validación o actualización
     */
    @Override
    public void actualizar(Libro libro) throws Exception {
        validarLibroParaActualizacion(libro);
        normalizarLibro(libro);
        libroDAO.actualizar(libro);
    }
    
    /**
     * Elimina lógicamente un libro por su ID.
     * 
     * @param id identificador del libro a eliminar
     * @throws Exception si el ID es inválido o hay error en la eliminación
     */
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        libroDAO.eliminar(id);
    }
    
    /**
     * Obtiene un libro por su ID.
     * 
     * @param id identificador del libro
     * @return el libro encontrado o null si no existe
     * @throws Exception si el ID es inválido o hay error en la consulta
     */
    @Override
    public Libro getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        return libroDAO.getById(id);
    }
    
    /**
     * Obtiene todos los libros no eliminados.
     * 
     * @return lista de todos los libros
     * @throws Exception si hay error en la consulta
     */
    @Override
    public List<Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }
    
    
    // ========================= Métodos de busqueda =========================
    
    /**
     * Busca libros cuyo título contenga el texto especificado.
     * 
     * @param titulo texto a buscar (búsqueda parcial, case-insensitive)
     * @return lista de libros que coinciden
     * @throws Exception si el título está vacío o hay error en la consulta
     */
    public List<Libro> buscarPorTitulo(String titulo) throws Exception {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo de busqueda no puede estar vacio");
        }
        return libroDAO.getByTitulo(titulo);
    }
    
    /**
     * Busca libros cuyo autor contenga el texto especificado.
     * 
     * @param autor texto a buscar (búsqueda parcial, case-insensitive)
     * @return lista de libros que coinciden
     * @throws Exception si el autor está vacío o hay error en la consulta
     */
    public List<Libro> buscarPorAutor(String autor) throws Exception {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor de busqueda no puede estar vacio");
        }
        return libroDAO.getByAutor(autor);
    }
    
    /**
     * Busca libros cuya editorial contenga el texto especificado.
     * 
     * @param editorial texto a buscar (búsqueda parcial, case-insensitive)
     * @return lista de libros que coinciden
     * @throws Exception si la editorial está vacía o hay error en la consulta
     */
    public List<Libro> buscarPorEditorial(String editorial) throws Exception {
        if (editorial == null || editorial.trim().isEmpty()) {
            throw new IllegalArgumentException("La editorial de busqueda no puede estar vacia");
        }
        return libroDAO.getByEditorial(editorial);
    }
    
    /**
     * Busca libros publicados en un año específico.
     * 
     * @param anio año de publicación (debe estar entre 1000 y año actual)
     * @return lista de libros publicados en ese año
     * @throws Exception si el año es inválido o hay error en la consulta
     */
    public List<Libro> buscarPorAnioPublicacion(int anio) throws Exception {
        int anioActual = java.time.Year.now().getValue();
        if (anio < 1000 || anio > anioActual) {
            throw new IllegalArgumentException("El anio debe estar entre 1000 y " + anioActual);
        }
        return libroDAO.getByAnioEdicion(anio);
    }
    
    /**
     * Busca libros por idioma (basado en la ficha bibliográfica).
     * 
     * @param idioma idioma a buscar (comparación exacta, case-insensitive)
     * @return lista de libros en ese idioma
     * @throws Exception si el idioma está vacío o hay error en la consulta
     */
    public List<Libro> buscarPorIdioma(String idioma) throws Exception {
        if (idioma == null || idioma.trim().isEmpty()) {
            throw new IllegalArgumentException("El idioma de busqueda no puede estar vacio");
        }
        return libroDAO.getByIdioma(idioma);
    }
    
    
    // ====================== Métodos con Transacciones ======================
    
    /**
     * Inserta un libro con su ficha bibliográfica en una TRANSACCIÓN ATÓMICA.
     * Usa TransactionManager para manejar commit/rollback automáticamente.
     * Si algo falla, se hace rollback de TODO.
     * 
     * @param libro el libro a insertar
     * @param ficha la ficha bibliográfica (puede ser null si no tiene)
     * @throws Exception si hay error en validación o inserción
     */
    public void insertarLibroConFicha(Libro libro, FichaBibliografica ficha) throws Exception {
        // Try-with-resources: TransactionManager cierra automáticamente
        try (TransactionManager tm = new TransactionManager(DatabaseConnection.getConnection())) {
            
            // 1. Iniciar transacción
            tm.startTransaction();
            Connection conn = tm.getConnection();
            
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
            
            // 4. Confirmar transacción
            tm.commit();
            System.out.println("Transaccion completada exitosamente");
            
        } catch (Exception e) {
            // 5. El rollback lo hace automáticamente TransactionManager en close()
            System.err.println("Error: Se revirtieron todos los cambios (rollback)");
            throw new Exception("Error al crear libro con ficha: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza un libro y su ficha bibliográfica en una TRANSACCIÓN ATÓMICA.
     * Usa TransactionManager para manejar commit/rollback automáticamente.
     * 
     * @param libro el libro con los datos actualizados
     * @param actualizarFicha true si también se debe actualizar la ficha
     * @throws Exception si hay error
     */
    public void actualizarLibroConFicha(Libro libro, boolean actualizarFicha) throws Exception {
        try (TransactionManager tm = new TransactionManager(DatabaseConnection.getConnection())) {
            
            // 1. Iniciar transacción
            tm.startTransaction();
            Connection conn = tm.getConnection();
            
            // 2. Actualizar ficha si es necesario
            if (actualizarFicha && libro.getFichaBibliografica() != null) {
                fichaBibliograficaService.actualizar(libro.getFichaBibliografica(), conn);
                System.out.println("Ficha actualizada");
            }
            
            // 3. Actualizar libro
            validarLibroParaActualizacion(libro);
            normalizarLibro(libro);
            libroDAO.actualizar(libro, conn);
            System.out.println("Libro actualizado");
            
            // 4. Confirmar
            tm.commit();
            System.out.println("Transaccion completada exitosamente");
            
        } catch (Exception e) {
            // El rollback lo hace automáticamente TransactionManager en close()
            System.err.println("Error: Se revirtieron todos los cambios (rollback)");
            throw new Exception("Error al actualizar libro: " + e.getMessage(), e);
        }
    }
    
    // ==================== Métodos de Validación Privados ====================
    
    /**
     * Normaliza los campos de texto del libro convirtiéndolos a mayúsculas.
     * Aplica trim() para eliminar espacios en blanco y toUpperCase() para uniformidad.
     * Solo normaliza campos no nulos.
     * 
     * @param libro el libro cuyos campos serán normalizados
     */
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
    
    /**
     * Valida todas las reglas de negocio para insertar un libro nuevo.
     * 
     * Verifica que:
     * - El libro no sea null.
     * - El título sea obligatorio y no exceda 150 caracteres.
     * - El autor sea obligatorio y no exceda 120 caracteres.
     * - La editorial no exceda 100 caracteres (opcional).
     * - El año de edición esté entre 1000 y el año actual (opcional).
     * 
     * @param libro el libro a validar
     * @throws IllegalArgumentException si alguna validación falla
     * @throws Exception si hay error en la validación
    */
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
    
    /**
     * Valida todas las reglas de negocio para actualizar un libro existente.
     * Primero verifica que el libro exista en la base de datos,
     * luego aplica las mismas validaciones que para inserción.
     * 
     * @param libro el libro a validar
     * @throws IllegalArgumentException si el ID es inválido o no existe
     * @throws Exception si alguna validación falla
     */
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
