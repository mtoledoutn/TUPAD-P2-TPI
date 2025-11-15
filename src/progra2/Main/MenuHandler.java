package progra2.Main;

import java.util.List;
import java.util.Scanner;
import progra2.Models.FichaBibliografica;
import progra2.Models.Libro;
import progra2.Service.FichaBibliograficaService;
import progra2.Service.LibroService;

/**
 * Controlador de operaciones del menú que procesa las interacciones del usuario.
 * Funciona como puente entre la capa de presentación (menu) y la capa de negocio (services).
 * Maneja validación de entrada y bucles de reintento.
 */
public class MenuHandler {
    
    /** Scanner para capturar entrada del usuario. */
    private final Scanner scanner;
    
    /** Servicio de libros que encapsula la lógica de negocio. */
    private final LibroService libroService;
    
    /** Servicio para operaciones relacionadas con fichas bibliográficas. */
    private final FichaBibliograficaService fichaService;
    
    /**
     * Constructor que recibe los servicios necesarios.
     *
     * @param scanner scanner compartido para entrada de datos
     * @param libroService servicio de libros
     * @param fichaService servicio de fichas bibliográficas
     */
    public MenuHandler(Scanner scanner, LibroService libroService, FichaBibliograficaService fichaService) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        if (libroService == null) {
            throw new IllegalArgumentException("LibroService no puede ser null");
        }
        if (fichaService == null) {
            throw new IllegalArgumentException("FichaService no puede ser null");
        }
        this.scanner = scanner;
        this.libroService = libroService;
        this.fichaService = fichaService;
    }
    
    /** Método que verifica la conexión a la base de datos. */
    public void verificarConexion() {
        TestConexion.main(null);
    }
    
    /**
     * Flujo interactivo para crear un nuevo libro con su ficha bibliográfica.
     * Captura todos los datos necesarios y persiste el libro en la base de datos.
     * La operación es atómica: si falla, no se guarda nada.
     * Mejorado : Usa transacciones para asegurar atomicidad
     */
    public void crearLibro() {
        try {
            System.out.println("\n========= CREAR LIBRO =========");
            
            String titulo = leerTexto("Titulo", true);
            String autor = leerTexto("Autor", true);
            String editorial = leerTexto("Editorial", false);
            Integer anioEdicion = leerAnioEdicion();
            
            // Opción: crear libro con o sin ficha
            System.out.print("¿Desea crear una ficha bibliografica para el libro? (S/N): ");
            String respuesta = scanner.nextLine().trim().toUpperCase();
            
            if (respuesta.equals("S")) {
                //Caso1 : Libro con ficha -> Usar Transaccion 
                System.out.println("\n--- Creando ficha bibliografica ---");
                FichaBibliografica ficha = crearFicha();
                
                Libro libro = new Libro(0, titulo, autor, editorial, anioEdicion, null);
                
                //Metodo con transaccion : Si falla algo, se revierte TODO
                libroService.insertarLibroConFicha(libro, ficha);
                System.out.println("Libro y Ficha creados exitosamente");
                System.out.println("- Libro ID: "+libro.getId());
                System.out.println("- Ficha ID: "+ficha.getId());
            }else{
                //caso 2: libro sin ficha -> insercion simple(sin transaccion)
                Libro libro = new Libro(0, titulo, autor, editorial, anioEdicion, null);
                libroService.insertar(libro);
                System.out.println("Libro Creado exitosamente con ID: "+ libro.getId());
            }
            
          
        } catch (IllegalArgumentException e) {
            System.err.println("Datos invalidos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al crear libro: " + e.getMessage());
        }
    }
    
    /**
     * Captura los datos necesarios para crear una ficha bibliográfica.
     * 
     * @return instancia de FichaBibliografica con los datos capturados
     */
    private FichaBibliografica crearFicha() {
        String isbn = leerISBN();
        String clasificacionDewey = leerTexto("Clasificacion Dewey", false);
        String estanteria = leerTexto("Estanteria", false);
        String idioma = leerTexto("Idioma", false);
        
        return new FichaBibliografica(isbn, clasificacionDewey, estanteria, idioma, 0, false);
    }
    
    /**
     * Flujo interactivo para actualizar un libro existente.
     * MEJORADO: Usa transacciones cuando se actualiza libro + ficha.
     */
    public void actualizarLibro() {
        try {
            System.out.println("\n========= ACTUALIZAR LIBRO =========");
            int id = leerIdPositivo("ID del libro a actualizar");
            
            Libro libro = libroService.getById(id);
            if (libro == null) {
                System.out.println("Libro no encontrado.");
                return;
            }
            
            boolean fichaModificada = false;
            
            while (true) {
                mostrarDatosLibro(libro);
                
                System.out.println("\nSeleccione campo a actualizar:");
                System.out.println("1. Titulo");
                System.out.println("2. Autor");
                System.out.println("3. Editorial");
                System.out.println("4. Anio de edicion");
                System.out.println("5. Ficha bibliografica");
                System.out.println("6. Guardar cambios y salir");
                System.out.println("0. Cancelar sin guardar");
                System.out.print("Opcion: ");

                int opt = leerOpcionMenu(0, 6);
                
                switch (opt) {
                    case 0 -> {
                        System.out.println("Operacion cancelada.");
                        return;
                    }
                    case 1 -> {
                        String titulo = leerTexto("Nuevo titulo", true);
                        libro.setTitulo(titulo);
                    }
                    case 2 -> {
                        String autor = leerTexto("Nuevo autor", true);
                        libro.setAutor(autor);
                    }
                    case 3 -> {
                        String editorial = leerTexto("Nueva editorial", false);
                        libro.setEditorial(editorial);
                    }
                    case 4 -> {
                        Integer anio = leerAnioEdicion();
                        libro.setAnioEdicion(anio);
                    }
                    case 5 -> {
                        FichaBibliografica ficha = libro.getFichaBibliografica();
                        if (ficha == null) {
                            System.out.println("Este libro no tiene ficha bibliografica asociada.");
                            System.out.print("¿Desea crear una? (S/N): ");
                            String resp = scanner.nextLine().trim().toUpperCase();
                            if (resp.equals("S")) {
                                ficha = crearFicha();
                                // Insertar la ficha primero
                                fichaService.insertar(ficha);
                                libro.setFichaBibliografica(ficha);
                                fichaModificada = true;
                                System.out.println("Ficha creada con ID: " + ficha.getId());
                            }
                        } else {
                            updateFichaById(ficha);
                            fichaModificada = true;
                        }
                    }
                    case 6 -> {
                        // GUARDAR: Usar transacción si se modificó la ficha
                        if (fichaModificada) {
                            // Actualizar libro + ficha en TRANSACCIÓN
                            libroService.actualizarLibroConFicha(libro, true);
                            System.out.println("\n✓ Libro y ficha actualizados exitosamente (con transaccion)");
                        } else {
                            // Solo actualizar libro (sin transacción)
                            libroService.actualizar(libro);
                            System.out.println("\n✓ Libro actualizado exitosamente");
                        }
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("\n✗ Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Flujo interactivo para actualizar campos individuales de una ficha bibliográfica.
     */
    private void updateFichaById(FichaBibliografica f) {
        if (f == null) {
            System.out.println("La ficha bibliografica no existe.");
            return;
        }
        
        while (true) {
            System.out.println("\n--- Ficha Actual ---");
            System.out.println("ISBN: " + f.getIsbn());
            System.out.println("Clasificacion Dewey: " + f.getClasificacionDewey());
            System.out.println("Estanteria: " + f.getEstanteria());
            System.out.println("Idioma: " + f.getIdioma());
            
            System.out.println("\nSeleccione campo a actualizar:");
            System.out.println("1. ISBN");
            System.out.println("2. Clasificacion Dewey");
            System.out.println("3. Estanteria");
            System.out.println("4. Idioma");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            int opcion = leerOpcionMenu(0, 4);
            if (opcion == 0) break;
            
            switch (opcion) {
                case 1 -> {
                    System.out.print("Nuevo ISBN (Enter para mantener actual): ");
                    String isbn = scanner.nextLine().trim();
                    if (!isbn.isEmpty()) f.setIsbn(isbn);
                }
                case 2 -> {
                    System.out.print("Nueva clasificacion Dewey (Enter para mantener): ");
                    String dewey = scanner.nextLine().trim();
                    if (!dewey.isEmpty()) f.setClasificacionDewey(dewey);
                }
                case 3 -> {
                    System.out.print("Nueva estanteria (Enter para mantener): ");
                    String est = scanner.nextLine().trim();
                    if (!est.isEmpty()) f.setEstanteria(est);
                }
                case 4 -> {
                    System.out.print("Nuevo idioma (Enter para mantener): ");
                    String idioma = scanner.nextLine().trim();
                    if (!idioma.isEmpty()) f.setIdioma(idioma);
                }
            }
        }
    }
    
    /**
     * Lista libros según diferentes criterios de búsqueda.
     */
    public void listarLibros() {
        try {
            System.out.println("\n========= LISTAR/BUSCAR LIBROS =========");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por autor");
            System.out.println("3. Buscar por titulo");
            System.out.println("4. Buscar por anio de publicacion");
            System.out.println("5. Buscar por idioma");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");
            
            int opcion = leerOpcionMenu(0, 5);
            if (opcion == 0) return;

            List<Libro> libros;
            
            switch (opcion) {
                case 1 -> libros = libroService.getAll();
                case 2 -> {
                    String autor = leerTexto("Autor a buscar", true);
                    libros = libroService.buscarPorAutor(autor);
                }
                case 3 -> {
                    String titulo = leerTexto("Titulo a buscar", true);
                    libros = libroService.buscarPorTitulo(titulo);
                }
                case 4 -> {
                    int anio = leerAnioObligatorio("Anio de publicacion a buscar");
                    libros = libroService.buscarPorAnioPublicacion(anio);
                }
                case 5 -> {
                    String idioma = leerTexto("Idioma a buscar", true);
                    libros = libroService.buscarPorIdioma(idioma);
                }
                default -> {
                    System.out.println("Opcion invalida.");
                    return;
                }
            }
            
            mostrarResultadosBusqueda(libros);
            
        } catch (Exception e) {
            System.err.println("\n✗ Error al listar libros: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra los resultados de una búsqueda de libros.
     */
    private void mostrarResultadosBusqueda(List<Libro> libros) {
        if (libros.isEmpty()) {
            System.out.println("\nNo se encontraron libros.");
            return;
        }
        
        System.out.println("\n========= RESULTADOS (" + libros.size() + ") =========");
        for (Libro l : libros) {
            System.out.println("\nID: " + l.getId());
            System.out.println("  Titulo: " + l.getTitulo());
            System.out.println("  Autor: " + l.getAutor());
            System.out.println("  Editorial: " + (l.getEditorial() != null ? l.getEditorial() : "N/A"));
            System.out.println("  Anio: " + (l.getAnioEdicion() != null ? l.getAnioEdicion() : "N/A"));
            
            FichaBibliografica f = l.getFichaBibliografica();
            if (f != null) {
                System.out.println("  FICHA:");
                System.out.println("    - ISBN: " + (f.getIsbn() != null ? f.getIsbn() : "N/A"));
                System.out.println("    - Dewey: " + (f.getClasificacionDewey() != null ? f.getClasificacionDewey() : "N/A"));
                System.out.println("    - Estanteria: " + (f.getEstanteria() != null ? f.getEstanteria() : "N/A"));
                System.out.println("    - Idioma: " + (f.getIdioma() != null ? f.getIdioma() : "N/A"));
            }
        }
    }
    
    /**
     * Muestra los datos actuales de un libro.
     */
    private void mostrarDatosLibro(Libro libro) {
        System.out.println("\n--- Libro Actual ---");
        System.out.println("ID: " + libro.getId());
        System.out.println("Titulo: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor());
        System.out.println("Editorial: " + libro.getEditorial());
        System.out.println("Anio: " + libro.getAnioEdicion());
        
        if (libro.getFichaBibliografica() != null) {
            FichaBibliografica f = libro.getFichaBibliografica();
            System.out.println("Ficha: ID=" + f.getId() + ", ISBN=" + f.getIsbn());
        } else {
            System.out.println("Ficha: Sin ficha bibliografica");
        }
    }
    
    /**
     * Flujo interactivo para eliminar un libro.
     */
    public void eliminarLibro() {
        try {
            System.out.println("\n========= ELIMINAR LIBRO =========");
            int id = leerIdPositivo("ID del libro a eliminar");
            
            Libro libro = libroService.getById(id);
            if (libro == null) {
                System.out.println("Libro no encontrado.");
                return;
            }
            
            System.out.println("\nLibro encontrado:");
            System.out.println("  " + libro.getTitulo() + " - " + libro.getAutor());
            System.out.print("\n¿Esta seguro que desea eliminarlo? (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacion.equals("S")) {
                libroService.eliminar(id);
                System.out.println("\n✓ Libro eliminado exitosamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (Exception e) {
            System.err.println("\n✗ Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =================== Métodos Auxiliares de Validación ===================
    
    /**
     * Lee una opción de menú válida dentro de un rango específico.
     * Repite hasta obtener un número válido o que el usuario ingrese 0 para salir.
     */
    private int leerOpcionMenu(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.print("Opcion no puede estar vacia. Ingrese un numero (0 para salir): ");
                    continue;
                }
                
                int opcion = Integer.parseInt(input);
                if (opcion >= min && opcion <= max) {
                    return opcion;
                }
                System.out.print("Opcion invalida. Ingrese un numero entre " + min + " y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Ingrese un numero (0 para salir): ");
            }
        }
    }
    
    /** Lee un ID positivo mayor a cero, repitiendo hasta obtener un valor válido. */
    private int leerIdPositivo(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " (0 para cancelar): ");
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("El ID no puede estar vacio.");
                    continue;
                }
                
                int id = Integer.parseInt(input);
                if (id == 0) {
                    throw new RuntimeException("Operacion cancelada por el usuario");
                }
                if (id > 0) {
                    return id;
                }
                System.out.println("El ID debe ser un numero positivo mayor a cero.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero entero.");
            }
        }
    }
    
    /** Lee un texto obligatorio u opcional según el parámetro. */
    private String leerTexto(String campo, boolean obligatorio) {
        while (true) {
            System.out.print(campo + (obligatorio ? ": " : " (Enter para omitir): "));
            String texto = scanner.nextLine().trim();
            
            if (!texto.isEmpty()) {
                return texto;
            }
            
            if (obligatorio) {
                System.out.println("El campo " + campo + " es obligatorio.");
            } else {
                return null;
            }
        }
    }
    
    /** Lee un año de edición válido (opcional). */
    private Integer leerAnioEdicion() {
        while (true) {
            System.out.print("Anio de edicion (Enter para omitir, 0 para cancelar): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                return null;
            }
            
            try {
                int anio = Integer.parseInt(input);
                if (anio == 0) {
                    return null;
                }
                
                int anioActual = java.time.Year.now().getValue();
                if (anio >= 1000 && anio <= anioActual) {
                    return anio;
                }
                System.out.println("El anio debe estar entre 1000 y " + anioActual + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero entero.");
            }
        }
    }
    
    /** Lee un año obligatorio (para búsquedas). */
    private int leerAnioObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (0 para cancelar): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("El anio es obligatorio.");
                continue;
            }
            
            try {
                int anio = Integer.parseInt(input);
                if (anio == 0) {
                    throw new RuntimeException("Operacion cancelada por el usuario");
                }
                
                int anioActual = java.time.Year.now().getValue();
                if (anio >= 1000 && anio <= anioActual) {
                    return anio;
                }
                System.out.println("El anio debe estar entre 1000 y " + anioActual + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero entero.");
            }
        }
    }
    
    /** Lee un ISBN con validación de formato. */
    private String leerISBN() {
        while (true) {
            System.out.print("ISBN (Enter para omitir): ");
            String isbn = scanner.nextLine().trim();
            
            if (isbn.isEmpty()) {
                return null;
            }
            
            // Validación con regex
            String isbnSinGuiones = isbn.replace("-", "");
            if (isbnSinGuiones.matches("\\d{10}|\\d{13}")) {
                return isbn;
            }
            System.out.println("ISBN invalido. Debe tener 10 o 13 digitos (ej: 978-3-16-148410-0).");
        }
    }
    
}
