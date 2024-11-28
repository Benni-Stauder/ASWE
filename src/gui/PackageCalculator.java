package gui;

import javax.swing.*;
import java.awt.*;

import control.Calculator;
import data.Packet;
import control.ConfigHandler;

public class PackageCalculator {

	private static final String CONFIG_FILE = "config.properties";
	private JLabel resultLabel;
	private final ConfigHandler configHandler;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PackageCalculator().createAndShowGUI());
	}

	public PackageCalculator() {
		this.configHandler = new ConfigHandler();
	}

	private void createAndShowGUI() {
		JFrame frame = new JFrame("Package Cost Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setMinimumSize(new Dimension(500, 400)); // Set minimum size for the window
		frame.setLayout(new BorderLayout());

		// ToolBar with styled buttons
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBackground(new Color(60, 63, 65));

		JButton configButton = createStyledButton("Config");
		JPopupMenu configMenu = new JPopupMenu();
		JMenuItem loadConfigItem = new JMenuItem("Load Config");
		JMenuItem createConfigItem = new JMenuItem("Create Config");
		configMenu.add(loadConfigItem);
		configMenu.add(createConfigItem);
		configButton.addActionListener(e -> configMenu.show(configButton, 0, configButton.getHeight()));

		JButton infoButton = createStyledButton("Info");
		infoButton.addActionListener(e -> showPackageCosts());

		JButton aboutButton = createStyledButton("About");
		aboutButton.addActionListener(e -> showAboutDialog());

		// Exit Button
		JButton exitButton = createStyledButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));

		toolBar.add(configButton);
		toolBar.add(infoButton);
		toolBar.add(aboutButton);
		toolBar.add(Box.createHorizontalGlue()); // Push the exit button to the right
		toolBar.add(exitButton);
		frame.add(toolBar, BorderLayout.NORTH);

		// Input Panel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		inputPanel.setBackground(new Color(245, 245, 245));

		// Styled input fields
		JTextField lengthField = createStyledTextField("Length (mm):");
		JTextField widthField = createStyledTextField("Width (mm):");
		JTextField heightField = createStyledTextField("Height (mm):");
		JTextField weightField = createStyledTextField("Weight (g):");

		JButton calculateButton = new JButton("Calculate Price");
		calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		calculateButton.setBackground(new Color(30, 144, 255));
		calculateButton.setForeground(Color.WHITE);
		calculateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		calculateButton.setFocusPainted(false);
		calculateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		resultLabel = new JLabel(" ");
		resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		resultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		resultLabel.setForeground(new Color(34, 139, 34));

		// Adding components to the input panel
		inputPanel.add(lengthField);
		inputPanel.add(Box.createVerticalStrut(10));
		inputPanel.add(widthField);
		inputPanel.add(Box.createVerticalStrut(10));
		inputPanel.add(heightField);
		inputPanel.add(Box.createVerticalStrut(10));
		inputPanel.add(weightField);
		inputPanel.add(Box.createVerticalStrut(20));
		inputPanel.add(calculateButton);
		inputPanel.add(Box.createVerticalStrut(20));
		inputPanel.add(resultLabel);

		frame.add(inputPanel, BorderLayout.CENTER);

		// Action Listener for Calculate Button
		calculateButton.addActionListener(e -> {
			try {
				int length = Integer.parseInt(lengthField.getText());
				int width = Integer.parseInt(widthField.getText());
				int height = Integer.parseInt(heightField.getText());
				int weight = Integer.parseInt(weightField.getText());

				Packet packet = new Packet(length, width, height, weight);
				double price = Calculator.calcShippingCosts(packet);

				resultLabel.setText(String.format("Shipping Cost: %.2f €", price));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Please enter valid numerical values.", "Input Error", JOptionPane.ERROR_MESSAGE);
				resultLabel.setText(" ");
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(frame, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
				resultLabel.setText(" ");
			}
		});

		// Clear the result label when input fields are modified
		lengthField.addActionListener(e -> resultLabel.setText(" "));
		widthField.addActionListener(e -> resultLabel.setText(" "));
		heightField.addActionListener(e -> resultLabel.setText(" "));
		weightField.addActionListener(e -> resultLabel.setText(" "));

		// Add actions to Config Menu items
		loadConfigItem.addActionListener(e -> configHandler.openLoadConfigWindow());
		createConfigItem.addActionListener(e -> configHandler.openCreateConfigWindow());

		frame.setVisible(true);
	}

	private JTextField createStyledTextField(String labelText) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(245, 245, 245));

		JLabel label = new JLabel(labelText);
		label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label.setForeground(new Color(60, 63, 65));
		JTextField textField = new JTextField(15);
		textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		textField.setPreferredSize(new Dimension(200, 30));

		panel.add(label, BorderLayout.WEST);
		panel.add(textField, BorderLayout.CENTER);

		return textField;
	}

	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(new Color(30, 144, 255));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("SansSerif", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		return button;
	}

	private void showPackageCosts() {
		JOptionPane.showMessageDialog(null, "Package Costs:\n(Not yet implemented)", "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showAboutDialog() {
		JOptionPane.showMessageDialog(null, "Package Cost Calculator\n© 2024 Benni", "About", JOptionPane.INFORMATION_MESSAGE);
	}
}
