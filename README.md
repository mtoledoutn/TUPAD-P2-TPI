# Sistema de Gestión de Biblioteca

## Trabajo Final Integrador - Programación 2

### Descripción del Proyecto

Este Trabajo Final Integrador implementa un sistema completo de gestión de biblioteca que permite administrar libros y sus fichas bibliográficas mediante operaciones CRUD. El proyecto demuestra la aplicación práctica de los conceptos fundamentales de Programación Orientada a Objetos, JDBC y arquitectura en capas aprendidos durante el cursado de Programación 2.

El dominio elegido es **Libro → FichaBibliografica**, una relación unidireccional 1→1 donde cada libro puede tener asociada una ficha bibliográfica que contiene información complementaria de catalogación (ISBN, clasificación Dewey, ubicación física, idioma).

### Objetivos Académicos

El desarrollo de este sistema permite aplicar y consolidar los siguientes conceptos clave:

**1. Arquitectura en Capas**

- Separación de responsabilidades en 4 capas diferenciadas:
  - **Config**: Gestión de conexiones y transacciones
  - **Models**: Entidades de dominio (Libro, FichaBibliografica, BaseModel)
  - **DAO**: Acceso a datos con JDBC
  - **Service**: Lógica de negocio y validaciones
  - **Main**: Interfaz de usuario y menús

**2. Programación Orientada a Objetos**

- Herencia mediante clase abstracta `BaseModel`
- Interfaces genéricas (`GenericDAO<T>`, `GenericService<T>`)
- Encapsulamiento con atributos privados
- Polimorfismo en operaciones CRUD
- Sobrescritura de métodos (`toString()`)

**3. Persistencia de Datos con JDBC**

- Conexión a MySQL mediante `DatabaseConnection`
- PreparedStatements en todas las operaciones (prevención SQL Injection)
- Gestión de transacciones con commit/rollback
- Manejo de claves autogeneradas
- LEFT JOIN para recuperar relaciones 1→1

**4. Manejo de Recursos y Excepciones**

- Try-with-resources para gestión automática de recursos JDBC
- `TransactionManager` implementa `AutoCloseable`
- Propagación controlada de excepciones
- Validación multi-nivel (Service + BD)

**5. Patrones de Diseño**

- **Factory Pattern**: DatabaseConnection
- **Service Layer Pattern**: Lógica de negocio centralizada
- **DAO Pattern**: Abstracción del acceso a datos
- **Soft Delete Pattern**: Eliminación lógica
- **Dependency Injection**: Manual en constructores

**6. Validación de Integridad**

- Validación de campos obligatorios (título, autor)
- Validación de formato ISBN (10 o 13 dígitos)
- Validación de unicidad de ISBN
- Foreign Key única garantiza relación 1→1



### Funcionalidades Implementadas

**Gestión de Libros:**

- Crear libro con o sin ficha bibliográfica (transaccional)
- Listar todos los libros con sus fichas
- Buscar por: título, autor, editorial, año, idioma
- Actualizar libro y/o su ficha (transaccional cuando incluye ficha)
- Eliminar libro (soft delete)

**Gestión de Fichas Bibliográficas:**

- Creación automática al crear libro (opcional)
- Actualización dentro de transacciones
- Validación de ISBN único
- Formato: ISBN-10/13, clasificación Dewey, estantería, idioma

**Características Principales:**

- **Transacciones ACID**: Operaciones atómicas con rollback automático
- **Búsqueda flexible**: Coincidencias parciales case-insensitive
- **Soft Delete**: Preserva integridad referencial
- **Validaciones robustas**: Multi-capa (entrada, negocio, BD)
- **Normalización automática**: Trim + uppercase en todos los campos de texto



## Requisitos del Sistema

| Componente        | Versión Requerida       |
| ----------------- | ----------------------- |
| Java JDK          | 17 o superior           |
| MySQL             | 8.0 o superior          |
| Gradle            | 8.12 (incluido wrapper) |
| Sistema Operativo | Windows, Linux o macOS  |



## Instalación

