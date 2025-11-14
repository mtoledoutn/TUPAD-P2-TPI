package progra2.Main;

import java.util.Scanner;
import progra2.DAO.FichaBibliograficaDAO;
import progra2.DAO.LibroDAO;
import progra2.Service.FichaBibliograficaService;
import progra2.Service.LibroService;

/** JAVADOC AQUÍ */
public class AppMenu {
    
    private final Scanner scanner;
    
    private final MenuHandler menuHandler;
    
    private boolean running;
    
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        FichaBibliograficaService fichaService = createFichaService();
        LibroService libroService = createLibroService(fichaService);
        
        this.menuHandler = new MenuHandler(scanner, libroService, fichaService);
        this.running = true;
    }
    
    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }
    
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
    
    
    private FichaBibliograficaService createFichaService(){
        FichaBibliograficaDAO fichaDAO =  new FichaBibliograficaDAO();
        return new FichaBibliograficaService(fichaDAO);
        
    }
    
    
    private LibroService createLibroService(FichaBibliograficaService fichaService) {
        LibroDAO libroDAO = new LibroDAO();
        return new LibroService(libroDAO, fichaService);
    }
    
}
