package projectmagenta.view;

import projectmagenta.controller.MovieController;
import javax.swing.*;
import projectmagenta.model.Movie; // Ensure the correct package path for the Movie class
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

/**
 * Formulario para agregar nuevas películas al sistema.
 * Incluye validaciones y botón para limpiar campos.
 * Muestra mensajes de error amigables si los datos son inválidos.
 *
 * @author Alex Fernandez
 */
public class AddMovieForm extends JInternalFrame {
    
    private JTextField txtTitulo;
    private JTextField txtDirector;
    private JSpinner spinnerAño;
    private JSpinner spinnerDuracion;
    private JComboBox<String> comboGenero;
    private JButton saveButton;
    private JButton cleanButton;
    private JButton cancelButton;
    
    private MovieController movieController;
    
    /**
     * Constructor. Inicializa el formulario y los listeners.
     */
    public AddMovieForm() {
        movieController = new MovieController();
        initComponents();
        setupEventListeners();
    }
    
    /**
     * Inicializa los componentes gráficos del formulario.
     */
    private void initComponents() {
        setTitle("Agregar Nueva Película");
        setSize(500, 450);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel del formulario
        JPanel formPanel = createFormPanel();
        
        // Panel de botones
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Crea el panel del formulario con los campos de entrada.
     * @return JPanel con los campos de la película
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información de la Película"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Título *:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTitulo = new JTextField(20);
        txtTitulo.setToolTipText("Ingrese el título de la película (máximo 150 caracteres)");
        panel.add(txtTitulo, gbc);
        
        // Director
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Director *:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtDirector = new JTextField(20);
        txtDirector.setToolTipText("Ingrese el nombre del director (máximo 50 caracteres)");
        
        panel.add(txtDirector, gbc);
        
        // Año
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10); // Restaurar márgenes normales
        panel.add(new JLabel("Año *:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        spinnerAño = new JSpinner(new SpinnerNumberModel(currentYear, 1900, currentYear + 5, 1));
        spinnerAño.setToolTipText("Seleccione el año de la película");
        panel.add(spinnerAño, gbc);
        
        // Duración
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Duración (minutos) *:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        spinnerDuracion = new JSpinner(new SpinnerNumberModel(90, 1, 999, 1));
        spinnerDuracion.setToolTipText("Ingrese la duración en minutos");
        panel.add(spinnerDuracion, gbc);
        
        // Género
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Género *:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        comboGenero = new JComboBox<>(Movie.GENEROS);
        comboGenero.setToolTipText("Seleccione el género de la película");
        panel.add(comboGenero, gbc);
        
        // Nota sobre campos obligatorios
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel noteLabel = new JLabel("* Campos obligatorios");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        panel.add(noteLabel, gbc);
        
        return panel;
    }
    
    /**
     * Crea el panel de botones (Guardar, Limpiar, Cancelar).
     * @return JPanel con los botones de acción
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        // Guardar
        saveButton = new JButton("Guardar");
        ImageIcon saveIcon = null;
        java.net.URL saveURL = getClass().getResource("/projectmagenta/view/icons/save.png");
        if (saveURL != null) {
            Image img = new ImageIcon(saveURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            saveIcon = new ImageIcon(img);
        }
        saveButton.setIcon(saveIcon);
        saveButton.setToolTipText("Guardar la película");
        saveButton.setPreferredSize(new Dimension(120, 35));
        
        // Limpiar
        cleanButton = new JButton("Limpiar");
        ImageIcon cleanIcon = null;
        java.net.URL cleanURL = getClass().getResource("/projectmagenta/view/icons/clean.png");
        if (cleanURL != null) {
            Image img = new ImageIcon(cleanURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            cleanIcon = new ImageIcon(img);
        }
        cleanButton.setIcon(cleanIcon);
        cleanButton.setToolTipText("Limpiar todos los campos");
        cleanButton.setPreferredSize(new Dimension(120, 35));
        
        // Cancelar
        cancelButton = new JButton("Cancelar");
        ImageIcon cancelIcon = null;
        java.net.URL cancelURL = getClass().getResource("/projectmagenta/view/icons/cancel.png");
        if (cancelURL != null) {
            Image img = new ImageIcon(cancelURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            cancelIcon = new ImageIcon(img);
        }
        cancelButton.setIcon(cancelIcon);
        cancelButton.setToolTipText("Cancelar y cerrar");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        
        panel.add(saveButton);
        panel.add(cleanButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * Configura los listeners de los botones y teclas del formulario.
     */
    private void setupEventListeners() {
        // Botón Guardar
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPelicula();
            }
        });
        
        // Botón Limpiar
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanFields();
            }
        });
        
        // Botón Cancelar
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Confirmar si hay cambios sin guardar
                if (pendingChanges()) {
                    int result = JOptionPane.showConfirmDialog(
                        AddMovieForm.this,
                        "Hay cambios sin guardar. ¿Está seguro de que desea cancelar?",
                        "Confirmar cancelación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    if (result == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
        
        // Configurar Enter para guardar
        getRootPane().setDefaultButton(saveButton);
        
        // Configurar teclas de escape
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        actionMap.put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButton.doClick();
            }
        });
    }
    
    /**
     * Intenta guardar la película usando los datos del formulario.
     * Muestra mensajes de validación si hay errores en los campos.
     * Si la inserción es exitosa, limpia el formulario y muestra confirmación.
     */
    private void guardarPelicula() {
        try {
            // Crear objeto Movie con los datos del formulario
            Movie movie = new Movie();
            try {
                movie.setTitle(txtTitulo.getText().trim());
                movie.setDirector(txtDirector.getText().trim());
                movie.setYear((Integer) spinnerAño.getValue());
                movie.setDuration((Integer) spinnerDuracion.getValue());
                movie.setGenre((String) comboGenero.getSelectedItem());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error de Validación",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            // Deshabilitar botón mientras se guarda
            saveButton.setEnabled(false);
            saveButton.setText("Guardando...");
            
            // Usar el controlador para guardar (incluye validaciones)
            SwingWorker<MovieController.MovieResult, Void> worker = new SwingWorker<MovieController.MovieResult, Void>() {
                @Override
                protected MovieController.MovieResult doInBackground() throws Exception {
                    return movieController.addMovie(movie);
                }
                
                @Override
                protected void done() {
                    try {
                        MovieController.MovieResult result = get();
                        if (result.isSuccess()) {
                            JOptionPane.showMessageDialog(
                                AddMovieForm.this,
                                result.getMessage(),
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            cleanFields();
                        } else {
                            JOptionPane.showMessageDialog(
                                AddMovieForm.this,
                                result.getMessage(),
                                "Error de Validación",
                                JOptionPane.WARNING_MESSAGE
                            );
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                            AddMovieForm.this,
                            "Error inesperado: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        saveButton.setEnabled(true);
                        saveButton.setText("Guardar");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al procesar los datos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            saveButton.setEnabled(true);
            saveButton.setText("Guardar");
        }
    }
    
    /**
     * Limpia todos los campos del formulario y restablece los valores por defecto.
     */
    private void cleanFields() {
        txtTitulo.setText("");
        txtDirector.setText("");
        spinnerAño.setValue(Calendar.getInstance().get(Calendar.YEAR));
        spinnerDuracion.setValue(90);
        comboGenero.setSelectedIndex(0);
        
        // Resetear colores de fondo
        txtTitulo.setBackground(Color.WHITE);
        txtDirector.setBackground(Color.WHITE);
        
        // Enfocar el primer campo
        txtTitulo.requestFocus();
        
    }
    
    /**
     * Indica si hay cambios pendientes sin guardar en el formulario.
     * @return true si hay cambios, false si está limpio
     */
    private boolean pendingChanges() {
        return !txtTitulo.getText().trim().isEmpty() ||
               !txtDirector.getText().trim().isEmpty() ||
               !spinnerAño.getValue().equals(Calendar.getInstance().get(Calendar.YEAR)) ||
               !spinnerDuracion.getValue().equals(90) ||
               comboGenero.getSelectedIndex() != 0;
    }
}