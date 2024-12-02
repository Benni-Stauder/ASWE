package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import control.Calculator;
import data.Packet;
import control.ConfigHandler;

public class PackageCalculator {

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
		frame.setSize(600, 450);
		frame.setMinimumSize(new Dimension(600, 450));
		frame.setLayout(new BorderLayout());

		// Add components to frame
		JToolBar toolBar = createToolBar();
		frame.add(toolBar, BorderLayout.NORTH);

		JPanel inputPanel = createInputPanel();
		frame.add(inputPanel, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = initializeToolBar();

		JButton configButton = createToolbarButton("Config", "settings-icon.png", this::showConfigMenu);
		JButton infoButton = createToolbarButton("Info", "info-icon.png", this::showPackageCosts);
		JButton aboutButton = createToolbarButton("About", "about-icon.png", this::showAboutDialog);
		JButton exitButton = createToolbarButton("Exit", "exit-icon.png", _ -> System.exit(0));

		toolBar.add(configButton);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(infoButton);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(aboutButton);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(exitButton);

		return toolBar;
	}

	private JToolBar initializeToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBackground(new Color(60, 63, 65));
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
		return toolBar;
	}

	private JButton createToolbarButton(String text, String iconPath, ActionListener action) {
		JButton button = new JButton(text, loadIcon(iconPath));
		button.setFont(new Font("SansSerif", Font.BOLD, 14));
		button.setBackground(new Color(30, 144, 255));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		button.addActionListener(action);
		return button;
	}

	private ImageIcon loadIcon(String iconPath) {
		try {
			return new ImageIcon(iconPath);
		} catch (Exception e) {
			System.err.println("Icon not found: " + iconPath);
			return null;
		}
	}

	private void showConfigMenu(ActionEvent e) {
		JPopupMenu configMenu = new JPopupMenu();
		JMenuItem loadConfigItem = new JMenuItem("Load Config");
		JMenuItem createConfigItem = new JMenuItem("Edit Config");

		loadConfigItem.addActionListener(_ -> configHandler.openLoadConfigWindow());
		createConfigItem.addActionListener(_ -> configHandler.openCreateConfigWindow());

		configMenu.add(loadConfigItem);
		configMenu.add(createConfigItem);

		JButton sourceButton = (JButton) e.getSource();
		configMenu.show(sourceButton, 0, sourceButton.getHeight());
	}

	public void showPackageCosts(ActionEvent e) {
		List<ConfigHandler.ConfigEntry> configEntries = configHandler.getConfigEntries();
		if (configEntries.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No package costs configured.", "Package Costs", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		StringBuilder costsInfo = new StringBuilder("Package Costs:\n");
        for (ConfigHandler.ConfigEntry entry : configEntries) {
            costsInfo.append(String.format(
                    "Dimensions %dx%dx%dx%d mm, Price: €%.2f\n",
                    entry.getLength(), entry.getWidth(), entry.getHeight(), entry.getWeight(), entry.getPrice()
            ));
        }

		JOptionPane.showMessageDialog(null, costsInfo.toString(), "Package Costs", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showAboutDialog(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "Package Cost Calculator\n© 2024 Benni", "About", JOptionPane.INFORMATION_MESSAGE);
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		inputPanel.setBackground(new Color(245, 245, 245));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JTextField lengthField = addLabeledTextField(inputPanel, gbc, 0, "Length (mm):");
		JTextField widthField = addLabeledTextField(inputPanel, gbc, 1, "Width (mm):");
		JTextField heightField = addLabeledTextField(inputPanel, gbc, 2, "Height (mm):");
		JTextField weightField = addLabeledTextField(inputPanel, gbc, 3, "Weight (g):");

		JButton calculateButton = createCalculateButton(lengthField, widthField, heightField, weightField);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		inputPanel.add(calculateButton, gbc);

		resultLabel = new JLabel(" ");
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		resultLabel.setForeground(new Color(34, 139, 34));
		gbc.gridy = 5;
		inputPanel.add(resultLabel, gbc);

		return inputPanel;
	}

	private JTextField addLabeledTextField(JPanel panel, GridBagConstraints gbc, int row, String labelText) {
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(new JLabel(labelText), gbc);

		gbc.gridx = 1;
		JTextField textField = new JTextField(15);
		panel.add(textField, gbc);

		return textField;
	}

	private JButton createCalculateButton(JTextField lengthField, JTextField widthField, JTextField heightField, JTextField weightField) {
		JButton calculateButton = new JButton("Calculate Price");
		calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		calculateButton.setBackground(new Color(30, 144, 255));
		calculateButton.setForeground(Color.WHITE);
		calculateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		calculateButton.setFocusPainted(false);
		calculateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		calculateButton.addActionListener(_ -> {
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
		});

		return calculateButton;
	}
}
