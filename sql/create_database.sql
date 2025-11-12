DROP DATABASE IF EXISTS dbtpi3;
CREATE DATABASE dbtpi3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dbtpi3;

CREATE TABLE ficha_bibliografica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    isbn VARCHAR(17) UNIQUE,
    clasificacion_dewey VARCHAR(20),
    estanteria VARCHAR(20),
    idioma VARCHAR(30)
);

CREATE TABLE libro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    editorial VARCHAR(100),
    anio_edicion INT,
    ficha_bibliografica_id BIGINT UNIQUE,
    CONSTRAINT fk_libro_ficha FOREIGN KEY (ficha_bibliografica_id) 
        REFERENCES ficha_bibliografica(id) 
        ON DELETE CASCADE
);
