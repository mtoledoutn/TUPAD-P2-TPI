package progra2.Main;

import java.util.Scanner;
import progra2.DAO.FichaBibliograficaDAO;
import progra2.DAO.LibroDAO;
import progra2.Service.FichaBibliograficaService;
import progra2.Service.LibroService;

/**
 * Controlador principal del sistema.
 * Administra el flujo de navegación, inicializa servicios y procesa
 * las operaciones del menú general y del menú de libros.
 */
public class AppMenu {
    
    /** Scanner para leer entrada del usuario desde consola. */
    private final Scanner scanner;
    
    /** Manejador que gestiona las acciones seleccionadas por el usuario. */
    private final MenuHandler menuHandler;
    
    /** Bandera que controla el bucle principal de la aplicación. */
    private boolean running;
    
    /**
     * Constructor que inicializa el escáner, servicios y estado de ejecución.
     * Configura la inyección de dependencias entre DAOs y Services.
     */
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        FichaBibliograficaService fichaService = createFichaService();
        LibroService libroService = createLibroService(fichaService);
        
        this.menuHandler = new MenuHandler(scanner, libroService, fichaService);
        this.running = true;
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
                    processMainOption(opcion);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                }
            }
        }
    }
    
    /** Método que procesa una opcion del menu principal. */
    private void processMainOption(int opcion) {
        switch (opcion) {
            case 1 -> gestionarLibros();
            case 2 -> verificarConexion();
            case 0 -> {
                System.out.println("Saliendo del sistema...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }
    
    /** Método que controla la navegación dentro del menú de libros. */
    private void gestionarLibros() {
        boolean enMenuLibros = true;
        while (enMenuLibros) {
            try {
                MenuDisplay.mostrarMenuLibros();
                int opcion = Integer.parseInt(scanner.nextLine());
                enMenuLibros = processLibroOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            }
        }
    }
    
    /** Método que procesa las opciones del menú de libros. */
    private boolean processLibroOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearLibro();
            case 2 -> menuHandler.listarLibros();
            case 3 -> menuHandler.actualizarLibro();
            case 4 -> menuHandler.eliminarLibro();
            case 0 -> {
                return false;
            }
            default -> System.out.println("Opcion no valida.");
        }
        return true;
    }
    
    /** Método que verifica directamente la conexión a la BD. */
    private void verificarConexion() {
        menuHandler.verificarConexion();
    }
    
    /** Factoría de servicio de ficha bibliográfica. */
    private FichaBibliograficaService createFichaService(){
        FichaBibliograficaDAO fichaDAO =  new FichaBibliograficaDAO();
        return new FichaBibliograficaService(fichaDAO);
    }
    
    /** Factoría de servicio de libro. */
    private LibroService createLibroService(FichaBibliograficaService fichaService) {
        LibroDAO libroDAO = new LibroDAO();
        return new LibroService(libroDAO, fichaService);
    }
    
}
