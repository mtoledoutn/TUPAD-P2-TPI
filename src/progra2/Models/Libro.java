package progra2.Models;

public class Libro extends BaseModel {
    
    /** JAVADOC AQUÍ */
    private String titulo;
    /** JAVADOC AQUÍ */
    private String autor;
    /** JAVADOC AQUÍ */
    private String editorial;
    /** JAVADOC AQUÍ */
    private Integer anioEdicion;
    /** JAVADOC AQUÍ */
    private FichaBibliografica fichaBibliografica;
    
    /** 
     * Constructor completo para crear un libro con todos sus datos.
     * @param id identificador del libro
     * @param titulo titulo del libro (obligatorio)
     * @param autor autor del libro (obligatorio)
     * @param editorial editorial del libro (opcional)
     * @param anioEdicion año de edición (opcional, puede ser null)
     * @param fichaBibliografica ficha bibliográfica asociada (opcional)
     */
    public Libro(int id, String titulo, String autor, String editorial, Integer anioEdicion, FichaBibliografica fichaBibliografica) {
        super(id, false);
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
        this.fichaBibliografica = fichaBibliografica;
    }
    
    /** Constructor vacío para instanciación sin datos. */
    public Libro() {
        super();
    }
    
    /** Obtiene el título del libro.
     * @return  Título del libro
     */
    public String getTitulo() {
        return titulo;
    }
    
    /**
     * Establece el titulo al libro.
     * 
     * @param titulo Nuevo título del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    /** Obtiene el autor del libro.
     * @return  Autor del libro
     */
    public String getAutor() {
        return autor;
    }
    
    /**
     * Establece el autor al libro.
     * 
     * @param autor Nuevo autor del libro
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    /** Obtiene el editorial del libro.
     * @return  Editorial del libro
     */
    public String getEditorial() {
        return editorial;
    }
    
    /**
     * Establece el editorial al libro.
     * 
     * @param editorial Nuevo editorial del libro
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    
    /** Obtiene el año de edición del libro.
     * @return  Año de edición del libro
     */
    public Integer getAnioEdicion() {
        return anioEdicion;
    }
    
    /**
     * Establece el año de edición al libro.
     * 
     * @param anioEdicion Nuevo autor del libro
     */
    public void setAnioEdicion(Integer anioEdicion) {
        this.anioEdicion = anioEdicion;
    }
    
    /** Obtiene la ficha bibliográfica de un libro.
     * @return  Ficha bibliografica de un libro
     */
    public FichaBibliografica getFichaBibliografica() {
        return fichaBibliografica;
    }
    
    /**
     * Establece la ficha bibliográfica al libro.
     * 
     * @param fichaBibliografica Nueva ficha bibliográfica del libro
     */
    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }
    
    /**
     * Representación en texto del libro.
     * Útil para debugging y logging.
     *
     * @return String con todos los campos del libro
     */
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
