package progra2.DAO;

import java.util.List;

/**
 * Interfaz genérica que define métodos comunes para trabajar con cualquier entidad.
 * Sirve como base para evitar repetir código en distintas clases DAO.
 *
 * @param <T>  Tipo de entidad manejada por la implementación.
 */
public interface GenericDAO<T> {
    
    void insertar(T entidad) throws Exception;
    void actualizar(T entidad) throws Exception;
    void eliminar(int id) throws Exception;
    T getById(int id) throws Exception;
    List<T> getAll() throws Exception;
    
}
