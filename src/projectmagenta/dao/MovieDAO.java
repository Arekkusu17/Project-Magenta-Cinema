package projectmagenta.dao;

import projectmagenta.model.Movie;
import java.sql.*;

/**
 * Data Access Object (DAO) para operaciones con películas en la base de datos.
 * <p>
 * Permite agregar nuevas películas y eliminar por título (usado en tests).
 * </p>
 * @author arekk
 */
public class MovieDAO {
    
    /**
     * Inserta una nueva película en la base de datos.
     * Valida los campos antes de intentar la inserción.
     * @param movie Objeto Movie a insertar
     * @return true si la inserción fue exitosa, false si hubo error o datos inválidos
     */
    public boolean addMovie(Movie movie) {
        System.out.println("[INFO] Intentando agregar una nueva película a la base de datos...");
        
        // Only check for null object - all field validation is in Movie setters
        if (movie == null) {
            System.out.println("[ERROR] No se recibió un objeto película válido para agregar.");
            return false;
        }

        String sql = "INSERT INTO Cartelera (title, director, year, duration, genre) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("[ERROR] No se pudo establecer conexión con la base de datos para agregar la película.");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getDirector());
            pstmt.setInt(3, movie.getYear());
            pstmt.setInt(4, movie.getDuration());
            pstmt.setString(5, movie.getGenre());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[SUCCESS] Película agregada correctamente: '" + movie.getTitle() + "'.");
                return true;
            } else {
                System.out.println("[ERROR] No se pudo agregar la película (sin filas afectadas).");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Error SQL al agregar película: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * Elimina todas las películas con el título especificado (usado para limpieza en tests).
     * @param title Título de la película a eliminar
     * @return número de filas eliminadas
     */
    public int deleteMoviesByTitle(String title) {
        String sql = "DELETE FROM Cartelera WHERE title = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return 0;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            int rows = pstmt.executeUpdate();
            return rows;
        } catch (SQLException e) {
            System.out.println("Error al eliminar películas de test: " + e.getMessage());
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    /**
     * Cierra recursos JDBC abiertos (conexión, statement, resultset).
     * @param conn conexión a cerrar
     * @param stmt statement a cerrar
     * @param rs resultset a cerrar
     */
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
