package progra2.Service;

import java.sql.Connection;
import java.util.List;
import progra2.DAO.FichaBibliograficaDAO;
import progra2.Models.FichaBibliografica;

/**
 * Servicio de negocio para la gestión de fichas bibliográficas.
 * Encapsula validaciones de reglas de negocio antes de delegar al DAO.
 * Soporta transacciones mediante conexiones externas.
 */
public class FichaBibliograficaService implements GenericService<FichaBibliografica> {
    
    /** DAO para operaciones de persistencia de fichas bibliográficas. */
    private final FichaBibliograficaDAO fichaDAO;
    
    /**
     * Constructor que recibe el DAO necesario para operaciones de persistencia.
     * 
     * @param fichaDAO DAO para operaciones con fichas bibliográficas
     * @throws IllegalArgumentException si fichaDAO es null
     */
    public FichaBibliograficaService(FichaBibliograficaDAO fichaDAO) {
        if (fichaDAO == null) {
            throw new IllegalArgumentException("FichaBibliograficaDAO no puede ser null");
        }
        this.fichaDAO = fichaDAO;
    }
    
    // ======================= Métodos sin transaccion =======================
    
    /**
     * Inserta una nueva ficha bibliográfica en la base de datos.
     * Realiza validaciones de negocio y normalización antes de persistir.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param ficha la ficha a insertar
     * @throws Exception si hay error en validación o inserción
     */
    @Override
    public void insertar(FichaBibliografica ficha) throws Exception {
        validarFichaParaInsercion(ficha);
        normalizarFicha(ficha);
        fichaDAO.insertar(ficha);
    }
    
    /**
     * Actualiza una ficha bibliográfica existente en la base de datos.
     * Realiza validaciones de negocio y normalización antes de persistir.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param ficha la ficha con datos actualizados
     * @throws Exception si hay error en validación o actualización
     */
    @Override
    public void actualizar(FichaBibliografica ficha) throws Exception {
        validarFichaParaActualizacion(ficha);
        normalizarFicha(ficha);
        fichaDAO.actualizar(ficha);
    }
    
