
package progra2.Service;

import java.util.List;

//GenericService estandariza operaciones para los demas service
public interface GenericService <T>{
    //Create
    void insertar (T entidad) throws Exception;
    //Update
    void actualizar (T entidad) throws Exception;
    //Delete
    void eliminar (int id) throws Exception;
    //Read
    T getById (int id) throws Exception;
    //Read
    List<T> getAll() throws Exception;
    
}
