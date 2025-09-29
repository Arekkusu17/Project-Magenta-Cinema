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
     * Elimina una película por su id.
     * @param id identificador único de la película
     * @return true si la eliminación fue exitosa, false si hubo error
     */
    public boolean deleteMovieById(int id) {
        String sql = "DELETE FROM Cartelera WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo eliminar la película.\nDetalles técnicos: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    /**
     * Busca una película por su título exacto.
     * @param title Título de la película a buscar
     * @return Objeto Movie si se encuentra, null si no existe
     */
    public Movie findMovieByTitle(String title) {
        String sql = "SELECT id, title, director, year, duration, genre FROM Cartelera WHERE title = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return null;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setDirector(rs.getString("director"));
                movie.setYear(rs.getInt("year"));
                movie.setDuration(rs.getInt("duration"));
                movie.setGenre(rs.getString("genre"));
                return movie;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo buscar la película.\nDetalles técnicos: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * Actualiza los datos de una película existente por su id.
     * @param movie Objeto Movie con los datos actualizados (debe tener id válido)
     * @return true si la actualización fue exitosa, false si hubo error
     */
    public boolean updateMovie(Movie movie) {
        if (movie == null || movie.getId() <= 0) {
            System.out.println("[ERROR] Se requiere un objeto película válido con id para actualizar.");
            return false;
        }
        String sql = "UPDATE Cartelera SET title = ?, director = ?, year = ?, duration = ?, genre = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getDirector());
            pstmt.setInt(3, movie.getYear());
            pstmt.setInt(4, movie.getDuration());
            pstmt.setString(5, movie.getGenre());
            pstmt.setInt(6, movie.getId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo actualizar la película.\nDetalles técnicos: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
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
            System.out.println("[ERROR] No se pudo agregar la película. Por favor, verifica que los datos sean correctos y que la conexión a la base de datos esté disponible.\nDetalles técnicos: " + e.getMessage());
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
            System.out.println("[ERROR] No se pudo eliminar la(s) película(s) con el título indicado. Por favor, verifica la conexión a la base de datos.\nDetalles técnicos: " + e.getMessage());
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
            System.out.println("[ADVERTENCIA] Ocurrió un problema al cerrar la conexión con la base de datos.\nDetalles técnicos: " + e.getMessage());
        }
    }
}
