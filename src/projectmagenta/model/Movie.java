package projectmagenta.model;

/**
 * Modelo de datos que representa una película en el sistema de cines Magenta.
 * Incluye atributos básicos, constructores, validación y utilidades para mostrar información.
 * @author arekk
 */
public class Movie {
    private int id;
    private String title;
    private String director;
    private int year;
    private int duration; // en minutos
    private String genre;
    
    /**
     * Géneros disponibles para las películas.
     */
    public static final String[] GENEROS = {
        "Acción", 
        "Drama", 
        "Comedia", 
        "Terror", 
        "Romance", 
        "Ciencia Ficción", 
        "Thriller", 
        "Aventura", 
        "Animación", 
        "Documental"
    };
    
    /**
     * Constructor vacío. Útil para frameworks y formularios.
     */
    public Movie() {
    }

    /**
     * Constructor completo con todos los campos.
     * @param id identificador único
     * @param titulo título de la película
     * @param director nombre del director
     * @param año año de estreno
     * @param duracion duración en minutos
     * @param genero género de la película
     */
    public Movie(int id, String titulo, String director, int año, int duracion, String genero) {
        this.id = id;
        this.title = titulo;
        this.director = director;
        this.year = año;
        this.duration = duracion;
        this.genre = genero;
    }
    
    /**
     * Constructor sin ID (para inserción en base de datos).
     * @param titulo título de la película
     * @param director nombre del director
     * @param año año de estreno
     * @param duracion duración en minutos
     * @param genero género de la película
     */
    public Movie(String titulo, String director, int año, int duracion, String genero) {
        this.title = titulo;
        this.director = director;
        this.year = año;
        this.duration = duracion;
        this.genre = genero;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    /**
     * Asigna el título de la película validando que no sea vacío ni demasiado largo.
     * @param title título de la película
     * @throws IllegalArgumentException si el título es nulo, vacío o excede 150 caracteres
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (title.length() > 150) {
            throw new IllegalArgumentException("El título no puede exceder 150 caracteres");
        }
        // Permitir letras, números, espacios y signos de puntuación básicos
        if (!title.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s\\.,;:!?\\-()\\[\\]\"']+$")) {
            throw new IllegalArgumentException("El título contiene caracteres no válidos");
        }
        this.title = title;
    }
    
    public String getDirector() {
        return director;
    }
    
    /**
     * Asigna el nombre del director validando formato y longitud.
     * @param director nombre del director
     * @throws IllegalArgumentException si es nulo, vacío, demasiado largo o con caracteres inválidos
     */
    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El director no puede estar vacío");
        }
        if (director.length() > 50) {
            throw new IllegalArgumentException("El nombre del director no puede exceder 50 caracteres");
        }
        // Permitir letras, acentos, ñ, ü, espacios, puntos y guiones
        if (!director.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s\\.\\-]+$")) {
            throw new IllegalArgumentException("El nombre del director solo debe contener letras, espacios, puntos y guiones");
        }
        this.director = director;
    }
    
    public int getYear() {
        return year;
    }
    
    /**
     * Asigna el año de estreno validando rango histórico y futuro.
     * @param year año de estreno
     * @throws IllegalArgumentException si el año es menor a 1888 o mayor a 5 años en el futuro
     */
    public void setYear(int year) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        if (year < 1888) {
            throw new IllegalArgumentException("El año debe ser mayor a 1887 (primera película de la historia)");
        }
        if (year > currentYear + 5) {
            throw new IllegalArgumentException("El año no puede ser mayor a " + (currentYear + 5));
        }
        this.year = year;
    }
    
    public int getDuration() {
        return duration;
    }
    
    /**
     * Asigna la duración de la película validando que sea positiva y razonable.
     * @param duration duración en minutos
     * @throws IllegalArgumentException si la duración es menor o igual a 0 o mayor a 999
     */
    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }
        if (duration > 999) {
            throw new IllegalArgumentException("La duración no puede exceder 999 minutos");
        }
        this.duration = duration;
    }
    
    public String getGenre() {
        return genre;
    }
    
    /**
     * Asigna el género de la película validando que sea uno de los permitidos.
     * @param genre género de la película
     * @throws IllegalArgumentException si el género es nulo, vacío o no está en la lista de géneros válidos
     */
    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("El género es obligatorio");
        }
        boolean validGenre = false;
        for (String g : GENEROS) {
            if (g.equalsIgnoreCase(genre)) {
                validGenre = true;
                break;
            }
        }
        if (!validGenre) {
            throw new IllegalArgumentException("El género seleccionado no es válido");
        }
        this.genre = genre;
    }
    
    /**
     * Devuelve una representación en texto de la película.
     * @return cadena con los datos principales de la película
     */
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", titulo='" + title + '\'' +
                ", director='" + director + '\'' +
                ", año=" + year +
                ", duracion=" + duration + " min" +
                ", genero='" + genre + '\'' +
                '}';
    }

    /**
     * Valida si la película tiene todos los campos necesarios y valores razonables.
     * @return true si es válida, false si falta algún campo o hay valores fuera de rango
     */
    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
               director != null && !director.trim().isEmpty() &&
               year > 1800 && year <= 2030 &&
               duration > 0 &&
               genre != null && !genre.trim().isEmpty();
    }

    /**
     * Devuelve la duración formateada en horas y minutos (ej: "2h 15m").
     * @return duración formateada como texto
     */
    public String getDuracionFormateada() {
        int horas = duration / 60;
        int minutos = duration % 60;
        if (horas > 0) {
            return horas + "h " + minutos + "m";
        } else {
            return minutos + "m";
        }
    }
}