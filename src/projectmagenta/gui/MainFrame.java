package projectmagenta.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema de cines Magenta
 * Contiene la barra de herramientas y menús para acceder a las diferentes funcionalidades
 * 
 * @author arekk
 */
public class MainFrame extends JFrame {

    // Main desktop area for internal frames
    private JDesktopPane desktopPane;
    // Status bar label
    private JLabel statusLabel;

    /**
     * Constructor: sets up the main window and its components.
     */
    public MainFrame() {
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
        addButton.setIcon(createIcon("➕"));
        addButton.setToolTipText("Agregar nueva película (Ctrl+N)");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setFocusable(false);
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.addActionListener(e -> openAddMovieForm());

        toolBar.add(Box.createHorizontalStrut(10)); // Padding
        toolBar.add(addButton);
        toolBar.add(Box.createHorizontalGlue());

        add(toolBar, BorderLayout.NORTH);
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
     * Creates a simple emoji icon for toolbar buttons.
     * Beginners: Replace with ImageIcon for custom images if desired.
     */
    private Icon createIcon(String emoji) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                g2.drawString(emoji, x, y + 18);
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return 28; }

            @Override
            public int getIconHeight() { return 28; }
        };
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
     * Shows a simple welcome panel for beginners.
     * You can customize this message or add instructions.
     */
    private void showWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(255, 255, 255, 220));
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));

        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
        welcomePanel.setFont(defaultFont);

        JLabel welcomeLabel = new JLabel("¡Bienvenido/a al Sistema de Gestión de Películas!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tipLabel = new JLabel("Usa el menú o la barra de herramientas para comenzar.");
        tipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(tipLabel);

        // Center the welcome panel in the desktop pane
        welcomePanel.setSize(600, 250);
        int x = (desktopPane.getWidth() - welcomePanel.getWidth()) / 2;
        int y = (desktopPane.getHeight() - welcomePanel.getHeight()) / 2;
        welcomePanel.setLocation(Math.max(x, 0), Math.max(y, 0));
        welcomePanel.setOpaque(true);

        // Use a JInternalFrame for the welcome panel, but remove the title bar (no system menu button)
        JInternalFrame welcomeFrame = new JInternalFrame("Bienvenida", false, false, false, false);
        // Set a default font for the frame as well
        welcomeFrame.setFont(defaultFont);
        welcomeFrame.setContentPane(welcomePanel);
        welcomeFrame.pack();
        welcomeFrame.setSize(600, 250);
        welcomeFrame.setLocation(Math.max(x, 50), Math.max(y, 50));
        // Remove the title bar (north pane) to eliminate the system menu button
        ((javax.swing.plaf.basic.BasicInternalFrameUI) welcomeFrame.getUI()).setNorthPane(null);
        welcomeFrame.setVisible(true);
        desktopPane.add(welcomeFrame);
        // As a fallback, set a default font for popup menus 
        UIManager.put("PopupMenu.font", defaultFont);
        try {
            welcomeFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            // Ignore
        }
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
            new MainFrame().setVisible(true);
        });
    }
}