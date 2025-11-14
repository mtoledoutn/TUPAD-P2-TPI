package progra2.Main;

/**
 * Clase utilitaria responsable de mostrar la interfaz de menú en consola.
 * Centraliza la presentación visual del menú principal de la aplicación.
 */
public class MenuDisplay {
    
    /**
     * Muestra el menú principal con todas las opciones disponibles.
     * Incluye operaciones CRUD básicas para libros y opción de salida.
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENU - Libros =========");
        System.out.println("1. Crear libro");
        System.out.println("2. Listar libros");
        System.out.println("3. Actualizar libro");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opcion: ");
    }
    
}
