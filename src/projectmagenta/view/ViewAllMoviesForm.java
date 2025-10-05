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
        
        // Panel superior con botón de refrescar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshButton = new JButton("Refrescar");
        refreshButton.setIcon(createRefreshIcon());
        refreshButton.setToolTipText("Actualizar la lista de películas");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMovies();
            }
        });
        
        topPanel.add(refreshButton);
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
     * Carga todas las películas desde la base de datos y las muestra en la tabla.
     */
    public void loadMovies() {
        statusLabel.setText("Cargando películas...");
        refreshButton.setEnabled(false);
        
        // Limpiar tabla actual
        tableModel.setRowCount(0);
        
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            
            if (movies.isEmpty()) {
                statusLabel.setText("No hay películas en la cartelera");
            } else {
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
                
                statusLabel.setText("Se encontraron " + movies.size() + " película(s) en la cartelera");
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