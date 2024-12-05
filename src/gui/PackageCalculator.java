package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import control.Calculator;
import control.ConfigEntry;
import control.ConfigHandler;
import data.Packet;

/**
 * PackageCalculator is a GUI-based application to calculate package shipping costs.
 * It provides functionalities for managing configurations, displaying package cost information,
 * and calculating shipping costs based on user input.
 */
public class PackageCalculator {

	private JLabel resultLabel;
	private final ConfigHandler configHandler;

	/**
	 * Main entry point of the application.
	 *
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PackageCalculator().createAndShowGUI());
	}

	/**
	 * Constructs a new PackageCalculator instance.
	 */
	public PackageCalculator() {
		this.configHandler = new ConfigHandler("config.properties");
	}

	/**
	 * Initializes and displays the main GUI of the application.
	 */
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Package Cost Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setLayout(new BorderLayout());
		frame.setMinimumSize(new Dimension(800, 600));

		// Set custom application icon
		setApplicationIcon(frame);

		frame.add(createToolBar(), BorderLayout.NORTH);
		frame.add(createInputPanel(), BorderLayout.CENTER);

		frame.setVisible(true);
	}

	/**
	 * Sets a custom icon for the application window.
	 *
	 * @param frame The JFrame instance
	 */
	private void setApplicationIcon(JFrame frame) {
		try {
			Image icon = Toolkit.getDefaultToolkit().getImage("src/gui/pictures/packet.png");
			frame.setIconImage(icon);
		} catch (Exception e) {
			System.err.println("Error setting application icon: " + e.getMessage());
		}
	}

	/**
	 * Creates the top toolbar with buttons for various actions.
	 *
	 * @return A JToolBar component
	 */
	private JToolBar createToolBar() {
		JToolBar toolBar = initializeToolBar();

		toolBar.add(createToolbarButton("Config", "src/gui/pictures/config.png", this::showConfigMenu));
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(createToolbarButton("Info", "src/gui/pictures/info.png", e -> showPackageCosts()));
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(createToolbarButton("About", "src/gui/pictures/about.png", e -> showAboutDialog()));
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(createToolbarButton("Exit", "src/gui/pictures/exit.png", e -> System.exit(0)));

		return toolBar;
	}

	/**
	 * Initializes the toolbar with default properties.
	 *
	 * @return A configured JToolBar
	 */
	private JToolBar initializeToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBackground(new Color(60, 63, 65));
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
		return toolBar;
	}

	/**
	 * Creates a toolbar button with specified properties.
	 *
	 * @param text     Button text
	 * @param iconPath Path to the icon
	 * @param action   ActionListener for the button
	 * @return A JButton instance
	 */
	private JButton createToolbarButton(String text, String iconPath, ActionListener action) {
		JButton button = new JButton(text, loadAndScaleIcon(iconPath));
		button.setFont(new Font("SansSerif", Font.BOLD, 14));
		button.setBackground(new Color(30, 144, 255));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		button.addActionListener(action);
		return button;
	}

	/**
	 * Loads and scales an image icon.
	 *
	 * @param path Path to the image
	 * @return A scaled ImageIcon
	 */
	private ImageIcon loadAndScaleIcon(String path) {
		ImageIcon icon = new ImageIcon(path);
		Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}

	/**
	 * Displays the configuration menu.
	 *
	 * @param e The action event
	 */
	private void showConfigMenu(ActionEvent e) {
		JPopupMenu configMenu = new JPopupMenu();
		JMenuItem loadConfigItem = new JMenuItem("Load Config");
		JMenuItem createConfigItem = new JMenuItem("Edit Config");

		loadConfigItem.addActionListener(event -> configHandler.openLoadConfigWindow());
		createConfigItem.addActionListener(event -> configHandler.openCreateConfigWindow());

		configMenu.add(loadConfigItem);
		configMenu.add(createConfigItem);

		JButton sourceButton = (JButton) e.getSource();
		configMenu.show(sourceButton, 0, sourceButton.getHeight());
	}

	/**
	 * Displays configured package costs in a dialog.
	 */
	public void showPackageCosts() {
		List<ConfigEntry> configEntries = configHandler.getConfigEntries();

		if (configEntries.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No package costs configured.", "Package Costs", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		StringBuilder costsInfo = new StringBuilder("Package Costs:\n");
		for (ConfigEntry entry : configEntries) {
			costsInfo.append(String.format(
					"Dimensions %dx%dx%d mm, Weight: %d g, Price: €%.2f\n",
					entry.getLength(), entry.getWidth(), entry.getHeight(), entry.getWeight(), entry.getPrice()
			));
		}

		JOptionPane.showMessageDialog(null, costsInfo.toString(), "Package Costs", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays the "About" dialog.
	 */
	private void showAboutDialog() {
		JOptionPane.showMessageDialog(null, "Package Cost Calculator\n© 2024 Benni", "About", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Creates the input panel for package dimensions and weight.
	 *
	 * @return A JPanel component
	 */
	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		inputPanel.setBackground(new Color(245, 245, 245));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JTextField lengthField = addLabeledTextField(inputPanel, gbc, 0, "Length (mm):");
		JTextField widthField = addLabeledTextField(inputPanel, gbc, 1, "Width (mm):");
		JTextField heightField = addLabeledTextField(inputPanel, gbc, 2, "Height (mm):");
		JTextField weightField = addLabeledTextField(inputPanel, gbc, 3, "Weight (g):");

		gbc.gridy = 4;
		gbc.gridwidth = 2;
		inputPanel.add(createCalculateButton(lengthField, widthField, heightField, weightField), gbc);

		resultLabel = new JLabel(" ");
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		resultLabel.setForeground(new Color(34, 139, 34));
		gbc.gridy = 5;
		inputPanel.add(resultLabel, gbc);

		return inputPanel;
	}

	/**
	 * Adds a labeled text field to the specified panel.
	 *
	 * @param panel     The target panel
	 * @param gbc       GridBagConstraints for layout
	 * @param row       Row index
	 * @param labelText Label text
	 * @return A JTextField instance
	 */
	private JTextField addLabeledTextField(JPanel panel, GridBagConstraints gbc, int row, String labelText) {
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(new JLabel(labelText), gbc);

		gbc.gridx = 1;
		JTextField textField = new JTextField(15);
		panel.add(textField, gbc);

		return textField;
	}

	/**
	 * Creates the "Calculate Price" button and attaches its action listener.
	 *
	 * @param lengthField Input for length
	 * @param widthField  Input for width
	 * @param heightField Input for height
	 * @param weightField Input for weight
	 * @return A JButton instance
	 */
	private JButton createCalculateButton(JTextField lengthField, JTextField widthField, JTextField heightField, JTextField weightField) {
		JButton calculateButton = new JButton("Calculate Price");
		calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		calculateButton.setBackground(new Color(30, 144, 255));
		calculateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		calculateButton.setForeground(Color.WHITE);
		calculateButton.setFocusPainted(false);
		calculateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		calculateButton.addActionListener(e -> calculateShippingCost(lengthField, widthField, heightField, weightField));

		return calculateButton;
	}

	/**
	 * Parses input fields, calculates the shipping cost, and updates the result label.
	 *
	 * @param lengthField Input for length
	 * @param widthField  Input for width
	 * @param heightField Input for height
	 * @param weightField Input for weight
	 */
	private void calculateShippingCost(JTextField lengthField, JTextField widthField, JTextField heightField, JTextField weightField) {
		try {
			int length = Integer.parseInt(lengthField.getText());
			int width = Integer.parseInt(widthField.getText());
			int height = Integer.parseInt(heightField.getText());
			int weight = Integer.parseInt(weightField.getText());

			Packet packet = new Packet(length, width, height, weight);
			double price = Calculator.calcShippingCosts(packet);

			resultLabel.setText(String.format("Shipping Cost: %.2f €", price));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Please enter valid numerical values.", "Input Error", JOptionPane.ERROR_MESSAGE);
			resultLabel.setText(" ");
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
			resultLabel.setText(" ");
		}
	}
}
