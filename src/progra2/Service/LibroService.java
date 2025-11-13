package progra2.Service;

import java.util.List;
import progra2.DAO.GenericDAO;
import progra2.Models.Libro;

public class LibroService implements GenericService<Libro>{
        private final GenericDAO<Libro> libroDAO;
    
    public LibroService(GenericDAO<Libro> libroDAO){
        this.libroDAO = libroDAO;
    }

        @Override
    public void insertar(Libro libro) throws Exception{
        //validacion para evitar datos nulos
        if(libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()){
            throw new IllegalArgumentException("El titulo del libro no puede ser nulo");
        }
//revisar si es obligatorio que tenga autor para cargarlo
        if(libro.getAutor() == null || libro.getAutor().trim().isEmpty()){
            throw new IllegalArgumentException("El Autor del libro no puede ser nulo");
        }
        if(libro.getEditorial() == null || libro.getEditorial().trim().isEmpty()){
        throw new IllegalArgumentException("La Editorial del libro no puede ser nula."); // <-- AÑADIDO
        }
        if (libro.getFichaBibliografica() == null || libro.getFichaBibliografica().getId() <= 0) {
            throw new IllegalArgumentException("El libro debe tener una Ficha Bibliográfica válida asociada.");
        }

        // 2. Validación de REGLA DE NEGOCIO (opcional/condicional)
        // Se valida el año solo si NO es nulo
        if (libro.getAnioEdicion() != null && libro.getAnioEdicion() > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("El año de edición no puede ser posterior al año actual.");
        }
        System.out.println("Insertando libro: "+ libro.getTitulo());
        libroDAO.insertar(libro);
    }
        @Override
    public void actualizar(Libro entidad) throws Exception {
        libroDAO.actualizar(entidad);
    }
    
        @Override
    public void eliminar(int id) throws Exception {
        libroDAO.eliminar(id);
    }
    
        @Override
    public Libro getById(int id) throws Exception {
        return libroDAO.getById(id);
    }
    
        @Override
    public List <Libro> getAll() throws Exception {
        return libroDAO.getAll();
    }
    
}
