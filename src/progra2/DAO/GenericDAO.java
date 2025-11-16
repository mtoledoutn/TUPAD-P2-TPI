package progra2.DAO;

import java.util.List;

/**
 * Interfaz genérica que define operaciones CRUD estándar para cualquier entidad.
 * Establece el contrato base que deben implementar todos los DAOs del sistema,
 * promoviendo consistencia y reutilización de código.
 *
 * @param <T> tipo de entidad manejada por la implementación
 */
public interface GenericDAO<T> {
    
    /**
     * Inserta una nueva entidad en la base de datos.
     * 
     * @param entidad la entidad a insertar
     * @throws Exception si hay error en la inserción
     */
    void insertar(T entidad) throws Exception;
    
    /**
     * Actualiza una entidad existente en la base de datos.
     * 
     * @param entidad la entidad con datos actualizados
     * @throws Exception si hay error en la actualización
     */
    void actualizar(T entidad) throws Exception;
    
    /**
     * Elimina una entidad por su ID (típicamente eliminación lógica).
     * 
     * @param id identificador de la entidad
     * @throws Exception si hay error en la eliminación
     */
    void eliminar(int id) throws Exception;
    
    /**
     * Obtiene una entidad por su ID.
     * 
     * @param id identificador de la entidad
     * @return la entidad encontrada o null si no existe
     * @throws Exception si hay error en la consulta
     */
    T getById(int id) throws Exception;
    
    /**
     * Obtiene todas las entidades no eliminadas de este tipo.
     * 
     * @return lista de entidades
     * @throws Exception si hay error en la consulta
     */
    List<T> getAll() throws Exception;
    
}
