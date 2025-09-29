package projectmagenta.dao;

import projectmagenta.model.Movie;

public class MovieDAOTest {
    public static void main(String[] args) throws java.io.UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        
        System.out.println("==============================");
        System.out.println("   PRUEBAS DE VALIDACIÓN Y BASE DE DATOS DE PELÍCULAS   ");
        System.out.println("==============================");

        // Probar conexión a la base de datos primero
        System.out.println("Probando conexión a la base de datos...");
        boolean connected = DBConnection.testConnection();
        if (connected) {
            System.out.println("[ÉXITO] Conexión a la base de datos exitosa\n");
        } else {
            System.out.println("[ERROR] Falló la conexión a la base de datos - abortando pruebas\n");
            System.exit(1);
        }
        
        MovieDAO dao = new MovieDAO();
        
        // Prueba 1: Película válida (debe insertarse en la base de datos)
        testCaseWithDB(dao, "Película válida", "Test Movie", "Juan Pérez", 2025, 120, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Test Movie");
            m.setDirector("Juan Pérez");
            m.setYear(2025);
            m.setDuration(120);
            m.setGenre("Drama");
            System.out.println("[ÉXITO] Película válida creada");
            return m;
        });

        // Test 1b: Modify movie (should update the movie in database)
        System.out.println("\n------------------------------");
        System.out.println("[CASO] Modificar película");
        System.out.flush();
        Movie movieToUpdate = dao.findMovieByTitle("Test Movie");
        if (movieToUpdate != null) {
            movieToUpdate.setDirector("Ana Gómez");
            movieToUpdate.setDuration(150);
            boolean updated = dao.updateMovie(movieToUpdate);
            if (updated) {
                System.out.println("[ÉXITO] Película modificada correctamente: director='" + movieToUpdate.getDirector() + "', duración=" + movieToUpdate.getDuration());
            } else {
                System.out.println("[ERROR] No se modificó la película");
            }
        } else {
            System.out.println("[ERROR] No se encontró la película a modificar");
        }

        // Test 1c: Delete movie (should delete the movie from database)
        System.out.println("\n------------------------------");
        System.out.println("[CASO] Eliminar película");
        System.out.flush();
        Movie movieToDelete = dao.findMovieByTitle("Test Movie");
        if (movieToDelete != null) {
            boolean deleted = dao.deleteMovieById(movieToDelete.getId());
            if (deleted) {
                System.out.println("[ÉXITO] Película eliminada correctamente");
            } else {
                System.out.println("[ERROR] No se eliminó la película");
            }
        } else {
            System.out.println("[ERROR] No se encontró la película a eliminar");
        }
        
        // Test 2: Null title (should fail validation)
        testCaseWithDB(dao, "Título nulo", null, "Juan Pérez", 2025, 100, "Acción", () -> {
            Movie m = new Movie();
            m.setTitle(null);
            return m;
        });
        
        // Test 3: Empty title (should fail validation)
        testCaseWithDB(dao, "Título vacío", "", "Juan Pérez", 2025, 100, "Comedia", () -> {
            Movie m = new Movie();
            m.setTitle("");
            return m;
        });
        
        // Test 4: Invalid year (should fail validation)
        testCaseWithDB(dao, "Año inválido (1800)", "Old Movie", "Juan Pérez", 1800, 90, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Old Movie");
            m.setDirector("Juan Pérez");
            m.setYear(1800);
            m.setDuration(90);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 5: Zero duration (should fail validation)
        testCaseWithDB(dao, "Duración cero", "Zero Duration Movie", "Juan Pérez", 2025, 0, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Zero Duration Movie");
            m.setDirector("Juan Pérez");
            m.setYear(2025);
            m.setDuration(0);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 6: Null director (should fail validation)
        testCaseWithDB(dao, "Director nulo", "No Director Movie", null, 2025, 100, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("No Director Movie");
            m.setDirector(null);
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 7: Invalid genre (should fail validation)
        testCaseWithDB(dao, "Género inválido", "Invalid Genre Movie", "Juan Pérez", 2025, 100, "Fantasia", () -> {
            Movie m = new Movie();
            m.setTitle("Invalid Genre Movie");
            m.setDirector("Juan Pérez");
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Fantasia");
            return m;
        });
        
        // Test 8: Long director name (should fail validation)
        testCaseWithDB(dao, "Nombre de director demasiado largo", "Long Director Movie", "NombreDeDirectorExcesivamenteLargoQueSuperaElLimiteDeCincuentaCaracteresPermitidosEnElCampo", 2025, 100, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Long Director Movie");
            m.setDirector("NombreDeDirectorExcesivamenteLargoQueSuperaElLimiteDeCincuentaCaracteresPermitidosEnElCampo");
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 9: Invalid director characters (should fail validation)
        testCaseWithDB(dao, "Director con caracteres inválidos", "Invalid Director Movie", "Director123", 2025, 100, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Invalid Director Movie");
            m.setDirector("Director123");
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 10: Null movie object
        System.out.println("\n------------------------------");
        System.out.println("[CASO] Objeto película nulo");
        System.out.flush();
        
        try {
            boolean inserted = dao.addMovie(null);
            if (inserted) {
                System.out.println("[ERROR] Se insertó una película nula (no esperado)");
            } else {
                System.out.println("[ÉXITO] Se rechazó la película nula como se esperaba");
            }
        } catch (Exception e) {
            System.out.println("[ÉXITO] Se lanzó excepción para película nula: " + e.getMessage());
        }

        // Test 11: Modify movie with null values (should fail validation)
        System.out.println("\n------------------------------");
        System.out.println("[CASO] Modificar película con valores nulos");
        System.out.flush();
        Movie invalidUpdate = new Movie();
        try {
            invalidUpdate.setTitle(null);
            invalidUpdate.setDirector(null);
            invalidUpdate.setYear(2025);
            invalidUpdate.setDuration(100);
            invalidUpdate.setGenre(null);
            boolean updated = dao.updateMovie(invalidUpdate);
            if (updated) {
                System.out.println("[ERROR] Se modificó una película con valores nulos (no esperado)");
            } else {
                System.out.println("[ÉXITO] Se rechazó la modificación con valores nulos como se esperaba");
            }
        } catch (Exception e) {
            System.out.println("[ÉXITO] Se lanzó excepción para modificación inválida: " + e.getMessage());
        }

        // Prueba 12: Eliminar película con ID inválido (debe fallar correctamente)
        System.out.println("\n------------------------------");
        System.out.println("[CASO] Eliminar película con ID inválido");
        System.out.flush();
        try {
            boolean deleted = dao.deleteMovieById(-1); // ID inválido
            if (deleted) {
                System.out.println("[ERROR] Se eliminó una película con ID inválido (no esperado)");
            } else {
                System.out.println("[ÉXITO] No se eliminó ninguna película con ID inválido, como se esperaba");
            }
        } catch (Exception e) {
            System.out.println("[ÉXITO] Se lanzó excepción para eliminación inválida: " + e.getMessage());
        }
        
        // Cleanup: remove any test data that might have been inserted
    System.out.println("\n==============================");
    System.out.println("   LIMPIEZA: Eliminando datos de prueba   ");
    System.out.println("==============================");
        
        String[] testTitles = {
            "Test Movie", "Old Movie", "Zero Duration Movie", "No Director Movie",
            "Invalid Genre Movie", "Long Director Movie", "Invalid Director Movie"
        };
        
        int totalDeleted = 0;
        for (String title : testTitles) {
            try {
                int deleted = dao.deleteMoviesByTitle(title);
                if (deleted > 0) {
                    System.out.println("[CLEANUP] Deleted " + deleted + " record(s) with title: " + title);
                    totalDeleted += deleted;
                }
            } catch (Exception e) {
                System.out.println("[CLEANUP ERROR] Failed to delete movies with title '" + title + "': " + e.getMessage());
            }
        }
        
        if (totalDeleted == 0) {
            System.out.println("[LIMPIEZA] No se encontraron registros de prueba para eliminar (todas las validaciones funcionaron correctamente)");
        }

        System.out.println("\n==============================");
        System.out.println("   TODAS LAS PRUEBAS FINALIZADAS   ");
        System.out.println("==============================");
    }
    
    private static void testCaseWithDB(MovieDAO dao, String caseName, String title, String director, int year, int duration, String genre, MovieCreator creator) {
        System.out.println("\n------------------------------");
        System.out.println("[CASO] " + caseName);
        System.out.println("[ENTRADA] título='" + title + "', director='" + director + "', año=" + year + ", duración=" + duration + ", género='" + genre + "'");
        System.out.flush();

        Movie movie = null;
        boolean validationPassed = true;

        try {
            movie = creator.create();
            System.out.println("[ÉXITO] Objeto película creado correctamente");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR DE VALIDACIÓN] " + e.getMessage());
            validationPassed = false;
        } catch (Exception e) {
            System.out.println("[ERROR] Excepción inesperada al crear la película: " + e.getMessage());
            validationPassed = false;
        }

        // Solo intentar inserción si la validación pasó
        if (validationPassed && movie != null) {
            try {
                boolean inserted = dao.addMovie(movie);
                if (inserted) {
                    System.out.println("[ÉXITO] Película insertada en la base de datos correctamente");
                } else {
                    System.out.println("[ERROR] No se insertó la película en la base de datos");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Falló la inserción en la base de datos: " + e.getMessage());
            }
        }

        System.out.flush();
    }
    
    @FunctionalInterface
    private interface MovieCreator {
        Movie create() throws Exception;
    }
}