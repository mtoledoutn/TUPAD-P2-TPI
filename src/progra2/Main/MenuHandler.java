/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progra2.Main;

import java.util.List;
import java.util.Scanner;
import progra2.Models.FichaBibliografica;
import progra2.Models.Libro;
import progra2.Service.FichaBibliograficaService;
import progra2.Service.LibroService;

/**
 *
 * @author met
 */
public class MenuHandler {
    private final Scanner scanner;


    private final LibroService libroService;
    
    private final FichaBibliograficaService fichaService;

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


    public void crearLibro() {
        try {
            System.out.print("Titulo: ");
            String titulo = scanner.nextLine().trim();
            System.out.print("Autor: ");
            String autor = scanner.nextLine().trim();
            System.out.print("Editorial: ");
            String editorial = scanner.nextLine().trim();
            System.out.print("Año de edicion: ");
            int anio_edicion = Integer.parseInt(scanner.nextLine().trim());
            
            //opcion: crear libro con o sin ficha
            
            System.out.println("Desea crear una ficha bibliografica para el libro? (S/N): ");
            String respuesta = scanner.nextLine().trim().toUpperCase();

            FichaBibliografica fichaBibliografica = null;
            
            if (respuesta.equals("S")) {
                System.out.println("Ahora crearemos la ficha bibliografica del libro....");
                fichaBibliografica = crearFicha();
                
                //insertamos ficha en la bd
                fichaService.insertar(fichaBibliografica);
                System.out.println("Ficha creada con ID: " + fichaBibliografica.getId());
            }
           
            
            Libro libro = new Libro(0, titulo, autor, editorial, anio_edicion, fichaBibliografica);
            libroService.insertar(libro);
            System.out.println("Libro creado exitosamente con ID: " + libro.getId());
        } catch (Exception e) {
            System.err.println("Error al crear libro: " + e.getMessage());
        }
    }
    
