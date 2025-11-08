/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.Models;

/**
 *
 * @author martin
 */
public abstract class BaseModel {
    
    private int id;
    private boolean eliminado;
    
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
