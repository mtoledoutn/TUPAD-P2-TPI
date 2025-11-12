package progra2.Models;

public class FichaBibliografica extends BaseModel {
    
    // Atributos
    private String isbn;
    private String clasificacion_dewey;
    private String estanteria;
    private String idioma;

    // Constructor
    public FichaBibliografica(String isbn, String clasificacionDewey, String estanteria, String idioma, int id, boolean eliminado) {
        super(id, false);
        this.isbn = isbn;
        this.clasificacion_dewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }
    
    public FichaBibliografica() {
        super();
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getClasificacionDewey() {
        return clasificacion_dewey;
    }
    
    public void setClasificacionDewey(String clasificacionDewey) {
        this.clasificacion_dewey = clasificacionDewey;
    }
    
    public String getEstanteria() {
        return estanteria;
    }
    
    public void setEstanteria(String estanteria) {
        this.estanteria = estanteria;
    }
    
    public String getIdioma() {
        return idioma;
    }
    
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
