package projectmagenta.view;

import projectmagenta.controller.MovieController;
import projectmagenta.model.Movie;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Formulario para modificar películas en el sistema.
 * Incluye validaciones y botón para limpiar campos.
 * @author Alex Fernandez
 */
public class EditMovieForm extends JInternalFrame {
    private MovieController movieController;
    private MainFrame mainFrame;
    
    /**
     * Sets the main frame reference for live updates.
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    // Componentes de búsqueda
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JButton searchButton;
    private JList<Movie> resultsList;
    private DefaultListModel<Movie> listModel;
    private JScrollPane resultsScrollPane;
    
    // Componentes del formulario
    private JTextField titleField;
    private JTextField directorField;
    private JTextField yearField;
    private JTextField durationField;
    private JTextField genreField;
    private JButton updateButton;
    private JButton clearButton;
    private JButton cancelButton;
    
    private Movie currentMovie;

    public EditMovieForm() {
        this.movieController = new MovieController();
        setTitle("Editar Película");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setLayout(new BorderLayout());
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        pack();
    }

    private void initializeComponents() {
        // Componentes de búsqueda
        searchTypeCombo = new JComboBox<>(new String[]{"Título exacto", "ID", "Título parcial"});
        searchField = new JTextField(20);
        searchButton = new JButton("Buscar");
        
        // Lista de resultados
        listModel = new DefaultListModel<>();
        resultsList = new JList<>(listModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setCellRenderer(new MovieListCellRenderer());
        resultsScrollPane = new JScrollPane(resultsList);
        resultsScrollPane.setPreferredSize(new Dimension(400, 100));
        resultsScrollPane.setVisible(false); // Inicialmente oculto
        
        // Componentes del formulario
        titleField = new JTextField(20);
        directorField = new JTextField(20);
        yearField = new JTextField(10);
        durationField = new JTextField(10);
        genreField = new JTextField(15);
        updateButton = new JButton("Actualizar");
        clearButton = new JButton("Limpiar");
        cancelButton = new JButton("Cancelar");
        
        // Inicialmente deshabilitados hasta que se seleccione una película
        setFormFieldsEnabled(false);
    }

    private void setupLayout() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel de búsqueda
        JPanel searchPanel = createSearchPanel();
        
        // Panel de resultados
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.add(new JLabel("Resultados de búsqueda:"), BorderLayout.NORTH);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);
        
        // Panel del formulario
        JPanel formPanel = createFormPanel();
        
        // Panel de botones
        JPanel buttonPanel = createButtonPanel();
        
        // Agregar al panel principal
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(resultsPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Película"));
        
        panel.add(new JLabel("Buscar por:"));
        panel.add(searchTypeCombo);
        panel.add(new JLabel("Valor:"));
        panel.add(searchField);
        panel.add(searchButton);
        
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Película"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        // Director
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Director:"), gbc);
        gbc.gridx = 1;
        panel.add(directorField, gbc);

        // Año
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        panel.add(yearField, gbc);

        // Duración
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 1;
        panel.add(durationField, gbc);

        // Género
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1;
        panel.add(genreField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        // Actualizar
        ImageIcon updateIcon = null;
        java.net.URL updateURL = getClass().getResource("/projectmagenta/view/icons/save.png");
        if (updateURL != null) {
            Image img = new ImageIcon(updateURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            updateIcon = new ImageIcon(img);
        }
        updateButton.setIcon(updateIcon);
        updateButton.setToolTipText("Actualizar la película");
        updateButton.setPreferredSize(new Dimension(120, 35));
        
        // Limpiar
        ImageIcon clearIcon = null;
        java.net.URL clearURL = getClass().getResource("/projectmagenta/view/icons/clean.png");
        if (clearURL != null) {
            Image img = new ImageIcon(clearURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            clearIcon = new ImageIcon(img);
        }
        clearButton.setIcon(clearIcon);
        clearButton.setToolTipText("Limpiar todos los campos");
        clearButton.setPreferredSize(new Dimension(120, 35));
        
        // Cancelar
        ImageIcon cancelIcon = null;
        java.net.URL cancelURL = getClass().getResource("/projectmagenta/view/icons/cancel.png");
        if (cancelURL != null) {
            Image img = new ImageIcon(cancelURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            cancelIcon = new ImageIcon(img);
        }
        cancelButton.setIcon(cancelIcon);
        cancelButton.setToolTipText("Cancelar y cerrar");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        
        panel.add(updateButton);
        panel.add(clearButton);
        panel.add(cancelButton);
        
        return panel;
    }

    private void setupEventListeners() {
        // Listener para el botón de búsqueda
        searchButton.addActionListener(e -> performSearch());
        
        // Listener para Enter en el campo de búsqueda
        searchField.addActionListener(e -> performSearch());
        
        // Listener para cambio de tipo de búsqueda
        searchTypeCombo.addActionListener(e -> onSearchTypeChanged());
        
        // Listener para selección en la lista de resultados
        resultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Movie selectedMovie = resultsList.getSelectedValue();
                if (selectedMovie != null) {
                    loadMovieData(selectedMovie);
                }
            }
        });
        
        // Listener para botón de actualizar
        updateButton.addActionListener(e -> updateMovie());
        
        // Listener para botón de limpiar
        clearButton.addActionListener(e -> clearFields());
        
        // Listener para botón de cancelar
        cancelButton.addActionListener(e -> {
            if (hasUnsavedChanges()) {
                int result = JOptionPane.showConfirmDialog(
                    EditMovieForm.this,
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
        });
    }

    private void onSearchTypeChanged() {
        String selectedType = (String) searchTypeCombo.getSelectedItem();
        
        // Limpiar resultados anteriores
        listModel.clear();
        resultsScrollPane.setVisible(false);
        clearMovieFields();
        setFormFieldsEnabled(false);
        
        // Ajustar placeholder del campo de búsqueda
        if ("ID".equals(selectedType)) {
            searchField.setToolTipText("Ingrese el ID de la película (número)");
        } else if ("Título exacto".equals(selectedType)) {
            searchField.setToolTipText("Ingrese el título exacto de la película");
        } else if ("Título parcial".equals(selectedType)) {
            searchField.setToolTipText("Ingrese parte del título de la película");
        }
        
        repaint();
    }

    private void performSearch() {
        String searchValue = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (searchValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un valor para buscar.", "Valor requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpiar resultados anteriores
        listModel.clear();
        clearMovieFields();
        setFormFieldsEnabled(false);
        
        try {
            if ("ID".equals(searchType)) {
                performSearchById(searchValue);
            } else if ("Título exacto".equals(searchType)) {
                performSearchByExactTitle(searchValue);
            } else if ("Título parcial".equals(searchType)) {
                performSearchByPartialTitle(searchValue);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error durante la búsqueda: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void performSearchById(String idString) {
        try {
            int id = Integer.parseInt(idString);
            Movie movie = movieController.findMovieById(id);
            
            if (movie != null) {
                loadMovieData(movie);
                resultsScrollPane.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna película con ID: " + id, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "ID inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearchByExactTitle(String title) {
        Movie movie = movieController.findMovieByTitle(title);
        
        if (movie != null) {
            loadMovieData(movie);
            resultsScrollPane.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ninguna película con el título exacto: " + title, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void performSearchByPartialTitle(String partialTitle) {
        List<Movie> movies = movieController.findMoviesByPartialTitle(partialTitle);
        
        System.out.println("[DEBUG] Búsqueda parcial encontró " + movies.size() + " películas");
        
        if (movies.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron películas que contengan: " + partialTitle, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            resultsScrollPane.setVisible(false);
        } else if (movies.size() == 1) {
            // Si solo hay un resultado, cargarlo directamente
            loadMovieData(movies.get(0));
            resultsScrollPane.setVisible(false);
        } else {
            // Múltiples resultados, mostrar la lista
            for (Movie movie : movies) {
                listModel.addElement(movie);
            }
            resultsScrollPane.setVisible(true);
            
            // Forzar repaint y repack
            revalidate();
            repaint();
            pack();
            
            JOptionPane.showMessageDialog(this, "Se encontraron " + movies.size() + " películas. Seleccione una de la lista.", "Múltiples resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadMovieData(Movie movie) {
        this.currentMovie = movie;
        titleField.setText(movie.getTitle());
        directorField.setText(movie.getDirector());
        yearField.setText(String.valueOf(movie.getYear()));
        durationField.setText(String.valueOf(movie.getDuration()));
        genreField.setText(movie.getGenre());
        setFormFieldsEnabled(true);
        
        System.out.println("[DEBUG] Datos cargados para película: " + movie.getTitle());
    }

    private void clearMovieFields() {
        titleField.setText("");
        directorField.setText("");
        yearField.setText("");
        durationField.setText("");
        genreField.setText("");
        currentMovie = null;
    }

    private void setFormFieldsEnabled(boolean enabled) {
        titleField.setEnabled(enabled);
        directorField.setEnabled(enabled);
        yearField.setEnabled(enabled);
        durationField.setEnabled(enabled);
        genreField.setEnabled(enabled);
        updateButton.setEnabled(enabled);
    }

    private void updateMovie() {
        if (currentMovie == null) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar y seleccionar una película para editar.", "Sin película seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Crear una copia de la película con los nuevos datos
            Movie updatedMovie = new Movie();
            updatedMovie.setId(currentMovie.getId());
            updatedMovie.setTitle(titleField.getText().trim());
            updatedMovie.setDirector(directorField.getText().trim());
            updatedMovie.setYear(Integer.parseInt(yearField.getText().trim()));
            updatedMovie.setDuration(Integer.parseInt(durationField.getText().trim()));
            updatedMovie.setGenre(genreField.getText().trim());

            MovieController.MovieResult result = movieController.updateMovie(updatedMovie);
            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Película actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                currentMovie = updatedMovie; // Actualizar la referencia local
                // Trigger live update
                if (mainFrame != null) {
                    mainFrame.notifyMovieChanged();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la película: " + result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año y la duración deben ser números válidos.", "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        searchField.setText("");
        listModel.clear();
        resultsScrollPane.setVisible(false);
        clearMovieFields();
        setFormFieldsEnabled(false);
        
        revalidate();
        repaint();
        pack();
    }

    private boolean hasUnsavedChanges() {
        return !searchField.getText().trim().isEmpty() ||
               !titleField.getText().trim().isEmpty() ||
               !directorField.getText().trim().isEmpty() ||
               !yearField.getText().trim().isEmpty() ||
               !durationField.getText().trim().isEmpty() ||
               !genreField.getText().trim().isEmpty();
    }

    // Renderer personalizado para la lista de películas
    private static class MovieListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Movie) {
                Movie movie = (Movie) value;
                setText(String.format("[ID: %d] %s (%d) - %s", 
                    movie.getId(), movie.getTitle(), movie.getYear(), movie.getDirector()));
            }
            
            return this;
        }
    }
}