package control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigHandler {

    private static final String CONFIG_FILE = "config.properties";
    private List<ConfigEntryPanel> configEntries;
    private JFrame configFrame;
    private JPanel entryContainer;

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
        configFrame.setSize(600, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        entryContainer = new JPanel();
        entryContainer.setLayout(new BoxLayout(entryContainer, BoxLayout.Y_AXIS));
        entryContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(entryContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add initial entry
        addConfigEntry();

        JButton addEntryButton = new JButton("Add Entry");
        addEntryButton.addActionListener(e -> addConfigEntry());

        JButton saveButton = new JButton("Save Config");
        saveButton.addActionListener(e -> saveConfigToFile());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addEntryButton);
        buttonPanel.add(saveButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        configFrame.add(mainPanel);
        configFrame.setVisible(true);
    }

    public void openLoadConfigWindow() {
        loadConfigFromFile();
    }

    private void addConfigEntry() {
        ConfigEntryPanel entryPanel = new ConfigEntryPanel();
        configEntries.add(entryPanel);
        entryContainer.add(entryPanel);
        entryContainer.revalidate();
        entryContainer.repaint();
    }

    private void saveConfigToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (OutputStream outputStream = new FileOutputStream(file)) {
                Properties properties = new Properties();
                for (int i = 0; i < configEntries.size(); i++) {
                    ConfigEntryPanel entry = configEntries.get(i);
                    properties.setProperty("entry." + i + ".dimensions",
                            entry.getLength() + "x" + entry.getWidth() + "x" + entry.getHeight() + "x" + entry.getWeight());
                    properties.setProperty("entry." + i + ".price", String.valueOf(entry.getPrice()));
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
                entryContainer.removeAll(); // Clear the previous entries
                int i = 0;
                while (properties.containsKey("entry." + i + ".dimensions")) {
                    String dimensions = properties.getProperty("entry." + i + ".dimensions");
                    String[] parts = dimensions.split("x");
                    double price = Double.parseDouble(properties.getProperty("entry." + i + ".price"));

                    ConfigEntryPanel entry = new ConfigEntryPanel(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3]),
                            price
                    );
                    configEntries.add(entry);
                    entryContainer.add(entry);
                    i++;
                }
                entryContainer.revalidate();
                entryContainer.repaint();
                JOptionPane.showMessageDialog(null, "Config loaded successfully.");
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error loading config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class ConfigEntryPanel extends JPanel {
        private final JTextField lengthField;
        private final JTextField widthField;
        private final JTextField heightField;
        private final JTextField weightField;
        private final JTextField priceField;

        public ConfigEntryPanel() {
            this(0, 0, 0, 0, 0.0);
        }

        public ConfigEntryPanel(int length, int width, int height, int weight, double price) {
            setLayout(new GridLayout(2, 5, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            lengthField = createField(String.valueOf(length));
            widthField = createField(String.valueOf(width));
            heightField = createField(String.valueOf(height));
            weightField = createField(String.valueOf(weight));
            priceField = createField(String.format("%.2f", price));

            add(new JLabel("Length (mm):"));
            add(lengthField);
            add(new JLabel("Width (mm):"));
            add(widthField);
            add(new JLabel("Height (mm):"));
            add(heightField);
            add(new JLabel("Weight (g):"));
            add(weightField);
            add(new JLabel("Price (€):"));
            add(priceField);
        }

        private JTextField createField(String text) {
            JTextField field = new JTextField(text);
            field.setPreferredSize(new Dimension(80, 25));
            return field;
        }

        public int getLength() {
            return Integer.parseInt(lengthField.getText());
        }

        public int getWidth() {
            return Integer.parseInt(widthField.getText());
        }

        public int getHeight() {
            return Integer.parseInt(heightField.getText());
        }

        public int getWeight() {
            return Integer.parseInt(weightField.getText());
        }

        public double getPrice() {
            return Double.parseDouble(priceField.getText());
        }
    }
}