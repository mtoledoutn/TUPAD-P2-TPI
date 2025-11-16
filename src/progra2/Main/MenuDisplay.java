package progra2.Main;

/**
 * Clase encargada de renderizar los menús de la aplicación en consola.
 * Centraliza únicamente la presentación visual, sin contener lógica de negocio.
 */
public class MenuDisplay {
    
    /** 
     * Muestra el menú principal con las opciones iniciales del sistema.
     * 
     * Opciones:
     * 1. Gestion de libros
     * 2. Verificar conexión a la Base de Datos
     * 0. Salir de la aplicación
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= SISTEMA DE GESTION DE BIBLIOTECA =========");
        System.out.println("1. Gestionar Libros");
        System.out.println("2. Verificar conexion a BD");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opcion: ");
    }
    
    /**
     * Muestra el menú específico para la gestión de libros con sus operaciones CRUD básicas.
     * 
     * Opciones:
     * 1. Crear
     * 2. Listar/Buscar
     * 3. Actualizar
     * 4. Eliminar
     * 0. Volver al menú principal
     */
    public static void mostrarMenuLibros() {
        System.out.println("\n========= MENU - Libros =========");
        System.out.println("1. Crear libro");
        System.out.println("2. Listar/Buscar libros");
        System.out.println("3. Actualizar libro");
        System.out.println("4. Eliminar libro");
        System.out.println("0. Volver al menu principal");
        System.out.print("Ingrese una opcion: ");
    }
    
}
