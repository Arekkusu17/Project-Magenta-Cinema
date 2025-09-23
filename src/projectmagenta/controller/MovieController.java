package projectmagenta.controller;

import projectmagenta.dao.MovieDAO;
import projectmagenta.model.Movie;

/**
 * Controlador para la gestión de películas.
 * Implementa la lógica de negocio para agregar películas, validando los datos
 * antes de delegar la inserción al DAO. Forma parte del patrón MVC.
 * @author arekk
 */
public class MovieController {
    
    private MovieDAO movieDao;
    
    /**
     * Constructor. Inicializa el DAO de películas.
     */
    public MovieController() {
        this.movieDao = new MovieDAO();
    }
    
    /**
     * Agrega una nueva película al sistema, validando los datos antes de guardar.
     * @param movie La película a agregar
     * @return MovieResult con el resultado de la operación (éxito o mensaje de error)
     */
    public MovieResult addMovie(Movie movie) {
        try {
            // All validation is now in Movie setters
            if (movie == null) {
                return new MovieResult(false, "Los datos de la película son requeridos");
            }
            
            // Intentar guardar en la base de datos
            boolean success = movieDao.addMovie(movie);
            
            if (success) {
                return new MovieResult(true, "Película agregada exitosamente");
            } else {
                return new MovieResult(false, "Error al guardar la película en la base de datos");
            }
            
        } catch (IllegalArgumentException ex) {
            return new MovieResult(false, "Error de validación: " + ex.getMessage());
        } catch (Exception e) {
            return new MovieResult(false, "Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Clase interna para representar el resultado de operaciones con películas.
     * Incluye si fue exitoso y un mensaje asociado.
     */
    public static class MovieResult {
        private boolean success;
        private String message;
        
        public MovieResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}