package progra2.Models;

public abstract class BaseModel {
    
    /** Identificador único de la entidad en la base de datos. */
    private int id;
    /** Indica si la entidad fue eliminada lógicamente (soft delete). */
    private boolean eliminado;
    
    /**
     * Constructor con parámetros para inicializar una entidad con ID.
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
    
    /** JAVADOC AQUÍ */
    public int getId() {
        return id;
    }
    
    /** JAVADOC AQUÍ */
    public void setId(int id) {
        this.id = id;
    }
    
    /** JAVADOC AQUÍ */
    public boolean isEliminado() {
        return eliminado;
    }
    
    /** JAVADOC AQUÍ */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
}
