package projectmagenta.dao;

import projectmagenta.model.Movie;

public class MovieDAOTest {
    public static void main(String[] args) throws java.io.UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        
        System.out.println("==============================");
        System.out.println("   MOVIE VALIDATION & DATABASE TEST   ");
        System.out.println("==============================");
        
        // Test database connection first
        System.out.println("Testing database connection...");
        boolean connected = DBConnection.testConnection();
        if (connected) {
            System.out.println("[SUCCESS] Database connection successful\n");
        } else {
            System.out.println("[ERROR] Database connection failed - aborting tests\n");
            System.exit(1);
        }
        
        MovieDAO dao = new MovieDAO();
        
        // Test 1: Valid movie (should insert to database)
        testCaseWithDB(dao, "Valid movie", "Test Movie", "Juan Pérez", 2025, 120, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Test Movie");
            m.setDirector("Juan Pérez");
            m.setYear(2025);
            m.setDuration(120);
            m.setGenre("Drama");
            System.out.println("[SUCCESS] Valid movie created");
            return m;
        });
        
        // Test 2: Null title (should fail validation)
        testCaseWithDB(dao, "Null title", null, "Juan Pérez", 2025, 100, "Acción", () -> {
            Movie m = new Movie();
            m.setTitle(null);
            return m;
        });
        
        // Test 3: Empty title (should fail validation)
        testCaseWithDB(dao, "Empty title", "", "Juan Pérez", 2025, 100, "Comedia", () -> {
            Movie m = new Movie();
            m.setTitle("");
            return m;
        });
        
        // Test 4: Invalid year (should fail validation)
        testCaseWithDB(dao, "Invalid year (1800)", "Old Movie", "Juan Pérez", 1800, 90, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Old Movie");
            m.setDirector("Juan Pérez");
            m.setYear(1800);
            m.setDuration(90);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 5: Zero duration (should fail validation)
        testCaseWithDB(dao, "Zero duration", "Zero Duration Movie", "Juan Pérez", 2025, 0, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Zero Duration Movie");
            m.setDirector("Juan Pérez");
            m.setYear(2025);
            m.setDuration(0);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 6: Null director (should fail validation)
        testCaseWithDB(dao, "Null director", "No Director Movie", null, 2025, 100, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("No Director Movie");
            m.setDirector(null);
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 7: Invalid genre (should fail validation)
        testCaseWithDB(dao, "Invalid genre", "Invalid Genre Movie", "Juan Pérez", 2025, 100, "Fantasia", () -> {
            Movie m = new Movie();
            m.setTitle("Invalid Genre Movie");
            m.setDirector("Juan Pérez");
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Fantasia");
            return m;
        });
        
        // Test 8: Long director name (should fail validation)
        testCaseWithDB(dao, "Director name too long", "Long Director Movie", "NombreDeDirectorExcesivamenteLargoQueSuperaElLimiteDeCincuentaCaracteresPermitidosEnElCampo", 2025, 100, "Drama", () -> {
            Movie m = new Movie();
            m.setTitle("Long Director Movie");
            m.setDirector("NombreDeDirectorExcesivamenteLargoQueSuperaElLimiteDeCincuentaCaracteresPermitidosEnElCampo");
            m.setYear(2025);
            m.setDuration(100);
            m.setGenre("Drama");
            return m;
        });
        
        // Test 9: Invalid director characters (should fail validation)
        testCaseWithDB(dao, "Director with invalid characters", "Invalid Director Movie", "Director123", 2025, 100, "Drama", () -> {
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
        System.out.println("[CASE] Null movie object");
        System.out.flush();
        
        try {
            boolean inserted = dao.addMovie(null);
            if (inserted) {
                System.out.println("[ERROR] Null movie was inserted (unexpected)");
            } else {
                System.out.println("[SUCCESS] Null movie was rejected as expected");
            }
        } catch (Exception e) {
            System.out.println("[SUCCESS] Exception thrown for null movie: " + e.getMessage());
        }
        
        // Cleanup: remove any test data that might have been inserted
        System.out.println("\n==============================");
        System.out.println("   CLEANUP: Removing test data   ");
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
            System.out.println("[CLEANUP] No test records found to delete (all validation worked correctly)");
        }
        
        System.out.println("\n==============================");
        System.out.println("   ALL TESTS COMPLETED   ");
        System.out.println("==============================");
    }
    
    private static void testCaseWithDB(MovieDAO dao, String caseName, String title, String director, int year, int duration, String genre, MovieCreator creator) {
        System.out.println("\n------------------------------");
        System.out.println("[CASE] " + caseName);
        System.out.println("[INPUT] title='" + title + "', director='" + director + "', year=" + year + ", duration=" + duration + ", genre='" + genre + "'");
        System.out.flush();
        
        Movie movie = null;
        boolean validationPassed = true;
        
        try {
            movie = creator.create();
            System.out.println("[SUCCESS] Movie object created successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("[VALIDATION ERROR] " + e.getMessage());
            validationPassed = false;
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected exception during movie creation: " + e.getMessage());
            validationPassed = false;
        }
        
        // Only attempt database insertion if validation passed
        if (validationPassed && movie != null) {
            try {
                boolean inserted = dao.addMovie(movie);
                if (inserted) {
                    System.out.println("[SUCCESS] Movie inserted into database successfully");
                } else {
                    System.out.println("[ERROR] Movie was not inserted into database");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Database insertion failed: " + e.getMessage());
            }
        }
        
        System.out.flush();
    }
    
    @FunctionalInterface
    private interface MovieCreator {
        Movie create() throws Exception;
    }
}