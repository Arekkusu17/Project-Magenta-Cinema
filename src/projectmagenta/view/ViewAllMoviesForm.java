package projectmagenta.view;

import projectmagenta.dao.MovieDAO;
import projectmagenta.model.Movie;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Formulario para visualizar todas las películas de la cartelera en una tabla.
 * Permite ordenar por columnas y refrescar la vista.
 * 
 * @author Alex Fernandez
 */
public class ViewAllMoviesForm extends JInternalFrame {
    
    private JTable moviesTable;
    private DefaultTableModel tableModel;
    private MovieDAO movieDAO;
    private JButton refreshButton;
    private JLabel statusLabel;
    
    // Componentes de filtrado
    private JCheckBox enableGenreFilter;
    private JComboBox<String> genreFilter;
    private JCheckBox enableYearFilter;
    private JSpinner fromYearSpinner;
    private JSpinner toYearSpinner;
    private JButton applyFiltersButton;
    private JButton clearFiltersButton;
    
    // Lista completa de películas para filtrado
    private List<Movie> allMovies;
    
    /**
     * Constructor: inicializa los componentes del formulario.
     */
    public ViewAllMoviesForm() {
        movieDAO = new MovieDAO();
        initComponents();
        loadMovies();
    }
    
    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initComponents() {
        setTitle("Cartelera Completa - Todas las Películas");
        setSize(900, 600);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Panel superior con controles de filtrado y botón de refrescar
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Panel de filtros
        JPanel filtersPanel = createFiltersPanel();
        topPanel.add(filtersPanel, BorderLayout.CENTER);
        
        // Panel de botón refrescar
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refrescar");
        refreshButton.setIcon(createRefreshIcon());
        refreshButton.setToolTipText("Actualizar la lista de películas");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMovies();
            }
        });
        refreshPanel.add(refreshButton);
        topPanel.add(refreshPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Configurar tabla
        String[] columnNames = {"ID", "Título", "Director", "Año", "Duración", "Género"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };
        
        moviesTable = new JTable(tableModel);
        moviesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moviesTable.setAutoCreateRowSorter(true);
        
        // Configurar anchos de columnas
        moviesTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        moviesTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Título
        moviesTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Director
        moviesTable.getColumnModel().getColumn(3).setPreferredWidth(70);  // Año
        moviesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Duración
        moviesTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Género
        
        // Scroll pane para la tabla
        JScrollPane scrollPane = new JScrollPane(moviesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Películas en Cartelera"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con información de estado
        JPanel bottomPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Cargando películas...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Configurar fuentes
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 12);
        moviesTable.setFont(tableFont);
        moviesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Configurar altura de filas
        moviesTable.setRowHeight(25);
        
        // Alternar colores de filas
        moviesTable.setShowGrid(true);
        moviesTable.setGridColor(Color.LIGHT_GRAY);
    }
    
    /**
     * Crea el panel de filtros con las opciones de búsqueda.
     */
    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel();
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda"));
        filtersPanel.setLayout(new BorderLayout());
        
        // Panel principal de filtros con GridBagLayout para mejor organización
        JPanel filtersContent = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Primera fila: Filtro por género
        gbc.gridx = 0; gbc.gridy = 0;
        enableGenreFilter = new JCheckBox("Filtrar por género:");
        enableGenreFilter.setToolTipText("Activar/desactivar filtro por género");
        enableGenreFilter.addActionListener(e -> toggleGenreFilter());
        filtersContent.add(enableGenreFilter, gbc);
        
        gbc.gridx = 1;
        genreFilter = new JComboBox<>();
        genreFilter.setPreferredSize(new Dimension(150, 25));
        genreFilter.setToolTipText("Seleccionar género para filtrar");
        genreFilter.setEnabled(false); // Inicialmente deshabilitado
        filtersContent.add(genreFilter, gbc);
        
        // Segunda fila: Filtro por año
        gbc.gridx = 0; gbc.gridy = 1;
        enableYearFilter = new JCheckBox("Filtrar por año:");
        enableYearFilter.setToolTipText("Activar/desactivar filtro por rango de años");
        enableYearFilter.addActionListener(e -> toggleYearFilter());
        filtersContent.add(enableYearFilter, gbc);
        
        // Panel para los spinners de año
        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        yearPanel.add(new JLabel("desde:"));
        fromYearSpinner = new JSpinner(new SpinnerNumberModel(1900, 1900, 2030, 1));
        fromYearSpinner.setPreferredSize(new Dimension(80, 25));
        fromYearSpinner.setToolTipText("Año inicial del rango de búsqueda");
        fromYearSpinner.setEnabled(false); // Inicialmente deshabilitado
        yearPanel.add(fromYearSpinner);
        
        yearPanel.add(new JLabel("hasta:"));
        toYearSpinner = new JSpinner(new SpinnerNumberModel(2030, 1900, 2030, 1));
        toYearSpinner.setPreferredSize(new Dimension(80, 25));
        toYearSpinner.setToolTipText("Año final del rango de búsqueda");
        toYearSpinner.setEnabled(false); // Inicialmente deshabilitado
        yearPanel.add(toYearSpinner);
        
        gbc.gridx = 1;
        filtersContent.add(yearPanel, gbc);
        
        // Agregar el contenido de filtros al panel principal
        filtersPanel.add(filtersContent, BorderLayout.CENTER);
        
        // Panel de botones separado en la parte derecha
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        applyFiltersButton = new JButton("Aplicar Filtros");
        applyFiltersButton.setToolTipText("Aplicar los filtros marcados");
        applyFiltersButton.addActionListener(e -> applyFilters());
        buttonsPanel.add(applyFiltersButton);
        
        clearFiltersButton = new JButton("Limpiar Filtros");
        clearFiltersButton.setToolTipText("Desmarcar todos los filtros y mostrar todas las películas");
        clearFiltersButton.addActionListener(e -> clearFilters());
        buttonsPanel.add(clearFiltersButton);
        
        filtersPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return filtersPanel;
    }
    
    /**
     * Habilita o deshabilita los componentes del filtro de género.
     */
    private void toggleGenreFilter() {
        boolean enabled = enableGenreFilter.isSelected();
        genreFilter.setEnabled(enabled);
        if (!enabled) {
            genreFilter.setSelectedIndex(0); // Reset to "Todos los géneros"
        }
    }
    
    /**
     * Habilita o deshabilita los componentes del filtro de año.
     */
    private void toggleYearFilter() {
        boolean enabled = enableYearFilter.isSelected();
        fromYearSpinner.setEnabled(enabled);
        toYearSpinner.setEnabled(enabled);
        if (!enabled) {
            fromYearSpinner.setValue(1900);
            toYearSpinner.setValue(2030);
        }
    }
    
    /**
     * Carga todas las películas desde la base de datos y las muestra en la tabla.
     */
    public void loadMovies() {
        statusLabel.setText("Cargando películas...");
        refreshButton.setEnabled(false);
        
        // Limpiar tabla actual
        tableModel.setRowCount(0);
        
        try {
            allMovies = movieDAO.getAllMovies();
            
            if (allMovies.isEmpty()) {
                statusLabel.setText("No hay películas en la cartelera");
            } else {
                // Actualizar el combo de géneros
                updateGenreFilter();
                
                // Mostrar todas las películas inicialmente
                displayMovies(allMovies);
                
                statusLabel.setText("Se encontraron " + allMovies.size() + " película(s) en la cartelera");
            }
            
        } catch (Exception e) {
            statusLabel.setText("Error al cargar las películas");
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las películas: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } finally {
            refreshButton.setEnabled(true);
        }
    }
    
    /**
     * Actualiza el combo box de géneros con los géneros predefinidos.
     */
    private void updateGenreFilter() {
        genreFilter.removeAllItems();
        genreFilter.addItem("Todos los géneros");
        
        // Géneros predefinidos disponibles
        String[] predefinedGenres = {
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
        
        // Agregar géneros predefinidos al combo
        for (String genre : predefinedGenres) {
            genreFilter.addItem(genre);
        }
    }
    
    /**
     * Muestra una lista de películas en la tabla.
     */
    private void displayMovies(List<Movie> movies) {
        // Limpiar tabla actual
        tableModel.setRowCount(0);
        
        // Agregar películas a la tabla
        for (Movie movie : movies) {
            Object[] row = {
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getYear(),
                movie.getDuracionFormateada(),
                movie.getGenre()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Aplica los filtros marcados a la lista de películas.
     */
    private void applyFilters() {
        if (allMovies == null || allMovies.isEmpty()) {
            return;
        }
        
        // Verificar qué filtros están habilitados
        boolean genreFilterActive = enableGenreFilter.isSelected();
        boolean yearFilterActive = enableYearFilter.isSelected();
        
        // Si no hay filtros activos, mostrar todas las películas
        if (!genreFilterActive && !yearFilterActive) {
            displayMovies(allMovies);
            statusLabel.setText("Se encontraron " + allMovies.size() + " película(s) en la cartelera");
            return;
        }
        
        String selectedGenre = null;
        int fromYear = 1900;
        int toYear = 2030;
        
        // Obtener valores de filtros activos
        if (genreFilterActive) {
            selectedGenre = (String) genreFilter.getSelectedItem();
            // Si está en "Todos los géneros", desactivar el filtro de género
            if ("Todos los géneros".equals(selectedGenre)) {
                genreFilterActive = false;
            }
        }
        
        if (yearFilterActive) {
            fromYear = (Integer) fromYearSpinner.getValue();
            toYear = (Integer) toYearSpinner.getValue();
            
            // Validar rango de años
            if (fromYear > toYear) {
                JOptionPane.showMessageDialog(this,
                    "El año inicial no puede ser mayor que el año final.",
                    "Error en el rango de años",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Filtrar películas
        List<Movie> filteredMovies = new java.util.ArrayList<>();
        for (Movie movie : allMovies) {
            boolean includeMovie = true;
            
            // Aplicar filtro de género si está activo
            if (genreFilterActive) {
                boolean matchesGenre = movie.getGenre() != null && movie.getGenre().equals(selectedGenre);
                if (!matchesGenre) {
                    includeMovie = false;
                }
            }
            
            // Aplicar filtro de año si está activo
            if (yearFilterActive && includeMovie) {
                boolean matchesYear = movie.getYear() >= fromYear && movie.getYear() <= toYear;
                if (!matchesYear) {
                    includeMovie = false;
                }
            }
            
            if (includeMovie) {
                filteredMovies.add(movie);
            }
        }
        
        // Mostrar resultados filtrados
        displayMovies(filteredMovies);
        
        // Actualizar estado con información de filtros aplicados
        StringBuilder statusMessage = new StringBuilder();
        if (filteredMovies.isEmpty()) {
            statusMessage.append("No se encontraron películas");
        } else {
            statusMessage.append("Se encontraron ").append(filteredMovies.size()).append(" película(s)");
        }
        
        // Indicar qué filtros están activos
        if (genreFilterActive || yearFilterActive) {
            statusMessage.append(" con filtros: ");
            if (genreFilterActive) {
                statusMessage.append("Género '").append(selectedGenre).append("'");
            }
            if (genreFilterActive && yearFilterActive) {
                statusMessage.append(" y ");
            }
            if (yearFilterActive) {
                statusMessage.append("Años ").append(fromYear).append("-").append(toYear);
            }
        }
        
        statusLabel.setText(statusMessage.toString());
    }
    
    /**
     * Limpia todos los filtros y muestra todas las películas.
     */
    private void clearFilters() {
        if (allMovies == null) {
            return;
        }
        
        // Desmarcar checkboxes
        enableGenreFilter.setSelected(false);
        enableYearFilter.setSelected(false);
        
        // Deshabilitar y resetear componentes
        toggleGenreFilter();
        toggleYearFilter();
        
        // Mostrar todas las películas
        displayMovies(allMovies);
        statusLabel.setText("Se encontraron " + allMovies.size() + " película(s) en la cartelera");
    }
    
    /**
     * Crea un icono simple para el botón de refrescar.
     * @return ImageIcon para el botón de refrescar
     */
    private ImageIcon createRefreshIcon() {
        // Crear un icono simple de refrescar usando gráficos
        int size = 16;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(70, 130, 180));
        g2.setStroke(new BasicStroke(2.0f));
        
        // Dibujar flecha circular
        g2.drawArc(2, 2, size-4, size-4, 30, 300);
        
        // Dibujar punta de flecha
        int[] xPoints = {size-3, size-6, size-6};
        int[] yPoints = {size/2, size/2-2, size/2+2};
        g2.fillPolygon(xPoints, yPoints, 3);
        
        g2.dispose();
        return new ImageIcon(img);
    }
}