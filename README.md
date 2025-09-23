# 🎬 Sistema de Gestión de Películas - Cines Magenta

Sistema de gestión de cartelera para la cadena de cines **Magenta**, desarrollado en Java con interfaz Swing y base de datos MySQL.

## 📋 Descripción del Proyecto

Este sistema permite gestionar la información de películas en cartelera, enfocándose en tres áreas principales:
- **Diseño de Base de Datos**: Estructura optimizada para almacenar información de películas
- **Formulario Principal**: Interfaz de usuario intuitiva con patrón MDI
- **Funcionalidad Agregar Películas**: Sistema completo para registrar nuevas películas con validaciones

## 🚀 Características Principales

- ✅ **Patrón MVC**: Arquitectura Model-View-Controller implementada
- ✅ **Validaciones en Tiempo Real**: Feedback inmediato al usuario
- ✅ **Interfaz Intuitiva**: Diseño moderno con Java Swing
- ✅ **Base de Datos Robusta**: MySQL con charset UTF-8 completo
- ✅ **Manejo de Errores**: Sistema comprensivo de manejo de excepciones

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 18+
- **Framework GUI**: Java Swing
- **Base de Datos**: MySQL 8.0+
- **Patrón Arquitectónico**: MVC (Model-View-Controller)
- **IDE Recomendado**: NetBeans, IntelliJ IDEA, o VS Code

## 📁 Estructura del Proyecto

```
ProjectMagenta/
├── src/
│   └── projectmagenta/
│       ├── ProjectMagenta.java          # Clase principal
│       ├── controller/
│       │   └── MovieController.java     # Controlador MVC
│       ├── dao/
│       │   ├── DBConnection.java        # Conexión a BD
│       │   └── MovieDao.java           # Acceso a datos
│       ├── gui/
│       │   ├── MainFrame.java          # Ventana principal
│       │   └── AddMovieForm.java       # Formulario agregar
│       └── model/
│           └── Movie.java              # Modelo de datos
├── database/
│   ├── create_database.sql             # Script creación BD
│   └── README.md                       # Documentación BD
├── build/                              # Archivos compilados
├── nbproject/                          # Configuración NetBeans
└── README.md                           # Este archivo
```

## ⚙️ Configuración e Instalación

### 1. Prerrequisitos

- **Java JDK 18+** instalado
- **MySQL Server 8.0+** ejecutándose
- **MySQL Connector/J** (incluido en el proyecto)

### 2. Configuración de Base de Datos

1. Ejecutar el script SQL:
   ```bash
   mysql -u root -p < database/create_database.sql
   ```

2. Verificar configuración en `DBConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/Cine_DB";
   private static final String USER = "root";
   private static final String PASSWORD = "Admin";
   ```

### 3. Compilación y Ejecución

**Desde NetBeans:**
1. Abrir el proyecto en NetBeans
2. Presionar F6 o "Run Project"

**Desde línea de comandos:**
```bash
# Compilar
javac -d build/classes -cp "lib/*" src/projectmagenta/**/*.java

# Ejecutar
java -cp "build/classes:lib/*" projectmagenta.ProjectMagenta
```

## 🎯 Funcionalidades Implementadas

### Formulario Principal (MainFrame)
- 📋 Menú de navegación simplificado
- 🔄 Validación automática de conexión a BD

### Agregar Películas (AddMovieForm)
- ✔️ Restricciones de entrada personalizadas
- 💾 Integración completa con base de datos

### Validaciones Implementadas
- **Título**: Máximo 150 caracteres, caracteres especiales permitidos
- **Director**: Máximo 50 caracteres, solo letras y caracteres básicos
- **Año**: Rango válido desde 1888 hasta 5 años en el futuro
- **Duración**: Entre 1 y 999 minutos
- **Género**: Lista predefinida con 10 opciones

## 🗃️ Modelo de Datos

### Tabla: Cartelera
```sql
CREATE TABLE Cartelera (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    director VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    duration INT NOT NULL,
    genre ENUM('Acción', 'Drama', 'Comedia', ...) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Géneros Soportados
- Acción, Drama, Comedia, Terror, Romance, Ciencia Ficción, Thriller, Aventura, Animación, Documental

## 🔧 Arquitectura MVC

### Model (Modelo)
- `Movie.java`: Entidad que representa una película
- Validaciones de datos y lógica de negocio

### View (Vista)
- `MainFrame.java`: Ventana principal del sistema
- `AddMovieForm.java`: Formulario para agregar películas

### Controller (Controlador)
- `MovieController.java`: Lógica de control y validaciones
- `MovieDao.java`: Acceso a datos y operaciones BD

## 📊 Casos de Uso

1. **Inicio de Aplicación**
   - Sistema valida conexión a BD
   - Muestra ventana principal
   - Habilita funcionalidades disponibles

2. **Agregar Nueva Película**
   - Usuario abre formulario desde menú/toolbar
   - Completa campos con validación en tiempo real
   - Sistema valida datos y guarda en BD
   - Confirma operación exitosa



## 🧪 Pruebas Automáticas y Validación

La documentación completa de los casos de prueba automáticos y validación de base de datos se encuentra en [`test/README.md`](test/README.md).

## 🐛 Manejo de Errores

- **Conexión BD**: Mensaje claro si MySQL no está disponible
- **Validación Datos**: Feedback visual inmediato en formularios
- **Errores SQL**: Captura y manejo de excepciones
- **Logs**: Sistema de logging para debugging

## 🏗️ Próximos Desarrollos

- Incluir funcionalidades de búsqueda
- Permitir editar/eliminar películas existentes
- Implementar reportes o estadísticas

## 👤 Información del Desarrollador

- **Autor**: Alex Fernández Varas
- **Fecha**: Septiembre 2025
- **Propósito**: Sistema académico de gestión de cartelera
