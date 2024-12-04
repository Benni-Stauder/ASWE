package control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Handles configuration for package dimension and shipping cost entries.
 * Supports loading, saving, and editing configurations via a GUI.
 */
public class ConfigHandler {

    private static final String CONFIG_FILE = "config.properties";
    private final List<ConfigEntry> configEntries;
    private JFrame configFrame;

    /**
     * Constructor initializes the handler and loads configuration entries from the default file.
     */
    public ConfigHandler() {
        configEntries = new ArrayList<>();
        try {
            loadFile(new File(CONFIG_FILE));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens a window for creating and editing configuration entries.
     */
    public void openCreateConfigWindow() {
        if (configFrame != null && configFrame.isVisible()) {
            configFrame.toFront();
            return;
        }

        configFrame = new JFrame("Create Config");
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setSize(600, 400);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ConfigTableModel tableModel = new ConfigTableModel(configEntries);
        JTable configTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(configTable);

        JPanel buttonPanel = createButtonPanel(tableModel, configTable);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        configFrame.add(mainPanel);
        configFrame.setVisible(true);
    }

    /**
     * Creates a button panel with action buttons for the configuration GUI.
     *
     * @param tableModel The table model managing the configuration entries.
     * @param configTable The table displaying the configuration entries.
     * @return A JPanel containing the buttons.
     */
    private JPanel createButtonPanel(ConfigTableModel tableModel, JTable configTable) {
        JButton addButton = new JButton("Add Config");
        addButton.addActionListener(_ -> tableModel.addEntry(new ConfigEntry(0, 0, 0, 0, 0.0)));

        JButton saveButton = new JButton("Save Config");
        saveButton.addActionListener(_ -> saveConfigToFile(configTable));

        JButton applyButton = new JButton("Apply Config");
        applyButton.addActionListener(_ -> applyConfig(configTable));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> cancelConfig());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    /**
     * Cancels the current edits and reloads the configuration from the default file.
     */
    private void cancelConfig() {
        loadFile(new File(CONFIG_FILE));
        if (configFrame != null) {
            configFrame.setVisible(false);
        }
    }

    /**
     * Applies the current configuration to the default file.
     *
     * @param configTable The table displaying the configuration entries.
     */
    void applyConfig(JTable configTable) {
        try {
            validateAndSortConfig();
            Properties properties = extractPropertiesFromTable(configTable);
            savePropertiesToFile(properties, new File(CONFIG_FILE));
            JOptionPane.showMessageDialog(null, "Configuration applied successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error applying configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the configuration to a user-specified file.
     *
     * @param configTable The table displaying the configuration entries.
     */
    void saveConfigToFile(JTable configTable) {
        try {
            validateAndSortConfig();
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Properties properties = extractPropertiesFromTable(configTable);
                savePropertiesToFile(properties, file);
                JOptionPane.showMessageDialog(null, "Configuration saved successfully.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads the configuration from a user-specified file.
     */
    public void openLoadConfigWindow() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                loadFile(file);
                validateAndSortConfig();
                JOptionPane.showMessageDialog(null, "Configuration loaded successfully.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error loading configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Validates and sorts the configuration entries for consistency and logical ordering.
     */
    void validateAndSortConfig() {
        for (ConfigEntry entry : configEntries) {
            int[] dimensions = {entry.getLength(), entry.getWidth(), entry.getHeight()};
            Arrays.sort(dimensions);
            entry.setLength(dimensions[0]);
            entry.setWidth(dimensions[1]);
            entry.setHeight(dimensions[2]);
        }

        configEntries.sort(Comparator.comparingInt(ConfigEntry::getLength)
                .thenComparingInt(ConfigEntry::getWidth)
                .thenComparingInt(ConfigEntry::getHeight)
                .thenComparingInt(ConfigEntry::getWeight));
    }

    /**
     * Loads configuration entries from the specified file.
     *
     * @param file The file to load from.
     */
    void loadFile(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            configEntries.clear();
            int i = 0;
            while (properties.containsKey("entry." + i + ".dimensions")) {
                String[] dimensions = properties.getProperty("entry." + i + ".dimensions").split("x");
                double price = Double.parseDouble(properties.getProperty("entry." + i + ".price"));
                configEntries.add(new ConfigEntry(
                        Integer.parseInt(dimensions[0]),
                        Integer.parseInt(dimensions[1]),
                        Integer.parseInt(dimensions[2]),
                        Integer.parseInt(dimensions[3]),
                        price));
                i++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Extracts configuration properties from the table.
     *
     * @param configTable The table displaying the configuration entries.
     * @return A Properties object representing the configuration.
     */
    private Properties extractPropertiesFromTable(JTable configTable) {
        Properties properties = new Properties();
        ConfigTableModel model = (ConfigTableModel) configTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            properties.setProperty("entry." + i + ".dimensions",
                    model.getValueAt(i, 0) + "x" + model.getValueAt(i, 1) + "x" + model.getValueAt(i, 2) + "x" + model.getValueAt(i, 3));
            properties.setProperty("entry." + i + ".price", String.valueOf(model.getValueAt(i, 4)));
        }
        return properties;
    }

    /**
     * Saves properties to a specified file.
     *
     * @param properties The properties to save.
     * @param file       The file to save to.
     * @throws IOException If an error occurs during saving.
     */
    private void savePropertiesToFile(Properties properties, File file) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, "Package Configurations");
        }
    }

    /**
     * Returns all present config entries.
     *
     * @return List of all config entries.
     */
    public List<ConfigEntry> getConfigEntries() {
        return configEntries;
    }
}
