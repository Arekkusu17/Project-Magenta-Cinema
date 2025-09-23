# Sistema de Gestión de Películas - Cines Magenta

## Configuración de la Base de Datos

### Requisitos previos
- MySQL Server 8.0 o superior
- Usuario con permisos para crear bases de datos
- Driver MySQL Connector/J en el classpath del proyecto

### Instalación de la Base de Datos

1. **Ejecutar el script SQL:**
   ```bash
   mysql -u root -p < database/create_database.sql
   ```

2. **O ejecutar desde MySQL Workbench:**
   - Abrir MySQL Workbench
   - Conectar al servidor MySQL
   - Abrir el archivo `create_database.sql`
   - Ejecutar el script completo

### Configuración de Conexión

Por defecto, la aplicación está configurada para conectarse con:
- **Host:** localhost
- **Puerto:** 3306
- **Base de datos:** Cine_DB
- **Usuario:** root
- **Contraseña:** Admin

Para cambiar estos parámetros, modifique la clase `DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/Cine_DB";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

### Estructura de la Base de Datos

#### Tabla: Cartelera
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | INT AUTO_INCREMENT | Identificador único (PK) |
| title | VARCHAR(150) | Título de la película |
| director | VARCHAR(50) | Director de la película |
| year | INT | Año de lanzamiento |
| duration | INT | Duración en minutos |
| genre | ENUM | Género de la película |
| created_at | TIMESTAMP | Fecha de creación del registro |
| updated_at | TIMESTAMP | Fecha de última actualización |

#### Géneros Disponibles
- Acción
- Drama  
- Comedia
- Terror
- Romance
- Ciencia Ficcion
- Thriller
- Aventura
- Animacion
- Documental

### Características de la Base de Datos

- **Timestamps automáticos:** `created_at` y `updated_at` se gestionan automáticamente
- **Validación de géneros:** Campo ENUM asegura solo valores válidos

