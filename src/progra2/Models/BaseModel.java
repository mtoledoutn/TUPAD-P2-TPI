package progra2.Models;

/**
 * Clase abstracta base para todas las entidades del dominio.
 * Proporciona atributos comunes como ID y estado de eliminación lógica.
 * Todas las entidades del sistema deben heredar de esta clase.
 */
public abstract class BaseModel {
    
    /** Identificador único de la entidad en la base de datos. */
    private int id;
    /** Indica si la entidad fue eliminada lógicamente (soft delete). */
    private boolean eliminado;
    
    /**
     * Constructor con parámetros para inicializar una entidad con ID.
     * 
     * @param id identificador de la entidad
     * @param eliminado estado de eliminación lógica
     */
    protected BaseModel(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }
    
    /** Constructor sin parámetros que inicializa eliminado en false. */
    protected BaseModel() {
        this.eliminado = false;
    }
    
    /**
     * Obtiene el identificador único de la entidad.
     * 
     * @return ID de la entidad
     */
    public int getId() {
        return id;
    }
    
    /**
     * Establece el identificador único de la entidad.
     * 
     * @param id nuevo ID a asignar
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Verifica si la entidad fue eliminada lógicamente.
     * 
     * @return true si está eliminada, false en caso contrario
     */
    public boolean isEliminado() {
        return eliminado;
    }
    
    /**
     * Establece el estado de eliminación lógica de la entidad.
     * 
     * @param eliminado true para marcar como eliminada, false en caso contrario
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
}
