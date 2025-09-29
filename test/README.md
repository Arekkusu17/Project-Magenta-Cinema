# 🧪 Pruebas de Validación y Base de Datos

Este archivo documenta las pruebas automáticas incluidas en el proyecto para validar la conexión a la base de datos y la inserción de películas, con validaciones centralizadas en el modelo `Movie`.

## Casos de Prueba Automatizados

- **Conexión a la base de datos:**
   - Verifica que la conexión se realice correctamente con las credenciales configuradas.

- **CRUD y validación de películas:**
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

## Comportamiento de las Pruebas

- **Para casos válidos:** El objeto `Movie` se crea exitosamente y se inserta, modifica o elimina en la base de datos
- **Para casos inválidos:** Los setters de `Movie` lanzan `IllegalArgumentException` inmediatamente, impidiendo la creación o modificación del objeto
- **Para operaciones inválidas en DAO:** (como eliminar con ID inválido o modificar con valores nulos) el DAO rechaza la operación o lanza excepción controlada
- **Limpieza automática:** Al final se eliminan todos los registros de prueba de la base de datos

## Mensajes de Salida

Cada caso de prueba muestra:
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

- **3 casos exitosos:** Inserción, modificación y eliminación de una película válida
- **12 casos con errores de validación o rechazo:** Cada uno muestra un mensaje específico del error o rechazo
- **Limpieza:** Se eliminan los registros de prueba exitosos al final
