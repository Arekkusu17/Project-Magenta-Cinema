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
        
        // FASE 1: PRUEBAS DE VALIDACIÓN
        System.out.println("==============================");
        System.out.println("   FASE 1: PRUEBAS DE VALIDACIÓN   ");
        System.out.println("==============================");
        
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
        
        // FASE 2: PRUEBAS DE FILTRADO
        System.out.println("\n==============================");
        System.out.println("   FASE 2: PRUEBAS DE FILTRADO   ");
        System.out.println("==============================");
        
        // Insertar películas de prueba para filtrado
        insertTestMoviesForFiltering(dao);
        
        // Test de filtrado por género
        testGenreFiltering(dao);
        
        // Test de filtrado por año
        testYearFiltering(dao);
        
        // Test de filtrado combinado
        testCombinedFiltering(dao);
        
        // Cleanup: remove any test data that might have been inserted
    System.out.println("\n==============================");
    System.out.println("   LIMPIEZA: Eliminando datos de prueba   ");
    System.out.println("==============================");
        
        String[] testTitles = {
            "Test Movie", "Old Movie", "Zero Duration Movie", "No Director Movie",
            "Invalid Genre Movie", "Long Director Movie", "Invalid Director Movie",
            // Títulos de películas de prueba para filtrado
            "Avengers: Endgame", "Titanic", "The Hangover", "It Chapter Two", 
            "Casablanca", "The Matrix", "Se7en", "Indiana Jones", 
            "Toy Story", "Free Solo"
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
    
    /**
     * Inserta películas de prueba para testing de filtrado
     */
    private static void insertTestMoviesForFiltering(MovieDAO dao) {
        System.out.println("\n------------------------------");
        System.out.println("[SETUP] Insertando películas de prueba para filtrado");
        System.out.flush();
        
        Movie[] testMovies = {
            // Películas de Acción de diferentes años
            createMovie("Avengers: Endgame", "Anthony Russo", 2019, 181, "Acción"),
            createMovie("The Matrix", "Lana Wachowski", 1999, 136, "Acción"),
            
            // Películas de Drama de diferentes años
            createMovie("Titanic", "James Cameron", 1997, 194, "Drama"),
            createMovie("Casablanca", "Michael Curtiz", 1942, 102, "Drama"),
            
            // Películas de Comedia de diferentes años
            createMovie("The Hangover", "Todd Phillips", 2009, 100, "Comedia"),
            
            // Películas de Terror de diferentes años
            createMovie("It Chapter Two", "Andy Muschietti", 2019, 169, "Terror"),
            
            // Películas de Thriller de diferentes años
            createMovie("Se7en", "David Fincher", 1995, 127, "Thriller"),
            
            // Películas de Aventura de diferentes años
            createMovie("Indiana Jones", "Steven Spielberg", 1981, 115, "Aventura"),
            
            // Películas de Animación de diferentes años
            createMovie("Toy Story", "John Lasseter", 1995, 81, "Animación"),
            
            // Películas de Documental de diferentes años
            createMovie("Free Solo", "Jimmy Chin", 2018, 100, "Documental")
        };
        
        int insertedCount = 0;
        for (Movie movie : testMovies) {
            try {
                boolean inserted = dao.addMovie(movie);
                if (inserted) {
                    insertedCount++;
                    System.out.println("[ÉXITO] Insertada: " + movie.getTitle() + " (" + movie.getGenre() + ", " + movie.getYear() + ")");
                } else {
                    System.out.println("[ERROR] No se pudo insertar: " + movie.getTitle());
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Excepción al insertar " + movie.getTitle() + ": " + e.getMessage());
            }
        }
        
        System.out.println("[SETUP] Total de películas insertadas para pruebas: " + insertedCount + "/" + testMovies.length);
        System.out.flush();
    }
    
    /**
     * Crea una película de prueba con los parámetros dados
     */
    private static Movie createMovie(String title, String director, int year, int duration, String genre) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setYear(year);
        movie.setDuration(duration);
        movie.setGenre(genre);
        return movie;
    }
    
    /**
     * Prueba el filtrado por género
     */
    private static void testGenreFiltering(MovieDAO dao) {
        System.out.println("\n------------------------------");
        System.out.println("[FILTRADO] Probando filtrado por género");
        System.out.flush();
        
        String[] genresToTest = {"Acción", "Drama", "Comedia", "Terror", "Animación"};
        
        for (String genre : genresToTest) {
            try {
                java.util.List<Movie> allMovies = dao.getAllMovies();
                java.util.List<Movie> filteredMovies = new java.util.ArrayList<>();
                
                // Simular filtrado por género
                for (Movie movie : allMovies) {
                    if (movie.getGenre() != null && movie.getGenre().equals(genre)) {
                        filteredMovies.add(movie);
                    }
                }
                
                System.out.println("[FILTRO GÉNERO] " + genre + ": " + filteredMovies.size() + " película(s) encontrada(s)");
                for (Movie movie : filteredMovies) {
                    System.out.println("  - " + movie.getTitle() + " (" + movie.getYear() + ")");
                }
                
            } catch (Exception e) {
                System.out.println("[ERROR] Error al filtrar por género " + genre + ": " + e.getMessage());
            }
        }
        System.out.flush();
    }
    
    /**
     * Prueba el filtrado por rango de años
     */
    private static void testYearFiltering(MovieDAO dao) {
        System.out.println("\n------------------------------");
        System.out.println("[FILTRADO] Probando filtrado por rango de años");
        System.out.flush();
        
        int[][] yearRanges = {
            {1990, 2000},  // Años 90
            {2000, 2010},  // Años 2000
            {2010, 2020},  // Años 2010
            {1940, 1950}   // Años 40
        };
        
        for (int[] range : yearRanges) {
            int fromYear = range[0];
            int toYear = range[1];
            
            try {
                java.util.List<Movie> allMovies = dao.getAllMovies();
                java.util.List<Movie> filteredMovies = new java.util.ArrayList<>();
                
                // Simular filtrado por rango de años
                for (Movie movie : allMovies) {
                    if (movie.getYear() >= fromYear && movie.getYear() <= toYear) {
                        filteredMovies.add(movie);
                    }
                }
                
                System.out.println("[FILTRO AÑOS] " + fromYear + "-" + toYear + ": " + filteredMovies.size() + " película(s) encontrada(s)");
                for (Movie movie : filteredMovies) {
                    System.out.println("  - " + movie.getTitle() + " (" + movie.getYear() + ", " + movie.getGenre() + ")");
                }
                
            } catch (Exception e) {
                System.out.println("[ERROR] Error al filtrar por años " + fromYear + "-" + toYear + ": " + e.getMessage());
            }
        }
        System.out.flush();
    }
    
    /**
     * Prueba el filtrado combinado (género + año)
     */
    private static void testCombinedFiltering(MovieDAO dao) {
        System.out.println("\n------------------------------");
        System.out.println("[FILTRADO] Probando filtrado combinado (género + año)");
        System.out.flush();
        
        // Casos de filtrado combinado
        String[][] combinedTests = {
            {"Acción", "1995", "2005"},  // Películas de acción entre 1995-2005
            {"Drama", "1940", "2000"},   // Películas de drama entre 1940-2000
            {"Comedia", "2000", "2020"}  // Películas de comedia entre 2000-2020
        };
        
        for (String[] test : combinedTests) {
            String genre = test[0];
            int fromYear = Integer.parseInt(test[1]);
            int toYear = Integer.parseInt(test[2]);
            
            try {
                java.util.List<Movie> allMovies = dao.getAllMovies();
                java.util.List<Movie> filteredMovies = new java.util.ArrayList<>();
                
                // Simular filtrado combinado
                for (Movie movie : allMovies) {
                    boolean matchesGenre = movie.getGenre() != null && movie.getGenre().equals(genre);
                    boolean matchesYear = movie.getYear() >= fromYear && movie.getYear() <= toYear;
                    
                    if (matchesGenre && matchesYear) {
                        filteredMovies.add(movie);
                    }
                }
                
                System.out.println("[FILTRO COMBINADO] " + genre + " (" + fromYear + "-" + toYear + "): " + 
                                 filteredMovies.size() + " película(s) encontrada(s)");
                for (Movie movie : filteredMovies) {
                    System.out.println("  - " + movie.getTitle() + " (" + movie.getYear() + ", " + movie.getGenre() + ")");
                }
                
            } catch (Exception e) {
                System.out.println("[ERROR] Error en filtrado combinado " + genre + " (" + fromYear + "-" + toYear + "): " + e.getMessage());
            }
        }
        System.out.flush();
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