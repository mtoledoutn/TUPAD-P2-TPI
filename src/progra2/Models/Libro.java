
package progra2.Models;

/**
 *
 * @author martin
 */
public class Libro extends BaseModel {
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioEdicion; // Integer en lugar de int para permitir null.
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

    public Integer getAnioEdicion() {  //cambiado a Integer
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

    public void setAnioEdicion(Integer anioEdicion) {  //Cambiado a Integer
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
