package progra2.Models;

public class FichaBibliografica extends BaseModel {
    
    /** JAVADOC AQUÍ */
    private String isbn;
    /** JAVADOC AQUÍ */
    private String clasificacion_dewey;
    /** JAVADOC AQUÍ */
    private String estanteria;
    /** JAVADOC AQUÍ */
    private String idioma;

    /** JAVADOC AQUÍ */
    public FichaBibliografica(String isbn, String clasificacionDewey, String estanteria, String idioma, int id, boolean eliminado) {
        super(id, false);
        this.isbn = isbn;
        this.clasificacion_dewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }
    
    /** JAVADOC AQUÍ */
    public FichaBibliografica() {
        super();
    }
    
    /** JAVADOC AQUÍ */
    public String getIsbn() {
        return isbn;
    }
    
    /** JAVADOC AQUÍ */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    /** JAVADOC AQUÍ */
    public String getClasificacionDewey() {
        return clasificacion_dewey;
    }
    
    /** JAVADOC AQUÍ */
    public void setClasificacionDewey(String clasificacionDewey) {
        this.clasificacion_dewey = clasificacionDewey;
    }
    
    /** JAVADOC AQUÍ */
    public String getEstanteria() {
        return estanteria;
    }
    
    /** JAVADOC AQUÍ */
    public void setEstanteria(String estanteria) {
        this.estanteria = estanteria;
    }
    
    /** JAVADOC AQUÍ */
    public String getIdioma() {
        return idioma;
    }
    
    /** JAVADOC AQUÍ */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    /** JAVADOC AQUÍ */
    @Override
    public String toString() {
        return "Ficha Bibliografica{" +
                "id=" + getId() +
                ", isbn='" + isbn + '\'' +
                ", clasificacion_dewey='" + clasificacion_dewey + '\'' +
                ", estanteria='" + estanteria + '\'' +
                ", idioma='" + idioma + '\'' +
                ", eliminado=" + isEliminado() +
                '}';
    }
    
}