##### 1. Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DEL_PROYECTO>
```

##### 2. Configurar Base de Datos

Ejecutar el script de creación de base de datos y tablas:

```bash
mysql -u root -p < sql/create_database.sql
```

O ejecutar manualmente en MySQL:

```sql
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
```

##### 3. Cargar Datos de Prueba (Opcional)

```bash
mysql -u root -p dbtpi3 < sql/insert_test_data.sql
```

Esto carga 3 libros de ejemplo con sus fichas:

- "Cien años de soledad" - Gabriel García Márquez
- "Clean Code" - Robert C. Martin
- "Rayuela" - Julio Cortázar

##### 4. Configurar Conexión (Opcional)

Por defecto conecta a:

- **Host**: localhost:3306
- **Base de datos**: dbtpi3
- **Usuario**: root
- **Contraseña**: (vacía)

Para cambiar, usar propiedades del sistema:

```bash
java -Ddb.url=jdbc:mysql://localhost:3306/dbtpi3 \
     -Ddb.user=tu_usuario \
     -Ddb.password=tu_contraseña \
     -cp ...
```



## Ejecución

##### Opción 1: Desde IDE

1. Abrir proyecto en Netbeans, IntelliJ IDEA o Eclipse
2. Ejecutar clase `progra2.Main.Main`

##### Opción 2: Línea de comandos

**Linux/macOS:**

```bash
# 1. Localizar JAR de MySQL
find ~/.gradle/caches -name "mysql-connector-j-*.jar"

# 2. Ejecutar (reemplazar <ruta-mysql-jar>)
java -cp "build/classes/java/main:<ruta-mysql-jar>" progra2.Main.Main
```

**Windows:**

```bash
# 1. Localizar JAR de MySQL
dir /s /b %USERPROFILE%\.gradle\caches\*mysql-connector-j-*.jar

# 2. Ejecutar (reemplazar <ruta-mysql-jar>)
java -cp "build\classes\java\main;<ruta-mysql-jar>" progra2.Main.Main
```

### Verificar Conexión

Para probar solo la conexión a la base de datos:

```bash
java -cp "build/classes/java/main:<ruta-mysql-jar>" progra2.Main.TestConexion
```

Salida esperada:

```
Probando conexion a la base de datos...
Conexion exitosa!

Informacion de la conexion:
- Usuario conectado: root@localhost
- Base de datos: dbtpi3
- URL: jdbc:mysql://localhost:3306/dbtpi3
- Driver: MySQL Connector/J v8.4.0
```



## Uso del Sistema

##### Menú Principal

```
========= SISTEMA DE GESTION DE BIBLIOTECA =========
1. Gestionar Libros
2. Verificar conexion a BD
0. Salir
```

##### Menú de Libros

```
========= MENU - Libros =========
1. Crear libro
2. Listar/Buscar libros
3. Actualizar libro
4. Eliminar libro
0. Volver al menu principal
```

### Ejemplos de Uso

##### 1. Crear Libro con Ficha (TRANSACCIONAL)

```
========= CREAR LIBRO =========
Titulo: El Principito
Autor: Antoine de Saint-Exupéry
Editorial (Enter para omitir): Salamandra
Anio de edicion (Enter para omitir): 1943
¿Desea crear una ficha bibliografica para el libro? (S/N): S

--- Creando ficha bibliografica ---
ISBN (ej: 978-3-16-148410-0 | Enter para omitir): 978-84-9838-776-2
Clasificacion Dewey (ej: 863.64 para literatura | Enter para omitir): 843
Estanteria (Enter para omitir): B-15
Idioma (Enter para omitir): Español

Ficha creada con ID: 4
Libro creado con ID: 4
Transaccion completada exitosamente
Libro y Ficha creados exitosamente
- Libro ID: 4
- Ficha ID: 4
```

**Nota**: Si algo falla durante la creación, se hace **rollback automático** de toda la operación.

##### 2. Buscar Libros por Autor

```
========= LISTAR/BUSCAR LIBROS =========
1. Listar todos
2. Buscar por autor
3. Buscar por titulo
4. Buscar por anio de publicacion
5. Buscar por idioma
0. Volver
Opcion: 2

