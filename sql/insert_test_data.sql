USE dbtpi3;

INSERT INTO ficha_bibliografica (eliminado, isbn, clasificacion_dewey, estanteria, idioma) 
VALUES 
(FALSE, '978-3-16-148410-0', '863.64', 'A-15', 'Español'),
(FALSE, '978-0-13-110362-7', '005.133', 'B-22', 'Inglés'),
(FALSE, '978-84-376-0494-7', '863.64', 'C-08', 'Español');

INSERT INTO libro (eliminado, titulo, autor, editorial, anio_edicion, ficha_bibliografica_id)
VALUES
(FALSE, 'Cien años de soledad', 'Gabriel García Márquez', 'Sudamericana', 1967, 1),
(FALSE, 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 2008, 2),
(FALSE, 'Rayuela', 'Julio Cortázar', 'Sudamericana', 1963, 3);