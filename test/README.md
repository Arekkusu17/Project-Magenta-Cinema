# 🧪 Pruebas de Validación, Base de Datos y Filtrado

Este archivo documenta las pruebas automáticas incluidas en el proyecto para validar la conexión a la base de datos, la inserción de películas con validaciones centralizadas en el modelo `Movie`, y las funcionalidades de filtrado de la aplicación.

## Estructura de Pruebas

Las pruebas están organizadas en **dos fases principales**:

### 📋 **FASE 1: PRUEBAS DE VALIDACIÓN Y CRUD**
Valida la lógica de negocio y operaciones básicas de base de datos.

### 🔍 **FASE 2: PRUEBAS DE FILTRADO**
Valida las funcionalidades de búsqueda y filtrado implementadas en la interfaz de usuario.

## Casos de Prueba Automatizados

### 🔌 **Conexión a la base de datos:**
- Verifica que la conexión se realice correctamente con las credenciales configuradas.

### 📝 **FASE 1: CRUD y validación de películas:**
1. **Película válida:** Todos los campos correctos, debe insertarse exitosamente
2. **Modificar película:** Modifica campos de una película existente y verifica actualización
3. **Eliminar película:** Elimina una película existente y verifica eliminación
4. **Título nulo:** Falla en `setTitle()` - título obligatorio
5. **Título vacío:** Falla en `setTitle()` - título obligatorio
6. **Año inválido (1800):** Falla en `setYear()` - año antes de 1888
7. **Duración cero:** Falla en `setDuration()` - duración debe ser positiva
8. **Director nulo:** Falla en `setDirector()` - director obligatorio
9. **Género vacío:** Falla en `setGenre()` - género obligatorio
10. **Género inválido:** Falla en `setGenre()` - debe estar en lista permitida
11. **Director muy largo:** Falla en `setDirector()` - máximo 50 caracteres
12. **Director con caracteres inválidos:** Falla en `setDirector()` - solo letras, espacios, puntos y guiones
13. **Objeto película nulo:** Solo validado en DAO - objeto obligatorio
14. **Modificar película con valores nulos:** Falla en validación o lanza excepción
15. **Eliminar película con ID inválido:** No elimina nada y no lanza excepción grave

### 🎬 **FASE 2: Pruebas de Filtrado:**

#### **Configuración de Datos de Prueba:**
Se insertan **10 películas diversas** que cubren:
- **Géneros:** Acción, Drama, Comedia, Terror, Thriller, Aventura, Animación, Documental
- **Períodos:** 1942-2019 (películas clásicas hasta modernas)
- **Variedad:** Diferentes duraciones y directores famosos

| Película | Género | Año | Director |
|----------|--------|-----|----------|
| Avengers: Endgame | Acción | 2019 | Anthony Russo |
| The Matrix | Acción | 1999 | Lana Wachowski |
| Titanic | Drama | 1997 | James Cameron |
| Casablanca | Drama | 1942 | Michael Curtiz |
| The Hangover | Comedia | 2009 | Todd Phillips |
| It Chapter Two | Terror | 2019 | Andy Muschietti |
| Se7en | Thriller | 1995 | David Fincher |
| Indiana Jones | Aventura | 1981 | Steven Spielberg |
| Toy Story | Animación | 1995 | John Lasseter |
| Free Solo | Documental | 2018 | Jimmy Chin |

#### **Pruebas de Filtrado por Género:**
- **Acción, Drama, Comedia, Terror, Animación**
- Muestra cuántas películas se encontraron para cada género
- Lista las películas específicas que coinciden

#### **Pruebas de Filtrado por Rango de Años:**
- **1990-2000:** Películas de los años 90
- **2000-2010:** Películas de los años 2000
- **2010-2020:** Películas de los años 2010
- **1940-1950:** Películas clásicas
- Muestra películas encontradas en cada período

