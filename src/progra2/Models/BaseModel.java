package progra2.Models;

public abstract class BaseModel {
    
    /** JAVADOC AQUÍ */
    private int id;
    /** JAVADOC AQUÍ */
    private boolean eliminado;
    
    /** JAVADOC AQUÍ */
    protected BaseModel(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }
    
    /** JAVADOC AQUÍ */
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
