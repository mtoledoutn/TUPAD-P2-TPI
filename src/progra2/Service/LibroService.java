package progra2.Service;

import java.util.List;
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
    
    @Override
    public void insertar(Libro libro) throws Exception {
        validarLibroParaInsercion(libro);
        libroDAO.insertar(libro);
    }

    @Override
    public void actualizar(Libro libro) throws Exception {
        validarLibroParaActualizacion(libro);
        libroDAO.actualizar(libro);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo mayor a cero");
        }
        
        // Verificar que el libro existe antes de intentar eliminarlo
        Libro libroExistente = libroDAO.getById(id);
        if (libroExistente == null) {
            throw new IllegalArgumentException("No existe un libro con ID: " + id);
        }
        
        libroDAO.eliminar(id);
    }

    @Override
    public Libro getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo mayor a cero");
        }
        return libroDAO.getById(id);
    }

    @Override
    public List<Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }

    public List<Libro> buscarPorTitulo(String titulo) throws Exception {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de búsqueda no puede estar vacío");
        }
        return libroDAO.getByTitulo(titulo);
    }

    public List<Libro> buscarPorAutor(String autor) throws Exception {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor de búsqueda no puede estar vacío");
        }
        return libroDAO.getByAutor(autor);
    }

    public List<Libro> buscarPorEditorial(String editorial) throws Exception {
        if (editorial == null || editorial.trim().isEmpty()) {
            throw new IllegalArgumentException("La editorial de búsqueda no puede estar vacía");
        }
        return libroDAO.getByEditorial(editorial);
    }

    public List<Libro> buscarPorAnioPublicacion(int anio) throws Exception {
        if (anio < 1000 || anio > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException(
                "El año debe estar entre 1000 y " + java.time.Year.now().getValue()
            );
        }
        return libroDAO.getByAnioEdicion(anio);
    }

    public List<Libro> buscarPorIdioma(String idioma) throws Exception {
        if (idioma == null || idioma.trim().isEmpty()) {
            throw new IllegalArgumentException("El idioma de búsqueda no puede estar vacío");
        }
        return libroDAO.getByIdioma(idioma);
    }
    
    
    // ==================== Métodos de Validación Privados ====================
    
    /**
     * Valida todas las reglas de negocio para insertar un libro nuevo.
     */
    private void validarLibroParaInsercion(Libro libro) throws Exception {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        
        // Validar título (obligatorio)
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro es obligatorio");
        }
        if (libro.getTitulo().length() > 150) {
            throw new IllegalArgumentException("El título no puede exceder 150 caracteres");
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
                throw new IllegalArgumentException("El año de edición debe ser mayor o igual a 1000");
            }
            if (libro.getAnioEdicion() > anioActual) {
                throw new IllegalArgumentException(
                    "El año de edición no puede ser posterior al año actual (" + anioActual + ")"
                );
            }
        }
        
        // Validar ficha bibliográfica (opcional, pero si existe debe estar en BD)
        if (libro.getFichaBibliografica() != null) {
            if (libro.getFichaBibliografica().getId() <= 0) {
                throw new IllegalArgumentException(
                    "La ficha bibliográfica debe estar guardada en la base de datos antes de asociarla"
                );
            }
            
            // Verificar que la ficha existe en la BD
            FichaBibliografica fichaExistente = fichaBibliograficaService.getById(
                libro.getFichaBibliografica().getId()
            );
            if (fichaExistente == null) {
                throw new IllegalArgumentException(
                    "No existe una ficha bibliográfica con ID: " + libro.getFichaBibliografica().getId()
                );
            }
        }
    }
    
    /**
     * Valida todas las reglas de negocio para actualizar un libro existente.
     */
    private void validarLibroParaActualizacion(Libro libro) throws Exception {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        
        // Validar que el ID sea válido
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser un número positivo mayor a cero");
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
