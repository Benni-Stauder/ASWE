package control;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigHandler {

    private static final String CONFIG_FILE = "config.properties";
    private final List<ConfigEntry> configEntries;
    private JFrame configFrame;

    public ConfigHandler() {
        configEntries = new ArrayList<>();
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        configFrame.add(mainPanel);
        configFrame.setVisible(true);
    }

    public void openLoadConfigWindow() {
        loadConfigFromFile();
    }

    private void saveConfigToFile(JTable configTable) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (OutputStream outputStream = new FileOutputStream(file)) {
                Properties properties = new Properties();
                ConfigTableModel model = (ConfigTableModel) configTable.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    properties.setProperty("entry." + i + ".dimensions",
                            model.getValueAt(i, 0) + "x" + model.getValueAt(i, 1) + "x" + model.getValueAt(i, 2) + "x" + model.getValueAt(i, 3));
                    properties.setProperty("entry." + i + ".price", String.valueOf(model.getValueAt(i, 4)));
                }
                properties.store(outputStream, "Package Configurations");
                JOptionPane.showMessageDialog(null, "Config saved successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadConfigFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
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
                JOptionPane.showMessageDialog(null, "Config loaded successfully.");
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error loading config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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

    private static class ConfigEntry {
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