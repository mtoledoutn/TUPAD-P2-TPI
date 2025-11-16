package progra2.Models;

/**
 * Modelo de dominio que representa una ficha bibliográfica.
 * Contiene información complementaria de catalogación de un libro como ISBN,
 * clasificación Dewey, ubicación física y idioma.
 * Todos los campos son opcionales excepto el ID heredado.
 */
public class FichaBibliografica extends BaseModel {
    
    /** ISBN del libro (opcional). */
    private String isbn;
    /** Clasificación Dewey (opcional). */
    private String clasificacion_dewey;
    /** Ubicación en estantería (opcional). */
    private String estanteria;
    /** Idioma del libro (opcional). */
    private String idioma;

    /**
     * Constructor completo para crear una ficha bibliográfica.
     * 
     * @param isbn código ISBN
     * @param clasificacionDewey clasificación decimal Dewey
     * @param estanteria ubicación física
     * @param idioma idioma del contenido
     * @param id identificador
     * @param eliminado estado de eliminación lógica
     */
    public FichaBibliografica(String isbn, String clasificacionDewey, String estanteria, String idioma, int id, boolean eliminado) {
        super(id, eliminado);
        this.isbn = isbn;
        this.clasificacion_dewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }
    
    /** Constructor vacío para instanciación sin datos. */
    public FichaBibliografica() {
        super();
    }
    
    /** Obtiene el ISBN de la ficha.
     * 
     * @return  ISBN de la ficha
     */
    public String getIsbn() {
        return isbn;
    }
    
    /**
     * Establece el ISBN a la ficha.
     * 
     * @param isbn Nuevo ISBN de la ficha
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    /** Obtiene la clasificación dewey de la ficha.
     * 
     * @return  Clasificación Dewey de la ficha
     */
    public String getClasificacionDewey() {
        return clasificacion_dewey;
    }
    
    /**
     * Establece la clasificación dewey a la ficha.
     * 
     * @param clasificacionDewey Nueva clasificación dewey de la ficha
     */
    public void setClasificacionDewey(String clasificacionDewey) {
        this.clasificacion_dewey = clasificacionDewey;
    }
    
    /** Obtiene el número de ubicación física en estantería de la ficha.
     * @return  Estantería de la ficha
     */
    public String getEstanteria() {
        return estanteria;
    }
    
    /**
     * Establece el número de ubicación física en estantería a la ficha.
     * 
     * @param estanteria Nuevo número de estantería de la ficha
     */
    public void setEstanteria(String estanteria) {
        this.estanteria = estanteria;
    }
    
    /** Obtiene el idioma de la ficha.
     * @return  Idioma de la ficha
     */
    public String getIdioma() {
        return idioma;
    }
    
    /**
     * Establece el idioma a la ficha.
     * 
     * @param idioma Nuevo idioma de la ficha
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    /**
     * Representación en texto de la ficha bibliográfica.
     * Útil para debugging y logging.
     *
     * @return String con todos los campos de la ficha bibliográfica
     */
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