Autor a buscar: García

========= RESULTADOS (1) =========

ID: 1
  Titulo: CIEN AÑOS DE SOLEDAD
  Autor: GABRIEL GARCÍA MÁRQUEZ
  Editorial: SUDAMERICANA
  Anio: 1967
  FICHA:
    - ISBN: 978-3-16-148410-0
    - Dewey: 863.64
    - Estanteria: A-15
    - Idioma: ESPAÑOL
```

##### 3. Actualizar Libro y Ficha (TRANSACCIONAL)

```
========= ACTUALIZAR LIBRO =========
ID del libro a actualizar (0 para cancelar): 1

--- Libro Actual ---
ID: 1
Titulo: CIEN AÑOS DE SOLEDAD
Autor: GABRIEL GARCÍA MÁRQUEZ
Editorial: SUDAMERICANA
Anio: 1967
Ficha: ID=1, ISBN=978-3-16-148410-0

Seleccione campo a actualizar:
1. Titulo
2. Autor
3. Editorial
4. Anio de edicion
5. Ficha bibliografica
6. Guardar cambios y salir
0. Cancelar sin guardar
Opcion: 5

--- Ficha Actual ---
ISBN: 978-3-16-148410-0
Clasificacion Dewey: 863.64
Estanteria: A-15
Idioma: ESPAÑOL

Seleccione campo a actualizar:
1. ISBN
2. Clasificacion Dewey
3. Estanteria
4. Idioma
0. Volver
Opcion: 3

Nueva estanteria (Enter para mantener): A-20
Ficha actualizada
Libro actualizado
Transaccion completada exitosamente

Libro y ficha actualizados exitosamente (con transaccion)
```

##### 4. Crear Libro sin Ficha

```
========= CREAR LIBRO =========
Titulo: El Quijote
Autor: Miguel de Cervantes
Editorial (Enter para omitir): 
Anio de edicion (Enter para omitir): 1605
¿Desea crear una ficha bibliografica para el libro? (S/N): N

Libro Creado exitosamente con ID: 5
```



## Arquitectura del Sistema

### Estructura en Capas

```
┌───────────────────────────────────────────┐
│              Main / UI Layer              │
│         (Interacción con usuario)         │
│         Main, AppMenu, MenuHandler,       │
│         MenuDisplay, TestConexion         │
└───────────────────┬───────────────────────┘
                    │
┌───────────────────▼───────────────────────┐
│              Service Layer                │
│      (Lógica de negocio y validación)     │
│  LibroService, FichaBibliograficaService  │
└───────────────────┬───────────────────────┘
                    │
┌───────────────────▼───────────────────────┐
│                DAO Layer                  │
│            (Acceso a datos)               │
│      LibroDAO, FichaBibliograficaDAO      │
└───────────────────┬───────────────────────┘
                    │
┌───────────────────▼───────────────────────┐
│              Models Layer                 │
│          (Entidades de dominio)           │
│    Libro, FichaBibliografica, BaseModel   │
└───────────────────┬───────────────────────┘
                    │
