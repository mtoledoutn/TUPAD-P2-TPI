package progra2.Service;

import java.util.List;
import progra2.DAO.FichaBibliograficaDAO;
import progra2.Models.FichaBibliografica;

/**
 * Servicio de negocio para la gestión de fichas bibliográficas.
 * Encapsula validaciones de reglas de negocio antes de delegar al DAO.
 */
public class FichaBibliograficaService implements GenericService<FichaBibliografica> {
    
    private final FichaBibliograficaDAO fichaDAO;
    
    public FichaBibliograficaService(FichaBibliograficaDAO fichaDAO) {
        if (fichaDAO == null) {
            throw new IllegalArgumentException("FichaBibliograficaDAO no puede ser null");
        }
        this.fichaDAO = fichaDAO;
    }
    
    @Override
    public void insertar(FichaBibliografica ficha) throws Exception {
        validarFichaParaInsercion(ficha);
        fichaDAO.insertar(ficha);
    }
    
    @Override
    public void actualizar(FichaBibliografica ficha) throws Exception {
        validarFichaParaActualizacion(ficha);
        fichaDAO.actualizar(ficha);
    }
    
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        fichaDAO.eliminar(id);
    }
    
    @Override
    public FichaBibliografica getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        return fichaDAO.getById(id);
    }
    
    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        return fichaDAO.getAll();
    }
    
    
    // ==================== Métodos de Validación Privados ====================
    
    /** Valida todas las reglas de negocio para insertar una ficha nueva. */
    private void validarFichaParaInsercion(FichaBibliografica ficha) throws Exception {
        if (ficha == null) {
            throw new IllegalArgumentException("La ficha bibliografica no puede ser null");
        }
        
        // Validar ISBN (opcional pero con formato específico)
        if (ficha.getIsbn() != null && !ficha.getIsbn().trim().isEmpty()) {
            String isbn = ficha.getIsbn().trim();
            
            if (isbn.length() > 17) {
                throw new IllegalArgumentException("El ISBN no puede exceder 17 caracteres");
            }
            
            // Validar que sean dígitos (con o sin guiones)
            String isbnSinGuiones = isbn.replace("-", "");
            if (!isbnSinGuiones.matches("\\d{10}|\\d{13}")) {
                throw new IllegalArgumentException(
                    "El ISBN debe tener 10 o 13 digitos (sin contar guiones). Ejemplo: 978-3-16-148410-0"
                );
            }
        }
        
        // Validar clasificación Dewey (opcional pero con longitud máxima)
        if (ficha.getClasificacionDewey() != null && ficha.getClasificacionDewey().length() > 20) {
            throw new IllegalArgumentException("La clasificacion Dewey no puede exceder 20 caracteres");
        }
        
        // Validar estantería (opcional pero con longitud máxima)
        if (ficha.getEstanteria() != null && ficha.getEstanteria().length() > 20) {
            throw new IllegalArgumentException("La estanteria no puede exceder 20 caracteres");
        }
        
        // Validar idioma (opcional pero con longitud máxima)
        if (ficha.getIdioma() != null && ficha.getIdioma().length() > 30) {
            throw new IllegalArgumentException("El idioma no puede exceder 30 caracteres");
        }
    }
    
    /** Valida todas las reglas de negocio para actualizar una ficha existente. */
    private void validarFichaParaActualizacion(FichaBibliografica ficha) throws Exception {
        if (ficha == null) {
            throw new IllegalArgumentException("La ficha bibliografica no puede ser null");
        }
        
        // Validar que el ID sea válido
        if (ficha.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la ficha debe ser un numero positivo mayor a cero");
        }
        
        // Verificar que la ficha existe
        FichaBibliografica fichaExistente = fichaDAO.getById(ficha.getId());
        if (fichaExistente == null) {
            throw new IllegalArgumentException("No existe una ficha bibliografica con ID: " + ficha.getId());
        }
        
        // Aplicar las mismas validaciones que en inserción
        validarFichaParaInsercion(ficha);
    }
    
}