#### **Pruebas de Filtrado Combinado (Género + Año):**
- **Acción (1995-2005):** Películas de acción del período especificado
- **Drama (1940-2000):** Películas dramáticas clásicas y modernas
- **Comedia (2000-2020):** Comedias contemporáneas
- Demuestra cómo funcionan los filtros en combinación

## Comportamiento de las Pruebas

### 📋 **Fase 1 - Validación y CRUD:**
- **Para casos válidos:** El objeto `Movie` se crea exitosamente y se inserta, modifica o elimina en la base de datos
- **Para casos inválidos:** Los setters de `Movie` lanzan `IllegalArgumentException` inmediatamente, impidiendo la creación o modificación del objeto
- **Para operaciones inválidas en DAO:** (como eliminar con ID inválido o modificar con valores nulos) el DAO rechaza la operación o lanza excepción controlada

### 🔍 **Fase 2 - Filtrado:**
- **Inserción de datos de prueba:** Se crean 10 películas diversas para testing
- **Filtrado por género:** Simula la funcionalidad de filtrado por género de la interfaz
- **Filtrado por año:** Simula la funcionalidad de filtrado por rango de años
- **Filtrado combinado:** Prueba la aplicación simultánea de ambos filtros
- **Validación de resultados:** Muestra conteos y listados de películas encontradas

### 🧹 **Limpieza automática:** 
Al final se eliminan **todos los registros de prueba** de la base de datos, incluyendo:
- Películas de validación de la Fase 1
- Películas de filtrado de la Fase 2

## Mensajes de Salida

### 📋 **Fase 1 - Cada caso de validación muestra:**
```
[CASO] Nombre del caso de prueba
[ENTRADA] título='...', director='...', año=..., duración=..., género='...'
[ÉXITO] Objeto película creado correctamente
[ÉXITO] Película insertada/modificada/eliminada en la base de datos correctamente
   O
[ERROR DE VALIDACIÓN] Mensaje específico del error de validación
   O
[ERROR] Mensaje de error o excepción controlada
```

### 🔍 **Fase 2 - Cada prueba de filtrado muestra:**
```
[SETUP] Total de películas insertadas para pruebas: X/10
[FILTRO GÉNERO] Acción: X película(s) encontrada(s)
  - Avengers: Endgame (2019)
  - The Matrix (1999)
[FILTRO AÑOS] 1990-2000: X película(s) encontrada(s)
  - Titanic (1997, Drama)
  - The Matrix (1999, Acción)
[FILTRO COMBINADO] Acción (1995-2005): X película(s) encontrada(s)
  - The Matrix (1999, Acción)
[CLEANUP] Deleted X record(s) with title: Película
```

## Ejecución de Pruebas

Para ejecutar las pruebas desde la línea de comandos:

```bash
# Compilar
javac -cp "build/classes:lib/*" test/projectmagenta/dao/MovieDaoTest.java

# Ejecutar
java -cp "build/classes;test;lib/*" test.projectmagenta.dao.MovieDaoTest
```

**Desde NetBeans/IDE:**
- Ejecutar directamente el archivo `MovieDaoTest.java` como aplicación Java

## Resultado Esperado

### 📊 **Resumen de Pruebas:**
- **3 casos exitosos de CRUD:** Inserción, modificación y eliminación de una película válida
- **12 casos con errores de validación:** Cada uno muestra un mensaje específico del error o rechazo
- **Pruebas de filtrado completas:** Validación de todas las funcionalidades de búsqueda y filtrado
- **Limpieza automática:** Se eliminan todos los registros de prueba (validación + filtrado) al final

### 🎯 **Cobertura de Funcionalidades:**
- ✅ **Validación de modelos** (Movie)
- ✅ **Operaciones CRUD** (MovieDAO)
- ✅ **Filtrado por género** (ViewAllMoviesForm)
- ✅ **Filtrado por rango de años** (ViewAllMoviesForm)
- ✅ **Filtrado combinado** (ViewAllMoviesForm)
- ✅ **Gestión de datos** (inserción, modificación, eliminación)
- ✅ **Limpieza automática** (sin residuos en BD)