┌───────────────────▼───────────────────────┐
│               Config Layer                │
│         (Conexión y transacciones)        │
│   DatabaseConnection, TransactionManager  │
└───────────────────────────────────────────┘
```

### Componentes Principales

**Config/**

- `DatabaseConnection.java`: Factory para conexiones JDBC con validación estática
- `TransactionManager.java`: Gestión de transacciones con AutoCloseable

**Models/**

- `BaseModel.java`: Clase abstracta con id y eliminado
- `Libro.java`: Entidad Libro (título, autor, editorial, año, ficha)
- `FichaBibliografica.java`: Entidad Ficha (ISBN, Dewey, estantería, idioma)

**DAO/**

- `GenericDAO<T>`: Interface genérica CRUD
- `LibroDAO`: Operaciones CRUD + búsquedas (LEFT JOIN con ficha)
- `FichaBibliograficaDAO`: Operaciones CRUD + validación ISBN único

**Service/**

- `GenericService<T>`: Interface genérica de servicios
- `LibroService`: Validaciones de libro + operaciones transaccionales
- `FichaBibliograficaService`: Validaciones de ficha (ISBN, formato)

**Main/**

- `Main.java`: Punto de entrada
- `AppMenu.java`: Orquestador del ciclo de menú
- `MenuHandler.java`: Lógica de operaciones CRUD
- `MenuDisplay.java`: Renderizado de menús
- `TestConexion.java`: Utilidad de verificación de conexión
  
  

## Modelo de Datos

```
┌────────────────────────┐          ┌──────────────────────┐
│        libro           │          │  ficha_bibliografica │
├────────────────────────┤          ├──────────────────────┤
│ id (PK)                │          │ id (PK)              │
│ titulo                 │          │ isbn (UNIQUE)        │
│ autor                  │          │ clasificacion_dewey  │
│ editorial              │          │ estanteria           │
│ anio_edicion           │          │ idioma               │
│ ficha_bibliografica_id │──────┐   │ eliminado            │
│ eliminado              │      │   └──────────────────────┘
└────────────────────────┘      │
                                │
                                └──▶ Relación 1→1 Unidireccional
                                    (FK UNIQUE + ON DELETE CASCADE)
```

**Características del Modelo:**

- **Relación 1→1 Unidireccional**: Solo Libro referencia a FichaBibliografica
- **FK Única**: `ficha_bibliografica_id UNIQUE` garantiza máximo 1 libro por ficha
- **ON DELETE CASCADE**: Al eliminar ficha, se actualiza automáticamente el libro
- **Soft Delete**: Campo `eliminado` para eliminación lógica
- **ISBN Único**: Constraint en BD + validación en Service
  
  

## Validaciones Implementadas

### Nivel Service (Antes de BD)

**LibroService:**

- Título obligatorio, máximo 150 caracteres
- Autor obligatorio, máximo 120 caracteres
- Editorial máximo 100 caracteres (opcional)
- Año entre 1000 y año actual (opcional)

**FichaBibliograficaService:**

- ISBN formato válido: `\d{10}|\d{13}` (con o sin guiones)
- ISBN único (consulta a BD antes de insertar/actualizar)
- Clasificación Dewey máximo 20 caracteres
- Estantería máximo 20 caracteres
- Idioma máximo 30 caracteres

### Nivel Base de Datos

- `NOT NULL` en campos obligatorios
- `UNIQUE` en ISBN y ficha_bibliografica_id
- `FOREIGN KEY` con ON DELETE CASCADE
- `CHECK` implícito en tipos de datos
  
  

## Patrones y Buenas Prácticas

##### Seguridad

- **100% PreparedStatements**: Prevención de SQL Injection
- **Validación multi-capa**: Service valida antes de DAO
- **Conexiones cerradas**: Try-with-resources en todas las operaciones

##### Gestión de Recursos

- **Try-with-resources**: Connection, Statement, ResultSet
- **AutoCloseable**: TransactionManager cierra y hace rollback automático
- **Scanner cerrado**: En AppMenu.run() al finalizar

##### Transacciones

- **Atomicidad**: Operaciones compuestas son todo o nada
- **Rollback automático**: TransactionManager en bloque try-with-resources
- **Conexión compartida**: Misma Connection para múltiples DAOs

##### Validaciones

- **Input trimming**: `.trim()` inmediato en todas las entradas
- **Normalización**: `.toUpperCase()` en campos de texto
- **IDs positivos**: Validación `id > 0` en todas las operaciones
- **Verificación rowsAffected**: En UPDATE y DELETE

##### Soft Delete

- DELETE ejecuta: `UPDATE tabla SET eliminado = TRUE WHERE id = ?`
- SELECT filtra: `WHERE eliminado = FALSE`
- No hay eliminación física de datos
  
  

## Solución de Problemas

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Causa**: JAR de MySQL no está en classpath

**Solución**: Incluir mysql-connector-j en el comando java -cp

### Error: "Communications link failure"

**Causa**: MySQL no está ejecutándose

**Solución**:

```bash
# Linux/macOS
sudo systemctl start mysql
# O
brew services start mysql