    /**
     * Elimina lógicamente una ficha bibliográfica por su ID.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param id identificador de la ficha a eliminar
     * @throws IllegalArgumentException si el ID es inválido
     * @throws Exception si hay error en la eliminación
     */
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        fichaDAO.eliminar(id);
    }
    
    /**
     * Obtiene una ficha bibliográfica por su ID.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @param id identificador de la ficha
     * @return la ficha encontrada o null si no existe
     * @throws IllegalArgumentException si el ID es inválido
     * @throws Exception si hay error en la consulta
     */
    @Override
    public FichaBibliografica getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo mayor a cero");
        }
        return fichaDAO.getById(id);
    }
    
    /**
     * Obtiene todas las fichas bibliográficas no eliminadas.
     * Crea su propia conexión y la cierra automáticamente.
     * 
     * @return lista de fichas bibliográficas
     * @throws Exception si hay error en la consulta
     */
    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        return fichaDAO.getAll();
    }
    
    
    // ======================= Métodos con transaccion =======================
    
    /**
     * Inserta una ficha dentro de una transacción existente.
     * Realiza validaciones de negocio y normalización antes de persistir.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param ficha la ficha a insertar
     * @param conn la conexión con transacción activa
     * @throws Exception si hay error en validación o inserción
     */
    public void insertar(FichaBibliografica ficha, Connection conn) throws Exception {
        validarFichaParaInsercion(ficha);
        normalizarFicha(ficha);
        fichaDAO.insertar(ficha, conn);
    }
    
    
    /**
     * Actualiza una ficha dentro de una transacción existente.
     * Realiza validaciones de negocio y normalización antes de persistir.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param ficha la ficha con datos actualizados
     * @param conn la conexión con transacción activa
     * @throws Exception si hay error en validación o actualización
     */
    public void actualizar(FichaBibliografica ficha, Connection conn) throws Exception {
        validarFichaParaActualizacion(ficha);
        normalizarFicha(ficha);
        fichaDAO.actualizar(ficha, conn);
    }
    
    
    /**
     * Elimina una ficha dentro de una transacción existente.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param id identificador de la ficha a eliminar
     * @param conn la conexión con transacción activa
     * @throws IllegalArgumentException si el ID es inválido
     * @throws Exception si hay error en la eliminación
     */
    public void eliminar(int id, Connection conn) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo o mayor a cero");
        }
        fichaDAO.eliminar(id, conn);
    }
    
    
    /**
     * Obtiene una ficha por ID dentro de una transacción existente.
     * No cierra la conexión (debe ser manejada por el llamador).
     * 
     * @param id identificador de la ficha
     * @param conn la conexión con transacción activa
     * @return la ficha encontrada o null si no existe
     * @throws IllegalArgumentException si el ID es inválido
     * @throws Exception si hay error en la consulta
     */
    public FichaBibliografica getById(int id, Connection conn) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID deber ser un numero positivo mayor o cero");
        }
        return fichaDAO.getById(id, conn);
    }
    
    
    // ==================== Métodos de Validación Privados ====================
    
    /**
     * Normaliza los campos de texto de la ficha convirtiéndolos a mayúsculas.
     * Aplica trim() para eliminar espacios en blanco y toUpperCase() para uniformidad.
     * Solo normaliza campos no nulos.
     * 
     * @param ficha la ficha cuyos campos serán normalizados
     */
    private void normalizarFicha(FichaBibliografica ficha) {
        if (ficha.getIsbn() != null) {
            ficha.setIsbn(ficha.getIsbn().trim().toUpperCase());
        }
        if (ficha.getClasificacionDewey() != null) {
            ficha.setClasificacionDewey(ficha.getClasificacionDewey().trim().toUpperCase());
        }
        if (ficha.getEstanteria() != null) {
            ficha.setEstanteria(ficha.getEstanteria().trim().toUpperCase());
        }
        if (ficha.getIdioma() != null) {
            ficha.setIdioma(ficha.getIdioma().trim().toUpperCase());
        }
    }
    
    /**
     * Valida todas las reglas de negocio para insertar una ficha nueva.
     * 
     * Verifica que:
     * - La ficha no sea null.
     * - El ISBN tenga formato válido (10 o 13 dígitos) y no exista ya.
     * - La clasificación Dewey no exceda 20 caracteres.
     * - La estantería no exceda 20 caracteres.
     * - El idioma no exceda 30 caracteres.
     * 
     * @param ficha la ficha a validar
     * @throws IllegalArgumentException si alguna validación falla
     * @throws Exception si hay error en la validación
     */
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
            
            // Validar que no exista ya ese ISBN
            if (fichaDAO.existeISBN(isbn)) {
                throw new IllegalArgumentException("Ya existe una ficha con el ISBN: "+ isbn);
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
    
    /**
     * Valida todas las reglas de negocio para actualizar una ficha existente.
     * Primero verifica que la ficha exista en la base de datos,
     * luego valida el formato del ISBN asegurándose de que no exista en otra ficha,
     * y finalmente valida las longitudes de los demás campos.
     * 
     * @param ficha la ficha a validar
     * @throws IllegalArgumentException si el ID es inválido o no existe
     * @throws Exception si alguna validación falla
     */
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
        
        // Validar ISBN si se esta actualizando
        if (ficha.getIsbn() != null && !ficha.getIsbn().trim().isEmpty()) {
            String isbn = ficha.getIsbn().trim();
            
            if (isbn.length() > 17) {
                throw new IllegalArgumentException("El ISBN no puede exceder 17 caracteres");
            }
            
            String isbnGuiones = isbn.replace("-", "");
            if (!isbnGuiones.matches("\\d{10}|\\d{13}")) {
                throw new IllegalArgumentException("El ISBN debe tener 10 o 13 digitos (sin contar guiones). Ejemplo: 978-3-16-148410-0");
            }
            
            // Validar que no exista ese ISBN en OTRA ficha
            if (fichaDAO.existeISBNExceptoId(isbn, ficha.getId())) {
                throw new IllegalArgumentException("Ya existe otra ficha con el ISBN: "+ isbn);
            }
            
        }
        
        // Aplicar las mismas validaciones que en inserción
        if (ficha.getClasificacionDewey() != null && ficha.getClasificacionDewey().length() > 20) {
            throw new IllegalArgumentException("La Clasificacion Dewey no puede exceder 20 caracteres");
        } 
        
        if (ficha.getEstanteria() != null && ficha.getEstanteria().length() > 20) {
            throw new IllegalArgumentException("La estanteria no puede exceder 20 caracteres");
        }
        
        if (ficha.getIdioma() != null && ficha.getIdioma().length() > 30) {
            throw new IllegalArgumentException ("El idioma no puede exceder 30 caracteres");
        }
        
    }
    
}
