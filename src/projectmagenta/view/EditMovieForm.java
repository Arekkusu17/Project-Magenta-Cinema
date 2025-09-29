package projectmagenta.view;

import projectmagenta.controller.MovieController;
import projectmagenta.model.Movie;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

/**
 * Formulario para modificar películas existentes en el sistema.
 * Permite buscar, mostrar, editar y guardar cambios de una película.
 * Incluye validaciones y botón para limpiar campos.
 * Muestra mensajes de error amigables si los datos son inválidos.
 * 
 * @author Alex Fernández
 */

public class EditMovieForm extends JInternalFrame {
    private JTextField txtTitulo;
    private JTextField txtDirector;
    private JSpinner spinnerAño;
    private JSpinner spinnerDuracion;
    private JComboBox<String> comboGenero;
    private JButton searchButton;
    private JButton saveButton;
    private JButton cleanButton;
    private JButton cancelButton;

    private MovieController movieController;
    private Movie currentMovie;

    /**
     * Constructor. Inicializa el formulario y los listeners.
     */
    public EditMovieForm() {
        movieController = new MovieController();
        initComponents();
        setupEventListeners();
    }

    /**
     * Inicializa los componentes gráficos y el layout del formulario.
     */
    private void initComponents() {
        setTitle("Modificar Película");
        setSize(500, 480);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Crea el panel de búsqueda y edición de datos de la película.
     * @return JPanel con los campos de la película
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Buscar y Modificar Película"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Título (clave de búsqueda)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Título a buscar *:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTitulo = new JTextField(20);
        txtTitulo.setToolTipText("Ingrese el título de la película a buscar");
        panel.add(txtTitulo, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        searchButton = new JButton("Buscar");
        panel.add(searchButton, gbc);

        // Director
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Director *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtDirector = new JTextField(20);
        txtDirector.setToolTipText("Ingrese el nombre del director");
        panel.add(txtDirector, gbc);
        gbc.gridwidth = 1;

        // Año
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Año *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        spinnerAño = new JSpinner(new SpinnerNumberModel(currentYear, 1900, currentYear + 5, 1));
        spinnerAño.setToolTipText("Seleccione el año de la película");
        panel.add(spinnerAño, gbc);
        gbc.gridwidth = 1;

        // Duración
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Duración (minutos) *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        spinnerDuracion = new JSpinner(new SpinnerNumberModel(90, 1, 999, 1));
        spinnerDuracion.setToolTipText("Ingrese la duración en minutos");
        panel.add(spinnerDuracion, gbc);
        gbc.gridwidth = 1;

        // Género
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Género *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboGenero = new JComboBox<>(Movie.GENEROS);
        comboGenero.setToolTipText("Seleccione el género de la película");
        panel.add(comboGenero, gbc);
        gbc.gridwidth = 1;

        // Nota sobre campos obligatorios
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JLabel noteLabel = new JLabel("* Campos obligatorios");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        panel.add(noteLabel, gbc);

        return panel;
    }

    /**
     * Crea el panel de botones de acción (Guardar, Limpiar, Cancelar).
     * @return JPanel con los botones principales
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        // Guardar Cambios
        saveButton = new JButton("Guardar Cambios");
        ImageIcon saveIcon = null;
        java.net.URL saveURL = getClass().getResource("/projectmagenta/view/icons/save.png");
        if (saveURL != null) {
            Image img = new ImageIcon(saveURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            saveIcon = new ImageIcon(img);
        }
        saveButton.setIcon(saveIcon);
        saveButton.setToolTipText("Guardar los cambios de la película");
        saveButton.setPreferredSize(new Dimension(140, 35));

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
     * Configura los listeners de los botones y acciones del formulario.
     */
    private void setupEventListeners() {
        // Buscar película
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPelicula();
            }
        });
        // Guardar cambios
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });
        // Limpiar campos
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        // Cancelar
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Busca una película por título usando el controlador y muestra sus datos si existe.
     * Muestra mensajes de error si no se encuentra o si el campo está vacío.
     */
    private void buscarPelicula() {
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el título de la película a buscar.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Movie movie = movieController.findMovieByTitle(titulo);
        if (movie == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ninguna película con ese título.", "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            currentMovie = null;
            return;
        }
        // Llenar los campos con los datos encontrados
        currentMovie = movie;
        txtTitulo.setText(movie.getTitle());
        txtDirector.setText(movie.getDirector());
        spinnerAño.setValue(movie.getYear());
        spinnerDuracion.setValue(movie.getDuration());
        comboGenero.setSelectedItem(movie.getGenre());
        JOptionPane.showMessageDialog(this, "Película encontrada. Puede modificar los datos y guardar los cambios.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Valida y guarda los cambios realizados en la película actual.
     * Muestra mensajes de éxito o error según el resultado.
     */
    private void guardarCambios() {
        // Validar que todos los campos estén llenos
        if (txtTitulo.getText().trim().isEmpty() || txtDirector.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar llenos.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentMovie == null) {
            JOptionPane.showMessageDialog(this, "Debe buscar y seleccionar una película antes de guardar cambios.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            currentMovie.setTitle(txtTitulo.getText().trim());
            currentMovie.setDirector(txtDirector.getText().trim());
            currentMovie.setYear((Integer) spinnerAño.getValue());
            currentMovie.setDuration((Integer) spinnerDuracion.getValue());
            currentMovie.setGenre((String) comboGenero.getSelectedItem());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        MovieController.MovieResult result = movieController.updateMovie(currentMovie);
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            currentMovie = null;
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia todos los campos del formulario y restablece los valores por defecto.
     */
    private void limpiarCampos() {
        txtTitulo.setText("");
        txtDirector.setText("");
        spinnerAño.setValue(Calendar.getInstance().get(Calendar.YEAR));
        spinnerDuracion.setValue(90);
        comboGenero.setSelectedIndex(0);
    }
}
