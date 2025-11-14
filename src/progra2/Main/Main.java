package progra2.Main;

/**
 * Clase principal que inicia la aplicación del sistema de gestión de biblioteca.
 * Simplemente delega la ejecución al menú de la aplicación.
 */
public class Main {
    
    /**
     * Punto de entrada de la aplicación.
     * Crea una instancia de AppMenu y ejecuta el bucle principal.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {   
        AppMenu app = new AppMenu();
        app.run();
    }
    
}
