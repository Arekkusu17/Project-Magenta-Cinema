package projectmagenta.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana principal del sistema de cines Magenta
 * Contiene la barra de herramientas y menús para acceder a las diferentes funcionalidades
 * 
 * @author Alex Fernandez
 */
public class MainFrame extends JFrame {

    // Main desktop area for internal frames
    private JDesktopPane desktopPane;
    // Status bar label
    private JLabel statusLabel;
    // Desktop cartelera table model for live updates
    private DefaultTableModel desktopTableModel;
    // List of observers for movie changes
    private List<MovieChangeListener> movieChangeListeners;
    
    // Interface for observing movie changes
    public interface MovieChangeListener {
        void onMovieChanged();
    }

    /**
     * Constructor: sets up the main window and its components.
     */
    public MainFrame() {
        // Initialize observers list
        movieChangeListeners = new ArrayList<>();
        // Initialize all GUI components
        initComponents();
        // Set up window event listeners
        setupEventListeners();

    }

    /**
     * Initializes all main components and layout.
     * Beginners: You can add more panels or change layout here.
     */
    private void initComponents() {
        setTitle("Sistema de Gestión de Películas - Cines Magenta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set initial window size and minimum size
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create the menu bar (top)
        createMenuBar();

        // Create the toolbar (top)
        createToolBar();

        // Create the main desktop area for internal windows
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(240, 240, 240));
        add(desktopPane, BorderLayout.CENTER);

        // Add a welcome panel for beginners
        showWelcomePanel();

        // Create the status bar (bottom)
        createStatusBar();

        // Set the system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo configurar el Look and Feel: " + e.getMessage());
        }

