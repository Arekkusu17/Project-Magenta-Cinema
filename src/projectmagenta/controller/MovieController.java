package projectmagenta.controller;

import projectmagenta.dao.MovieDAO;
import projectmagenta.model.Movie;
import java.util.List;

/**
 * Controlador para la gestión de películas.
 * Implementa la lógica de negocio para agregar películas, validando los datos
 * antes de delegar la inserción al DAO. Forma parte del patrón MVC.
 * @author Alex Fernandez
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
     * Elimina una película por su id.
     * @param id identificador único de la película
     * @return MovieResult con el resultado de la operación
     */
    public MovieResult deleteMovieById(int id) {
        if (id <= 0) {
            return new MovieResult(false, "ID de película inválido para eliminar.");
        }
        boolean success = movieDao.deleteMovieById(id);
        if (success) {
            return new MovieResult(true, "Película eliminada exitosamente.");
        } else {
            return new MovieResult(false, "No se pudo eliminar la película en la base de datos.");
        }
    }
    /**
     * Busca una película por su título exacto.
     * @param title Título de la película a buscar
     * @return Movie si se encuentra, null si no existe
     */
    public Movie findMovieByTitle(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        return movieDao.findMovieByTitle(title.trim());
    }

    /**
     * Busca una película por su ID.
     * @param id Identificador único de la película
     * @return Movie si se encuentra, null si no existe
     */
    public Movie findMovieById(int id) {
        if (id <= 0) return null;
        return movieDao.findMovieById(id);
    }

    /**
     * Busca películas cuyos títulos contengan una cadena específica.
     * @param partialTitle Cadena parcial del título a buscar
     * @return Lista de objetos Movie que coincidan con la búsqueda
     */
    public List<Movie> findMoviesByPartialTitle(String partialTitle) {
        if (partialTitle == null || partialTitle.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return movieDao.findMoviesByPartialTitle(partialTitle.trim());
    }

    /**
     * Actualiza los datos de una película existente.
     * @param movie Objeto Movie con los datos actualizados
     * @return MovieResult con el resultado de la operación
     */
    public MovieResult updateMovie(Movie movie) {
        try {
            if (movie == null || movie.getId() <= 0) {
                return new MovieResult(false, "Se requiere una película válida para actualizar.");
            }
            boolean success = movieDao.updateMovie(movie);
            if (success) {
                return new MovieResult(true, "Película actualizada exitosamente.");
            } else {
                return new MovieResult(false, "No se pudo actualizar la película en la base de datos.");
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