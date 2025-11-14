package progra2.Main;

import java.util.Scanner;
import progra2.DAO.FichaBibliograficaDAO;
import progra2.DAO.LibroDAO;
import progra2.Service.FichaBibliograficaService;
import progra2.Service.LibroService;

/**
 * Controlador principal de la aplicación que gestiona el menú y el flujo de navegación.
 * Inicializa los servicios necesarios y coordina las operaciones del usuario.
 */
public class AppMenu {
    
    /** Scanner para leer entrada del usuario desde consola */
    private final Scanner scanner;
    
    /** Manejador que procesa las operaciones del menú */
    private final MenuHandler menuHandler;
    
    /** Bandera que controla el bucle principal de la aplicación */
    private boolean running;
    
    /**
     * Constructor que inicializa el escáner, servicios y estado de ejecución.
     * Configura la inyección de dependencias entre DAOs y Services.
     */
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        LibroService libroService = createLibroService();
        this.menuHandler = new MenuHandler(scanner, libroService);
        this.running = true;
    }
    
    /**
     * Método main alternativo para ejecutar la aplicación desde esta clase.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
    
    /**
     * Bucle principal de la aplicación que muestra el menú y procesa opciones.
     * Gestiona errores de entrada inválida y mantiene la aplicación en ejecución
     * hasta que el usuario seleccione salir.
     */
    public void run() {
        try (scanner) {
            while (running) {
                try {
                    MenuDisplay.mostrarMenuPrincipal();
                    int opcion = Integer.parseInt(scanner.nextLine());
                    processOption(opcion);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                }
            }
        }
    }
    
    /**
     * Procesa la opción seleccionada por el usuario y ejecuta la acción correspondiente.
     * 
     * @param opcion número de la opción del menú seleccionada
     */
    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearLibro();
            case 2 -> menuHandler.listarLibros();
            case 3 -> menuHandler.actualizarLibro();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }
    
    /**
     * Factory method que crea e inyecta las dependencias necesarias para LibroService.
     * 
     * @return instancia configurada de LibroService con sus dependencias
     */
    private LibroService createLibroService() {
        FichaBibliograficaDAO fichaBibliograficaDAO = new FichaBibliograficaDAO();
        LibroDAO libroDAO = new LibroDAO();
        FichaBibliograficaService fichaBibliograficaService = new FichaBibliograficaService(fichaBibliograficaDAO);
        return new LibroService(libroDAO, fichaBibliograficaService);
    }
    
}
