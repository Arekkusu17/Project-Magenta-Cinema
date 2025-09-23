# 🧪 Pruebas de Validación y Base de Datos

Este archivo documenta las pruebas automáticas incluidas en el proyecto para validar la conexión a la base de datos y la inserción de películas, con validaciones centralizadas en el modelo `Movie`.

## Casos de Prueba Automatizados

- **Conexión a la base de datos:**
   - Verifica que la conexión se realice correctamente con las credenciales configuradas.

- **Inserción y validación de películas:**
   1. **Película válida:** Todos los campos correctos, debe insertarse exitosamente
   2. **Título nulo:** Falla en `setTitle()` - título obligatorio
   3. **Título vacío:** Falla en `setTitle()` - título obligatorio
   4. **Año inválido (1800):** Falla en `setYear()` - año antes de 1888
   5. **Duración cero:** Falla en `setDuration()` - duración debe ser positiva
   6. **Director nulo:** Falla en `setDirector()` - director obligatorio
   7. **Género vacío:** Falla en `setGenre()` - género obligatorio
   8. **Género inválido:** Falla en `setGenre()` - debe estar en lista permitida
   9. **Director muy largo:** Falla en `setDirector()` - máximo 50 caracteres
   10. **Director con caracteres inválidos:** Falla en `setDirector()` - solo letras, espacios, puntos y guiones
   11. **Objeto película nulo:** Solo validado en DAO - objeto obligatorio

## Comportamiento de las Pruebas

- **Para casos válidos:** El objeto `Movie` se crea exitosamente y se inserta en la base de datos
- **Para casos inválidos:** Los setters de `Movie` lanzan `IllegalArgumentException` inmediatamente, impidiendo la creación del objeto
- **Limpieza automática:** Al final se eliminan todos los registros de prueba de la base de datos

## Mensajes de Salida

Cada caso de prueba muestra:
```
[CASE] Nombre del caso de prueba
[INPUT] title='...', director='...', year=..., duration=..., genre='...'
[SUCCESS] Movie object created successfully
[SUCCESS] Movie inserted into database successfully
   O
[VALIDATION ERROR] Mensaje específico del error de validación
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

- **1 caso exitoso:** La película válida se inserta y se confirma en la base de datos
- **9 casos con errores de validación:** Cada uno muestra un mensaje específico del error
- **1 caso de objeto nulo:** Manejado por el DAO
- **Limpieza:** Se elimina el registro de prueba exitoso al final
