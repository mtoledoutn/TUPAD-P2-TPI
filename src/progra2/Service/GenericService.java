
package progra2.Service;

import java.util.List;

/**
 * Interfaz genérica que define operaciones de negocio estándar para cualquier entidad.
 * Establece el contrato base que deben implementar todos los servicios del sistema.
 * Los servicios encapsulan reglas de negocio y validaciones antes de delegar a los DAOs.
 *
 * @param <T> tipo de entidad manejada por la implementación
 */
public interface GenericService <T>{
    
    /**
     * Crea una nueva entidad en el sistema.
     * Aplica validaciones de negocio antes de persistir.
     * 
     * @param entidad la entidad a crear
     * @throws Exception si hay error en validación o creación
     */
    void insertar (T entidad) throws Exception;
    
    /**
     * Actualiza una entidad existente en el sistema.
     * Aplica validaciones de negocio antes de persistir.
     * 
     * @param entidad la entidad con datos actualizados
     * @throws Exception si hay error en validación o actualización
     */
    void actualizar (T entidad) throws Exception;
    
    /**
     * Elimina una entidad del sistema por su ID.
     * 
     * @param id identificador de la entidad
     * @throws Exception si hay error en la eliminación
     */
    void eliminar (int id) throws Exception;
    
    /**
     * Obtiene una entidad por su identificador.
     * 
     * @param id identificador de la entidad
     * @return la entidad encontrada o null si no existe
     * @throws Exception si hay error en la consulta
     */
    T getById (int id) throws Exception;
    
    /**
     * Obtiene todas las entidades del sistema.
     * 
     * @return lista de entidades no eliminadas
     * @throws Exception si hay error en la consulta
     */
    List<T> getAll() throws Exception;
    
}