# Windows
net start MySQL80
```

### Error: "Access denied for user 'root'@'localhost'"

**Causa**: Credenciales incorrectas

**Solución**: Verificar usuario/contraseña en DatabaseConnection.java o usar -Ddb.user y -Ddb.password

### Error: "Unknown database 'dbtpi3'"

**Causa**: Base de datos no creada

**Solución**: Ejecutar `sql/create_database.sql`

### Error: "Table 'libro' doesn't exist"

**Causa**: Tablas no creadas

**Solución**: Ejecutar el CREATE TABLE en `sql/create_database.sql`

### Error: "Duplicate entry '...' for key 'isbn'"

**Causa**: ISBN ya existe en la BD

**Solución**: Usar un ISBN diferente o actualizar la ficha existente



## Tecnologías Utilizadas

- **Lenguaje**: Java 17
- **Build Tool**: Gradle 8.12
- **Base de Datos**: MySQL 8.x
- **JDBC Driver**: mysql-connector-j 8.4.0
- **Character Set**: UTF-8 (utf8mb4_unicode_ci)
  
  

## Estructura de Directorios

```
proyecto-biblioteca/
├── lib/                       # 
├── nbproject/                 # 
├── sql/
│   ├── create_database.sql    # Script de creación de BD y tablas
│   └── insert_test_data.sql   # Datos de prueba
├── src/progra2/
│   ├── Config/                # Conexión y transacciones
│   │   ├── DatabaseConnection.java
│   │   └── TransactionManager.java
│   ├── DAO/                   # Acceso a datos
│   │   ├── FichaBibliograficaDAO.java
│   │   ├── GenericDAO.java
│   │   └── LibroDAO.java
│   ├── Main/                  # UI y punto de entrada
│   │   ├── AppMenu.java
│   │   ├── Main.java
│   │   ├── MenuDisplay.java
│   │   ├── MenuHandler.java
│   │   └── TestConexion.java
│   ├── Models/                # Entidades de dominio
│   │   ├── BaseModel.java
│   │   ├── FichaBibliografica.java
│   │   └── Libro.java
│   └── Service/               # Lógica de negocio
│       ├── FichaBibliograficaService.java
│       ├── GenericService.java
│       └── LibroService.java
├── .gitignore                 # Archivo gitignore
├── build.xml                  # 
├── manifest.mf                # 
├── HISTORIAS_DEL_USUARIO      # Especificaciones funcionales
└── README.md                  # Este archivo
```



## Convenciones de Código

- **Idioma**: Español (clases, métodos, variables)
- **Nomenclatura**:
  - Clases: PascalCase (`LibroService`)
  - Métodos: camelCase (`buscarPorAutor`)
  - Constantes SQL: UPPER_SNAKE_CASE (`INSERT_SQL`)
- **Indentación**: 4 espacios
- **Recursos**: Siempre try-with-resources
- **SQL**: Constantes private static final
- **Excepciones**: Capturar y manejar con mensajes claros
  
  

## Conceptos de Programación 2 Demostrados

| Concepto                 | Implementación                                          |
| ------------------------ | ------------------------------------------------------- |
| **Herencia**             | `BaseModel` heredada por `Libro` y `FichaBibliografica` |
| **Polimorfismo**         | Interfaces `GenericDAO<T>` y `GenericService<T>`        |
| **Encapsulamiento**      | Atributos privados con getters/setters                  |
| **Abstracción**          | Interfaces que definen contratos                        |
| **JDBC**                 | Connection, PreparedStatement, ResultSet, transacciones |
| **DAO Pattern**          | Separación de persistencia de lógica de negocio         |
| **Service Layer**        | Validaciones centralizadas                              |
| **Exception Handling**   | Try-catch en todas las capas                            |
| **Resource Management**  | Try-with-resources para AutoCloseable                   |
| **Dependency Injection** | Manual en constructores                                 |
| **Transacciones**        | setAutoCommit(false), commit(), rollback()              |



---



**Versión**: 1.0  
**Java**: 17+  
**MySQL**: 8.x  
**Gradle**: 8.12  
**Proyecto Educativo** - Trabajo Final Integrador de Programación 2