        // Center the window again (in case look and feel changed size)
        setLocationRelativeTo(null);
    }
    
    /**
     * Removes the menu bar (Archivo, Películas, Ayuda) as requested.
     */
    private void createMenuBar() {
        // No menu bar
        setJMenuBar(null);
    }
    
    /**
     * Creates the toolbar with large, clear buttons and tooltips.
     * Beginners: Add more buttons for new features.
     */
    private void createToolBar() {
        JToolBar toolBar = new JToolBar("Herramientas");
        toolBar.setFloatable(false);

        // Add Movie button (large, clear)

    JButton addButton = new JButton("Agregar Película");
    addButton.setIcon(createImageIcon("/projectmagenta/view/icons/add_movie.png"));
    addButton.setToolTipText("Agregar nueva película (Ctrl+N)");
    addButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
    addButton.setFocusable(false);
    addButton.setPreferredSize(new Dimension(240, 56));
    addButton.setIconTextGap(16);
    addButton.addActionListener(e -> openAddMovieForm());

    // Edit Movie button
    JButton editButton = new JButton("Modificar Película");
    editButton.setIcon(createImageIcon("/projectmagenta/view/icons/edit_movie.png"));
    editButton.setToolTipText("Modificar una película existente");
    editButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
    editButton.setFocusable(false);
    editButton.setPreferredSize(new Dimension(260, 56));
    editButton.setIconTextGap(16);
    editButton.addActionListener(e -> openEditMovieForm());

    // Delete Movie button
    JButton deleteButton = new JButton("Eliminar Película");
    deleteButton.setIcon(createImageIcon("/projectmagenta/view/icons/delete_movie.png"));
    deleteButton.setToolTipText("Eliminar una película existente");
    deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
    deleteButton.setFocusable(false);
    deleteButton.setPreferredSize(new Dimension(260, 56));
    deleteButton.setIconTextGap(16);
    deleteButton.addActionListener(e -> openDeleteMovieForm());

    // View All Movies button
    JButton viewAllButton = new JButton("Ver Cartelera");
    // Try to use an existing icon, fallback to no icon if not found
    ImageIcon viewIcon = createImageIcon("/projectmagenta/view/icons/view_movies.png");
    if (viewIcon == null) {
        // Create a simple list-like icon if the image doesn't exist
        viewIcon = createListIcon();
    }
    viewAllButton.setIcon(viewIcon);
    viewAllButton.setToolTipText("Ver todas las películas en cartelera");
    viewAllButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
    viewAllButton.setFocusable(false);
    viewAllButton.setPreferredSize(new Dimension(220, 56));
    viewAllButton.setIconTextGap(16);
    viewAllButton.addActionListener(e -> openViewAllMoviesForm());

        toolBar.add(Box.createHorizontalStrut(10)); // Padding
        toolBar.add(addButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(editButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(deleteButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(viewAllButton);
        toolBar.add(Box.createHorizontalGlue());

        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Abre el formulario para modificar películas existentes.
     */
    private void openEditMovieForm() {
        EditMovieForm editForm = new EditMovieForm();
        // Set up observer for live updates
        editForm.setMainFrame(this);
        addInternalFrame(editForm, "Modificar Película");
        updateStatus("Formulario de modificar película abierto");
    }
    
    /**
     * Abre el formulario para eliminar películas existentes.
     */
    private void openDeleteMovieForm() {
        DeleteMovieForm deleteForm = new DeleteMovieForm();
        // Set up observer for live updates
        deleteForm.setMainFrame(this);
        addInternalFrame(deleteForm, "Eliminar Película");
        updateStatus("Formulario de eliminar película abierto");
    }
    
    /**
     * Abre el formulario para ver todas las películas en cartelera.
     */
    private void openViewAllMoviesForm() {
        ViewAllMoviesForm viewAllForm = new ViewAllMoviesForm();
        addInternalFrame(viewAllForm, "Cartelera Completa");
        updateStatus("Vista de cartelera completa abierta");
    }
    
    /**
     * Creates the status bar at the bottom of the window.
     * Beginners: You can add more info or icons here.
     */
    private void createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        statusLabel = new JLabel("Listo");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JLabel connectionLabel = new JLabel("Conectado a Cine_DB");
        connectionLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        connectionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        connectionLabel.setToolTipText("Estado de la conexión a la base de datos");

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(connectionLabel, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crea un ImageIcon escalado para los botones de la barra de herramientas.
     * @param path Ruta del recurso (por ejemplo, "/projectmagenta/view/icons/add.png")
     * @return ImageIcon escalado o null si no se encuentra
     */
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.err.println("No se pudo encontrar el icono: " + path);
            return null;
        }
    }
    
    /**
     * Crea un icono simple de lista para el botón de ver cartelera.
     * @return ImageIcon con un dibujo de lista
     */
    private ImageIcon createListIcon() {
        int size = 40;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(70, 130, 180));
        g2.setStroke(new BasicStroke(2.0f));
        
        // Dibujar líneas que representen una lista
        int startX = 8;
        int lineHeight = 6;
        for (int i = 0; i < 5; i++) {
            int y = 8 + (i * lineHeight);
            g2.drawLine(startX, y, size - 8, y);
        }
        
        // Dibujar puntos de viñeta
        g2.fillOval(3, 6, 4, 4);
        g2.fillOval(3, 12, 4, 4);
        g2.fillOval(3, 18, 4, 4);
        g2.fillOval(3, 24, 4, 4);
        g2.fillOval(3, 30, 4, 4);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Sets up window event listeners (e.g., confirm on close).
     * Beginners: Add more listeners for custom behavior.
     */
    private void setupEventListeners() {
        // Confirm before closing the app
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(MainFrame.this,
                        "¿Está seguro de que desea salir?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
        
    /**
     * Opens the Add Movie form as an internal window.
     * Beginners: Add more forms using this pattern.
     */
    private void openAddMovieForm() {
        AddMovieForm addForm = new AddMovieForm();
        // Set up observer for live updates
        addForm.setMainFrame(this);
        addInternalFrame(addForm, "Agregar Película");
        updateStatus("Formulario de agregar película abierto");
    }

    /**
     * Adds an internal frame (window) to the desktop pane, centered.
     * Beginners: Use this for any new forms you create.
     */
    private void addInternalFrame(JInternalFrame frame, String title) {
        frame.setTitle(title);
        frame.setResizable(true);
        frame.setClosable(true);
        frame.setMaximizable(true);
        frame.setIconifiable(true);

        // Set a default size for the internal frame if not already set
        if (frame.getWidth() == 0 || frame.getHeight() == 0) {
            frame.setSize(600, 400); // Default size
        }

        // Center the internal frame
        Dimension desktopSize = desktopPane.getSize();
        Dimension frameSize = frame.getSize();

        int x = (desktopSize.width - frameSize.width) / 2;
        int y = (desktopSize.height - frameSize.height) / 2;

        frame.setLocation(x, y);

        desktopPane.add(frame);
        frame.setVisible(true);

        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            System.out.println("Error al seleccionar frame: " + e.getMessage());
        }
    }

    /**
     * Shows a cartelera table panel on the main desktop.
     * Displays an overview of all movies directly on the desktop.
     */
    private void showWelcomePanel() {
        JPanel carteleraPanel = new JPanel(new BorderLayout(10, 10));
        carteleraPanel.setBackground(new Color(255, 255, 255, 240));
        carteleraPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
        carteleraPanel.setFont(defaultFont);

        // Header with title
        JLabel titleLabel = new JLabel("Cartelera de Cines Magenta", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        carteleraPanel.add(titleLabel, BorderLayout.NORTH);

        // Create table for movies
        String[] columnNames = {"ID", "Título", "Director", "Año", "Duración", "Género"};
        desktopTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        JTable moviesTable = new JTable(desktopTableModel);
        moviesTable.setFont(defaultFont);
        moviesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        moviesTable.setRowHeight(22);
        moviesTable.setShowGrid(true);
        moviesTable.setGridColor(Color.LIGHT_GRAY);
        moviesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure column widths
        moviesTable.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        moviesTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        moviesTable.getColumnModel().getColumn(2).setPreferredWidth(130); // Director
        moviesTable.getColumnModel().getColumn(3).setPreferredWidth(50);  // Año
        moviesTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Duración
        moviesTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Género

        // Load movies data
        loadMoviesData(desktopTableModel);

        JScrollPane scrollPane = new JScrollPane(moviesTable);
        scrollPane.setPreferredSize(new Dimension(650, 300));
        carteleraPanel.add(scrollPane, BorderLayout.CENTER);

        // Create internal frame
        JInternalFrame carteleraFrame = new JInternalFrame("Cartelera Principal", false, false, false, false);
        carteleraFrame.setFont(defaultFont);
        carteleraFrame.setContentPane(carteleraPanel);
        carteleraFrame.setSize(700, 400);
        
        // Center the frame
        int x = Math.max((desktopPane.getWidth() - 700) / 2, 20);
        int y = Math.max((desktopPane.getHeight() - 400) / 2, 20);
        carteleraFrame.setLocation(x, y);
        
        // Remove title bar
        ((javax.swing.plaf.basic.BasicInternalFrameUI) carteleraFrame.getUI()).setNorthPane(null);
        carteleraFrame.setVisible(true);
        desktopPane.add(carteleraFrame);
        
        try {
            carteleraFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            // Ignore
        }
    }

    /**
     * Loads movies data into the table model.
     */
    private void loadMoviesData(DefaultTableModel tableModel) {
        try {
            projectmagenta.dao.MovieDAO movieDAO = new projectmagenta.dao.MovieDAO();
            java.util.List<projectmagenta.model.Movie> movies = movieDAO.getAllMovies();
            
            for (projectmagenta.model.Movie movie : movies) {
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
        } catch (Exception e) {
            // If there's an error loading movies, show a placeholder message
            Object[] row = {"Error", "No se pudieron cargar las películas", "-", "-", "-", "-"};
            tableModel.addRow(row);
        }
    }
    
    /**
     * Refreshes the desktop cartelera table with current data.
     */
    public void refreshDesktopCartelera() {
        if (desktopTableModel != null) {
            SwingUtilities.invokeLater(() -> {
                // Clear existing data
                desktopTableModel.setRowCount(0);
                // Reload data
                loadMoviesData(desktopTableModel);
                updateStatus("Cartelera actualizada");
            });
        }
    }
    
    /**
     * Adds a movie change listener.
     */
    public void addMovieChangeListener(MovieChangeListener listener) {
        movieChangeListeners.add(listener);
    }
    
    /**
     * Notifies all listeners that movies have changed.
     */
    public void notifyMovieChanged() {
        refreshDesktopCartelera();
        for (MovieChangeListener listener : movieChangeListeners) {
            listener.onMovieChanged();
        }
    }
    
    /**
     * Gets the singleton instance of MainFrame (for accessing from other forms).
     */
    private static MainFrame instance;
    
    public static MainFrame getInstance() {
        return instance;
    }
    
    public static void setInstance(MainFrame mainFrame) {
        instance = mainFrame;
    }
  
    /**
     * Updates the status bar message.
     * Beginners: Call this to show feedback to the user.
     */
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    /**
     * Main entry point. Launches the application.
     * Beginners: You can change the look and feel here.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Try to use Nimbus look and feel for a modern look
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Start the main window
            MainFrame mainFrame = new MainFrame();
            MainFrame.setInstance(mainFrame);
            mainFrame.setVisible(true);
        });
    }
}