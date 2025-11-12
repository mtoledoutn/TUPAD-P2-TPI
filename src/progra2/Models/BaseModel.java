package progra2.Models;

public abstract class BaseModel {
    
    // Atributos
    private int id;
    private boolean eliminado;
    
    // Constructor
    protected BaseModel(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }
    
    protected BaseModel() {
        this.eliminado = false;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isEliminado() {
        return eliminado;
    }
    
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
}
