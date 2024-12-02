package control;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigHandler {

    private static final String CONFIG_FILE = "config.properties";

    public List<ConfigEntry> getConfigEntries() {
        return configEntries;
    }

    private static List<ConfigEntry> configEntries;
    private JFrame configFrame;

    public ConfigHandler() {
        configEntries = new ArrayList<>();
        try {
            File configFile = new File(CONFIG_FILE);
            loadFile(configFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(configFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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

        JButton addButton = new JButton("Add Config");
        addButton.addActionListener(e -> tableModel.addEntry(new ConfigEntry(0, 0, 0, 0, 0.0)));

        JButton saveButton = new JButton("Save Config");
        saveButton.addActionListener(e -> saveConfigToFile(configTable));

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyConfig(configTable));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> configFrame.setVisible(false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        configFrame.add(mainPanel);
        configFrame.setVisible(true);
    }

    public void openLoadConfigWindow() {
        loadConfigFromFile();
    }

    private void applyConfig(JTable configTable) {
        validateAndSortConfig();
        try (OutputStream outputStream = new FileOutputStream(CONFIG_FILE);) {
            Properties properties = new Properties();
            ConfigTableModel model = (ConfigTableModel) configTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                properties.setProperty("entry." + i + ".dimensions",
                        model.getValueAt(i, 0) + "x" + model.getValueAt(i, 1) + "x" + model.getValueAt(i, 2) + "x" + model.getValueAt(i, 3));
                properties.setProperty("entry." + i + ".price", String.valueOf(model.getValueAt(i, 4)));
            }
            properties.store(outputStream, "Config");
            JOptionPane.showMessageDialog(null, "Config applied successfully and saved to persistent storage.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error applying config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void saveConfigToFile(JTable configTable) {
        validateAndSortConfig();
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (OutputStream outputStream = new FileOutputStream(file)) {
                Properties properties = getProperties(configTable);
                properties.store(outputStream, "Package Configurations");
                JOptionPane.showMessageDialog(null, "Config saved successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static Properties getProperties(JTable configTable) {
        Properties properties = new Properties();
        ConfigTableModel model = (ConfigTableModel) configTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            properties.setProperty("entry." + i + ".dimensions",
                    model.getValueAt(i, 0) + "x" + model.getValueAt(i, 1) + "x" + model.getValueAt(i, 2) + "x" + model.getValueAt(i, 3));
            properties.setProperty("entry." + i + ".price", String.valueOf(model.getValueAt(i, 4)));
        }
        return properties;
    }

    private void loadConfigFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try{
                loadFile(file);
                JOptionPane.showMessageDialog(null, "Config loaded successfully.");
            } catch(Exception _){}
        }
    }

    private void validateAndSortConfig() {
        // Normalize each entry's dimensions
        for (ConfigEntry entry : configEntries) {
            int[] dimensions = {entry.getLength(), entry.getWidth(), entry.getHeight()};
            Arrays.sort(dimensions); // Sort dimensions so smallest is first, largest is last
            entry.setLength(dimensions[0]);
            entry.setWidth(dimensions[1]);
            entry.setHeight(dimensions[2]);
        }

        // Sort the configuration entries
        configEntries.sort((entry1, entry2) -> {
            if (entry1.getLength() != entry2.getLength()) {
                return Integer.compare(entry1.getLength(), entry2.getLength());
            } else if (entry1.getWidth() != entry2.getWidth()) {
                return Integer.compare(entry1.getWidth(), entry2.getWidth());
            } else if (entry1.getHeight() != entry2.getHeight()) {
                return Integer.compare(entry1.getHeight(), entry2.getHeight());
            } else {
                return Integer.compare(entry1.getWeight(), entry2.getWeight());
            }
        });

        // Validate the configuration for conflicting or mismatched dimensions
        for (int i = 0; i < configEntries.size() - 1; i++) {
            ConfigEntry current = configEntries.get(i);
            ConfigEntry next = configEntries.get(i + 1);

            // Check for conflicting constraints (larger dimensions with lower weight limit)
            if (current.getLength() <= next.getLength() &&
                    current.getWidth() <= next.getWidth() &&
                    current.getHeight() <= next.getHeight() &&
                    current.getWeight() > next.getWeight()) {
                JOptionPane.showMessageDialog(null,
                        String.format("Invalid configuration found between entries:\n" +
                                        "Entry %d: %dx%dx%dx%d\n" +
                                        "Entry %d: %dx%dx%dx%d\n" +
                                        "The larger dimensions have a lower weight limit.",
                                i + 1, current.getLength(), current.getWidth(), current.getHeight(), current.getWeight(),
                                i + 2, next.getLength(), next.getWidth(), next.getHeight(), next.getWeight()),
                        "Validation Warning", JOptionPane.WARNING_MESSAGE);
            }

            // Check for mismatched dimensions
            if ((current.getLength() < next.getLength() &&
                    (current.getWidth() > next.getWidth() || current.getHeight() > next.getHeight())) ||
                    (current.getWidth() < next.getWidth() &&
                            (current.getLength() > next.getLength() || current.getHeight() > next.getHeight())) ||
                    (current.getHeight() < next.getHeight() &&
                            (current.getLength() > next.getLength() || current.getWidth() > next.getWidth()))) {
                JOptionPane.showMessageDialog(null,
                        String.format("Mismatched dimensions between entries:\n" +
                                        "Entry %d: %dx%dx%d\n" +
                                        "Entry %d: %dx%dx%d\n" +
                                        "Smaller dimensions cannot have larger counterparts in other fields.",
                                i + 1, current.getLength(), current.getWidth(), current.getHeight(),
                                i + 2, next.getLength(), next.getWidth(), next.getHeight()),
                        "Validation Warning", JOptionPane.WARNING_MESSAGE);
            }
        }

        // If validation passes, confirm the configuration is valid
        JOptionPane.showMessageDialog(null, "Configuration is valid and sorted.", "Validation Complete", JOptionPane.INFORMATION_MESSAGE);
    }


    private void loadFile(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            configEntries.clear();
            int i = 0;
            while (properties.containsKey("entry." + i + ".dimensions")) {
                String dimensions = properties.getProperty("entry." + i + ".dimensions");
                String[] parts = dimensions.split("x");
                double price = Double.parseDouble(properties.getProperty("entry." + i + ".price"));

                configEntries.add(new ConfigEntry(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]),
                        price
                ));
                i++;
            }
            OutputStream outputStream = new FileOutputStream(CONFIG_FILE);
            properties.store(outputStream, "Persistent Package Configurations");
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ConfigTableModel extends AbstractTableModel {

        private final List<ConfigEntry> entries;
        private final String[] columnNames = {"Length (mm)", "Width (mm)", "Height (mm)", "Weight (g)", "Price (€)"};

        public ConfigTableModel(List<ConfigEntry> entries) {
            this.entries = entries;
        }

        @Override
        public int getRowCount() {
            return entries.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ConfigEntry entry = entries.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> entry.getLength();
                case 1 -> entry.getWidth();
                case 2 -> entry.getHeight();
                case 3 -> entry.getWeight();
                case 4 -> entry.getPrice();
                default -> null;
            };
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ConfigEntry entry = entries.get(rowIndex);
            switch (columnIndex) {
                case 0: entry.setLength((Integer) aValue); break;
                case 1: entry.setWidth((Integer) aValue); break;
                case 2: entry.setHeight((Integer) aValue); break;
                case 3: entry.setWeight((Integer) aValue); break;
                case 4: entry.setPrice((Double) aValue); break;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0, 1, 2, 3 -> Integer.class;
                case 4 -> Double.class;
                default -> Object.class;
            };
        }

        public void addEntry(ConfigEntry entry) {
            entries.add(entry);
            fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
        }
    }

    public static class ConfigEntry {
        private int length;
        private int width;
        private int height;
        private int weight;
        private double price;

        public ConfigEntry(int length, int width, int height, int weight, double price) {
            this.length = length;
            this.width = width;
            this.height = height;
            this.weight = weight;
            this.price = price;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}