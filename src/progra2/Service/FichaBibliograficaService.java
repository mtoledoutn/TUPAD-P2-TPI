package progra2.Service;

import java.util.List;
import progra2.DAO.FichaBibliograficaDAO;
import progra2.DAO.GenericDAO;
import progra2.Models.FichaBibliografica;

public class FichaBibliograficaService implements GenericService<FichaBibliografica>{
    private final GenericDAO<FichaBibliografica> fichaDAO;
    
    public FichaBibliograficaService(GenericDAO<FichaBibliografica> fichaDAO) {
        this.fichaDAO = fichaDAO;
    }
    
    @Override
    public void insertar(FichaBibliografica ficha) throws Exception {
        // Validación de Lógica de Negocio: Campos Obligatorios

        if (ficha.getIsbn() == null || ficha.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede ser nulo o vacío.");
        }
        if (ficha.getClasificacionDewey() == null || ficha.getClasificacionDewey().trim().isEmpty()) {
            throw new IllegalArgumentException("La Clasificación Dewey es obligatoria.");
        }
        if (ficha.getIdioma() == null || ficha.getIdioma().trim().isEmpty()) {
            throw new IllegalArgumentException("El idioma de la ficha no puede ser nulo.");
        }
        
        // Validación adicional (ejemplo: longitud mínima de ISBN)
        if (ficha.getIsbn().length() < 10) {
            throw new IllegalArgumentException("El ISBN debe tener al menos 10 caracteres.");
        }

        System.out.println("Insertando Ficha Bibliográfica con ISBN: " + ficha.getIsbn());
        
        // Delegación al DAO
        fichaDAO.insertar(ficha);
    }
    @Override
    public void actualizar(FichaBibliografica entidad) throws Exception {
        // Aquí se podría añadir validaciones para asegurarse de que el ID exista, 
        // y repetir las validaciones de campos nulos/vacíos antes de delegar.
        
        System.out.println("Actualizando Ficha Bibliográfica con ID: " + entidad.getId());
        fichaDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {
        // En un caso real, se podría verificar si existen libros asociados antes de eliminar, 
        // pero por ahora, solo delegamos.
        
        System.out.println("Eliminando Ficha Bibliográfica con ID: " + id);
        fichaDAO.eliminar(id);
    }

    @Override
    public FichaBibliografica getById(int id) throws Exception {
        // Delegación pura
        return fichaDAO.getById(id);
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        // Delegación pura
        return fichaDAO.getAll();
    }
}

