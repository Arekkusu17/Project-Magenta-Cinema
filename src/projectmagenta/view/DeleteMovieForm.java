package projectmagenta.view;

import projectmagenta.controller.MovieController;
import projectmagenta.model.Movie;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeleteMovieForm extends JInternalFrame {
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
    
    // Componentes de información
    private JLabel titleLabel;
    private JLabel directorLabel;
    private JLabel yearLabel;
    private JLabel durationLabel;
    private JLabel genreLabel;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton cancelButton;
    
    private Movie currentMovie;

    public DeleteMovieForm() {
        this.movieController = new MovieController();
        setTitle("Eliminar Película");
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
        resultsScrollPane.setVisible(false);
        
        // Componentes de información
        titleLabel = new JLabel("-");
        directorLabel = new JLabel("-");
        yearLabel = new JLabel("-");
        durationLabel = new JLabel("-");
        genreLabel = new JLabel("-");
        deleteButton = new JButton("Eliminar");
        clearButton = new JButton("Limpiar");
        cancelButton = new JButton("Cancelar");
        
        deleteButton.setEnabled(false);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel de búsqueda
        JPanel searchPanel = createSearchPanel();
        
        // Panel de resultados
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.add(new JLabel("Resultados de búsqueda:"), BorderLayout.NORTH);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);
        
        // Panel de información
        JPanel infoPanel = createInfoPanel();
        
        // Panel de botones
        JPanel buttonPanel = createButtonPanel();
        
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(resultsPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
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

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información de la Película"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        panel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Director:"), gbc);
        gbc.gridx = 1;
        panel.add(directorLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        panel.add(yearLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Duración:"), gbc);
        gbc.gridx = 1;
        panel.add(durationLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1;
        panel.add(genreLabel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        // Eliminar
        ImageIcon deleteIcon = null;
        java.net.URL deleteURL = getClass().getResource("/projectmagenta/view/icons/trash.png");
        if (deleteURL != null) {
            Image img = new ImageIcon(deleteURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            deleteIcon = new ImageIcon(img);
        }
        deleteButton.setIcon(deleteIcon);
        deleteButton.setToolTipText("Eliminar la película seleccionada");
        deleteButton.setPreferredSize(new Dimension(120, 35));
        
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
        
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(cancelButton);
        
        return panel;
    }

    private void setupEventListeners() {
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        searchTypeCombo.addActionListener(e -> onSearchTypeChanged());
        
        resultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Movie selectedMovie = resultsList.getSelectedValue();
                if (selectedMovie != null) {
                    loadMovieData(selectedMovie);
                }
            }
        });
        
        deleteButton.addActionListener(e -> deleteMovie());
        clearButton.addActionListener(e -> clearFields());
        
        // Listener para botón de cancelar
        cancelButton.addActionListener(e -> {
            if (hasUnsavedChanges()) {
                int result = JOptionPane.showConfirmDialog(
                    DeleteMovieForm.this,
                    "Hay búsquedas o selecciones sin procesar. ¿Está seguro de que desea cancelar?",
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
        listModel.clear();
        resultsScrollPane.setVisible(false);
        clearMovieData();
        
        String selectedType = (String) searchTypeCombo.getSelectedItem();
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
        
        listModel.clear();
        clearMovieData();
        
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
            loadMovieData(movies.get(0));
            resultsScrollPane.setVisible(false);
        } else {
            for (Movie movie : movies) {
                listModel.addElement(movie);
            }
            resultsScrollPane.setVisible(true);
            
            revalidate();
            repaint();
            pack();
            
            JOptionPane.showMessageDialog(this, "Se encontraron " + movies.size() + " películas. Seleccione una de la lista.", "Múltiples resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadMovieData(Movie movie) {
        this.currentMovie = movie;
        titleLabel.setText(movie.getTitle());
        directorLabel.setText(movie.getDirector());
        yearLabel.setText(String.valueOf(movie.getYear()));
        durationLabel.setText(movie.getDuration() + " minutos");
        genreLabel.setText(movie.getGenre());
        deleteButton.setEnabled(true);
        
        System.out.println("[DEBUG] Datos cargados para eliminar: " + movie.getTitle());
    }

    private void clearMovieData() {
        titleLabel.setText("-");
        directorLabel.setText("-");
        yearLabel.setText("-");
        durationLabel.setText("-");
        genreLabel.setText("-");
        currentMovie = null;
        deleteButton.setEnabled(false);
    }

    private void deleteMovie() {
        if (currentMovie == null) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar y seleccionar una película para eliminar.", "Sin película seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar la película '" + currentMovie.getTitle() + "'?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            MovieController.MovieResult result = movieController.deleteMovieById(currentMovie.getId());
            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Película eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                // Trigger live update
                if (mainFrame != null) {
                    mainFrame.notifyMovieChanged();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la película.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        searchField.setText("");
        listModel.clear();
        resultsScrollPane.setVisible(false);
        clearMovieData();
        
        revalidate();
        repaint();
        pack();
    }

    private boolean hasUnsavedChanges() {
        return !searchField.getText().trim().isEmpty() ||
               currentMovie != null;
    }

    // Renderer personalizado para la lista
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