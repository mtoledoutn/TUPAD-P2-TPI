/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.Models;

/**
 *
 * @author martin
 */
public class Libro extends BaseModel {
    private String titulo;
    private String autor;
    private String editorial;
    private int anioEdicion;
    private FichaBibliografica fichaBibliografica;
    
    public Libro(int id, String titulo, String autor, String editorial, int anioEdicion, FichaBibliografica fichaBibliografica) {
        super(id, false);
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
        this.fichaBibliografica = fichaBibliografica;
    }
    
    public Libro() {
        super();
    }
    
    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public int getAnioEdicion() {
        return anioEdicion;
    }

    public FichaBibliografica getFichaBibliografica() {
        return fichaBibliografica;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setAnioEdicion(int anioEdicion) {
        this.anioEdicion = anioEdicion;
    }

    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + getId() +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", editorial='" + editorial + '\'' +
                ", anioEdicion=" + anioEdicion +
                ", fichaBibliografica=" + fichaBibliografica +
                ", eliminado=" + isEliminado() +
                '}';
    } 
}
