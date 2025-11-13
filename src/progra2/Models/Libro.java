package progra2.Models;

public class Libro extends BaseModel {
    
    /** JAVADOC AQUÍ */
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioEdicion;    // Integer en lugar de int para permitir null.
    private FichaBibliografica fichaBibliografica;
    
    /** JAVADOC AQUÍ */
    public Libro(int id, String titulo, String autor, String editorial, int anioEdicion, FichaBibliografica fichaBibliografica) {
        super(id, false);
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
        this.fichaBibliografica = fichaBibliografica;
    }
    
    /** JAVADOC AQUÍ */
    public Libro() {
        super();
    }
    
    /** JAVADOC AQUÍ */
    public String getTitulo() {
        return titulo;
    }
    
    /** JAVADOC AQUÍ */
    public String getAutor() {
        return autor;
    }
    
    /** JAVADOC AQUÍ */
    public String getEditorial() {
        return editorial;
    }
    
    /** JAVADOC AQUÍ */
    public Integer getAnioEdicion() {  //cambiado a Integer
        return anioEdicion;
    }
    
    /** JAVADOC AQUÍ */
    public FichaBibliografica getFichaBibliografica() {
        return fichaBibliografica;
    }
    
    /** JAVADOC AQUÍ */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    /** JAVADOC AQUÍ */
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    /** JAVADOC AQUÍ */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    
    /** JAVADOC AQUÍ */
    public void setAnioEdicion(Integer anioEdicion) {    //Cambiado a Integer
        this.anioEdicion = anioEdicion;
    }
    
    /** JAVADOC AQUÍ */
    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }
    
    /** JAVADOC AQUÍ */
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
