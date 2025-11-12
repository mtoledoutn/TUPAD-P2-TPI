/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import progra2.Config.DatabaseConnection;
import progra2.Models.Libro;
import progra2.Models.FichaBibliografica;

/**
 *
 * @author met
 */
public class LibroDAO implements GenericDAO<Libro> {
    
    private static final String INSERT_SQL = "INSERT INTO libros (titulo, autor, editorial, anioEdicion, fichaBibliograficaId) VALUES (?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SQL = "UPDATE libros SET titulo = ?, autor = ?, editorial = ?, anioEdicion = ?, fichaBibliograficaId = ? WHERE id = ?";
    
    private static final String DELETE_SQL = "UPDATE libros SET eliminado = TRUE WHERE id = ?";
    
    private static final String SELECT_BY_ID_SQL = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anioEdicion, " +
            "f.id AS ficha_id, f.clasificacionDewey, f.estanteria, f.idioma " +
            "FROM libros l LEFT JOIN ficha_bibliografica f ON l.fichaBibliograficaId = f.id " +
            "WHERE l.id = ? AND l.eliminado = FALSE";
    
    private static final String SELECT_ALL_SQL = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anioEdicion " +
            "f.id AS ficha_id, f.clasificacionDewey, f.estanteria, f.idioma " +
            "FROM libros l LEFT JOIN ficha_bibliografica f ON l.fichaBibliograficaId = f.id " +
            "WHERE l.eliminado = FALSE";
    
    // IMPLEMENTAR SEARCH BY DISTINTAS COLUMNAS

    @Override
    public void insertar(Libro libro) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
            setLibrosParameters(stmt, libro);
            stmt.executeUpdate();
            
            setGeneratedId(stmt,libro);
          }
        }

        @Override
        public void insertTx(Libro libro, Connection conn) throws Exception {
          try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
            setLibrosParameters(stmt,libro);
            stmt.executeUpdate();
            setGeneratedId(stmt,libro);
        }
        }

        @Override
        public void actualizar(Libro libro) throws Exception {
           try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
                setLibrosParameters(stmt,libro);
                stmt.setInt(6,libro.getId());
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0){
                    throw new SQLException("No se pudo actualizar libro con ID:" + libro.getId());
                }
                        
                
            }
        }

        @Override
        public void eliminar(int id) throws Exception {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)){
                stmt.setInt(1,id);
                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("No se encontro libro con ID: " + id); 
                }
                
            }
        }

        @Override
        public Libro getById(int id) throws Exception {
            try(Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)){
                stmt.setInt(1,id);
                try (ResultSet rs = stmt.executeQuery()){
                    if(rs.next()){
                        return mapResulSetToLibro(rs);
                    }
                }
            }
            return null;
        }

        @Override
        public List<Libro> getAll() throws Exception {
            List<Libro> libros = new ArrayList();
            
            try (Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)){
                       while (rs.next()){
                           libros.add(mapResulSetToLibro(rs));
                       }
                            
                    }
                return libros;
        }

        
        
        public List<Libro> getByEditorial(String editorial) throws Exception{
            List<Libro> libros = new ArrayList();
            
            String sql = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anioEdicion, " + 
                    "f.id AS ficha_id, f.clasificacionDeWey, f.estanteria, f.idioma" +
                    "FROM libros l LEFT JOIN ficha_bibliografica f ON l.fichaBibliograficaId = f.id " +
                    "WHERE l.editorial = ? AND l.eliminado = FALSE";
            
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1,editorial);
                
                try (ResultSet rs = stmt.executeQuery()){
                    while (rs.next()){
                        libros.add(mapResulSetToLibro(rs));
                    }
                }
            }
            return libros;
        }
        
        public List<Libro> getByIdioma(String idioma) throws Exception{
            List<Libro> libros = new ArrayList();
            
            String sql = "SELECT l.id, l.titulo, l.autor, l.editorial, l.anioEdicion, " +
                 "f.id AS ficha_id, f.clasificacionDewey, f.estanteria, f.idioma " +
                 "FROM libros l LEFT JOIN ficha_bibliografica f ON l.fichaBibliograficaId = f.id " +
                 "WHERE f.idioma = ? AND l.eliminado = FALSE";
            
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1,idioma);
                
                try (ResultSet rs = stmt.executeQuery()){
                    while (rs.next()){
                        libros.add(mapResulSetToLibro(rs));
                    }
                }
            }
            return libros;
        }
        
            
      //Metodos Auxiliares
        
        private void setLibrosParameters(PreparedStatement stmt, Libro libro) throws SQLException{
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getEditorial());
            stmt.setInt(4, libro.getAnioEdicion());
            stmt.setInt(5, libro.getFichaBibliografica().getId());
        }
        
        
        private void setGeneratedId(PreparedStatement stmt, Libro libro) throws SQLException{
            try(ResultSet Keys = stmt.getGeneratedKeys()){
                if(Keys.next()){
                    libro.setId(Keys.getInt(1));
                }else{
                    throw new SQLException("No puedo obtener el ID generado del libro.");
                }
            }
        }
         
        private Libro mapResulSetToLibro(ResultSet rs) throws SQLException{
            FichaBibliografica ficha = new FichaBibliografica(
            null,
            rs.getString("Clasificacion De Wey"),
            rs.getString("estanteria"),
            rs.getString("idioma"),
            rs.getInt("Ficha_id"),
            false
            );
            
            return new Libro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("editorial"),
            rs.getInt("anioEdicion"),
            ficha
            );
            
            
        }
        
 }

