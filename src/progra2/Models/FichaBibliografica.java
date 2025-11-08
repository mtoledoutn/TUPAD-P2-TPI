/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.Models;

/**
 *
 * @author martin
 */
public class FichaBibliografica extends BaseModel {
    private String isbn;
    private String clasificacionDewey;
    private String estanteria;
    private String idioma;

    public FichaBibliografica(String isbn, String clasificacionDewey, String estanteria, String idioma, int id, boolean eliminado) {
        super(id, eliminado);
        this.isbn = isbn;
        this.clasificacionDewey = clasificacionDewey;
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
        return clasificacionDewey;
    }

    public void setClasificacionDewey(String clasificacionDewey) {
        this.clasificacionDewey = clasificacionDewey;
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
    
    @Override
    public String toString() {
        return "Ficha Bibliografica{" +
                "id=" + getId() +
                ", isbn='" + isbn + '\'' +
                ", clasificacionDewey='" + clasificacionDewey + '\'' +
                ", estanteria='" + estanteria + '\'' +
                ", idioma='" + idioma + '\'' +
                ", eliminado=" + isEliminado() +
                '}';
    }
}