    private FichaBibliografica crearFicha() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Clasificacion Dewey: ");
        String clasificacionDewey = scanner.nextLine().trim();
        System.out.print("Estanteria: ");
        String estanteria = scanner.nextLine().trim();
        System.out.print("idioma: ");
        String idioma = scanner.nextLine().trim();
        return new FichaBibliografica(isbn, clasificacionDewey, estanteria, idioma, 0, false);
    }

    private void updateFichaById(FichaBibliografica f) {
        if (f == null) {
            System.out.println("La ficha bibliográfica no existe.");
            return;
        }

        while (true) {
            System.out.println("\nFicha actual: ISBN='" + f.getIsbn() + "', Dewey='" + f.getClasificacionDewey() + "', Estanteria='" + f.getEstanteria() + "', Idioma='" + f.getIdioma() + "'");
            System.out.println("Seleccione atributo a actualizar:");
            System.out.println(" 1) ISBN");
            System.out.println(" 2) Clasificacion Dewey");
            System.out.println(" 3) Estanteria");
            System.out.println(" 4) Idioma");
            System.out.println(" 5) Volver");
            System.out.print("Opcion ficha: ");

            String fo = scanner.nextLine().trim();
            int foInt;
            try {
                foInt = Integer.parseInt(fo);
            } catch (NumberFormatException ex) {
                System.out.println("Opcion invalida.");
                continue;
            }

            if (foInt == 5) break;

            switch (foInt) {
                case 1 -> {
                    System.out.print("Nuevo ISBN (actual: " + f.getIsbn() + ", Enter para mantener): ");
                    String isbn = scanner.nextLine().trim();
                    if (!isbn.isEmpty()) f.setIsbn(isbn);
                }
                case 2 -> {
                    System.out.print("Nueva clasificacion Dewey (actual: " + f.getClasificacionDewey() + ", Enter para mantener): ");
                    String dewey = scanner.nextLine().trim();
                    if (!dewey.isEmpty()) f.setClasificacionDewey(dewey);
                }
                case 3 -> {
                    System.out.print("Nueva estanteria (actual: " + f.getEstanteria() + ", Enter para mantener): ");
                    String est = scanner.nextLine().trim();
                    if (!est.isEmpty()) f.setEstanteria(est);
                }
                case 4 -> {
                    System.out.print("Nuevo idioma (actual: " + f.getIdioma() + ", Enter para mantener): ");
                    String idioma = scanner.nextLine().trim();
                    if (!idioma.isEmpty()) f.setIdioma(idioma);
                }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    public void listarLibros() {
        try {
            System.out.print("¿Desea (1) listar todos o (2) buscar por autor (3) titulo (4) año de publicacion (5) idioma? Ingrese opcion: ");
            int subopcion = Integer.parseInt(scanner.nextLine());

            List<Libro> libros;
            String filtro;
            switch (subopcion) {
                case 1 -> libros = libroService.getAll();
                case 2 -> {
                    System.out.print("Ingrese autor a buscar: ");
                    filtro = scanner.nextLine().trim();
                    libros = libroService.buscarPorAutor(filtro);
                }
                case 3 -> {
                    System.out.print("Ingrese titulo del libro a buscar: ");
                    filtro = scanner.nextLine().trim();
                    libros = libroService.buscarPorTitulo(filtro);
                }
                case 4 -> {
                    System.out.print("Ingrese año de publicacion a buscar: ");
                    filtro = scanner.nextLine().trim();
                    libros = libroService.buscarPorAnioPublicacion(filtro);
                }
                case 5 -> {
                    System.out.print("Ingrese idioma a buscar: ");
                    filtro = scanner.nextLine().trim();
                    libros = libroService.buscarPorIdioma(filtro);
                }
                default -> {
                    System.out.println("Opcion invalida.");
                    return;
                }
            }

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros.");
                return;
            }

            for (Libro l : libros) {
                System.out.println("ID: " + l.getId()
                        + ", Titulo: " + l.getTitulo()
                        + ", Autor: " + l.getAutor()
                        + ", Editorial: " + l.getEditorial()
                        + ", Año: " + l.getAnioEdicion());

                FichaBibliografica f = l.getFichaBibliografica();
                if (f != null) {
                    System.out.println("   ISBN: " + f.getIsbn()
                            + ", Dewey: " + f.getClasificacionDewey()
                            + ", Estanteria: " + f.getEstanteria()
                            + ", Idioma: " + f.getIdioma());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar personas: " + e.getMessage());
        }
    }

    public void actualizarLibro() {
        try {
            System.out.print("ID del libro a a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Libro l = libroService.getById(id);

            if (l == null) {
                System.out.println("Libro no encontraado.");
                return;
            }
            
            while (true) {
                System.out.println("\nLibro encontrado: ID=" + l.getId() + ", Titulo='" + l.getTitulo() + "', Autor='" + l.getAutor() + "', Editorial='" + l.getEditorial() + "', Año='" + l.getAnioEdicion() + "'");
                System.out.println("Seleccione campo a actualizar:");
                System.out.println(" 1) Titulo");
                System.out.println(" 2) Autor");
                System.out.println(" 3) Editorial");
                System.out.println(" 4) Año de edicion");
                System.out.println(" 5) Ficha bibliográfica (ISBN/Dewey/Estanteria/Idioma)");
                System.out.println(" 6) Guardar cambios y salir");
                System.out.println(" 7) Cancelar sin guardar");
                System.out.print("Opcion: ");

                String optStr = scanner.nextLine().trim();
                int opt;
                try {
                    opt = Integer.parseInt(optStr);
                } catch (NumberFormatException nfe) {
                    System.out.println("Opcion invalida.");
                    continue;
                }

                switch (opt) {
                    case 1 -> {
                        System.out.print("Nuevo titulo (actual: " + l.getTitulo() + ", Enter para mantener): ");
                        String nuevoTitulo = scanner.nextLine().trim();
                        if (!nuevoTitulo.isEmpty()) {
                            l.setTitulo(nuevoTitulo);
                        }
                    }
                    case 2 -> {
                        System.out.print("Nuevo autor (actual: " + l.getAutor() + ", Enter para mantener): ");
                        String nuevoAutor = scanner.nextLine().trim();
                        if (!nuevoAutor.isEmpty()) {
                            l.setAutor(nuevoAutor);
                        }
                    }
                    case 3 -> {
                        System.out.print("Nueva editorial (actual: " + l.getEditorial() + ", Enter para mantener): ");
                        String nuevaEditorial = scanner.nextLine().trim();
                        if (!nuevaEditorial.isEmpty()) {
                            l.setEditorial(nuevaEditorial);
                        }
                    }
                    case 4 -> {
                        System.out.print("Nuevo año de edicion (actual: " + l.getAnioEdicion() + ", Enter para mantener): ");
                        String anioStr = scanner.nextLine().trim();
                        if (!anioStr.isEmpty()) {
                            try {
                                int anio = Integer.parseInt(anioStr);
                                l.setAnioEdicion(anio);
                            } catch (NumberFormatException nfe) {
                                System.out.println("Año inválido, no se actualizó.");
                            }
                        }
                    }
                    case 5 -> {
                        FichaBibliografica f = l.getFichaBibliografica();
                        if (f == null) {
                            System.out.println("Ficha bibliográfica ausente para este libro.");
                        } else {
                            updateFichaById(f);
                        }
                    }
                    case 6 -> {
                        libroService.actualizar(l);
                        System.out.println("Libro actualizado exitosamente.");
                        return;
                    }
                    case 7 -> {
                        System.out.println("Operacion cancelada. No se guardaron cambios.");
                        return;
                    }
                    default -> System.out.println("Opcion invalida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar persona: " + e.getMessage());
        }
    }

}
