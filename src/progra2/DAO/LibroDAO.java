/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.DAO;

import progra2.Models.Libro;

/**
 *
 * @author met
 */
public class LibroDAO implements GenericDAO<Libro> {
    
    private static final String INSERT_SQL = "INSERT INTO libros (titulo, autor, editorial, anioEdicion, fichaBibliograficaId) VALUES (?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SQL = "UPDATE libros SET titulo = ?, autor = ?, editorial = ?, anioEdicion = ?, fichaBibliograficaId = ? WHERE id = ?";
    
    private static final String DELETE_SQL = "UPDATE libros SET eliminado = TRUE WHERE id = ?";
    
    private static final String SELECT_BY_ID_SQL = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anioEdicion " +
            "f.id AS ficha_id, f.clasificacionDewey, f.estanteria, f.idioma " +
            "FROM libros l LEFT JOIN fichaBibliografica f ON p.fichaBibliograficaId = f.id " +
            "WHERE l.id = ? AND l.eliminado = FALSE";
    
    private static final String SELECT_ALL_SQL = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anioEdicion " +
            "f.id AS ficha_id, f.clasificacionDewey, f.estanteria, f.idioma " +
            "FROM libros l LEFT JOIN fichaBibliografica f ON p.fichaBibliograficaId = f.id " +
            "WHERE l.eliminado = FALSE";
    
    // IMPLEMENTAR SEARCH BY DISTINTAS COLUMNAS
    
    

    
    
}
