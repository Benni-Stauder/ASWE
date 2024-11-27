package gui;

import control.Calculator;
import data.Packet;

import javax.swing.*;
import java.awt.*;

public class CalculatorArea extends JPanel {

	// Input fields
	private JTextField lengthTextField = new JTextField(10);
	private JTextField widthTextField = new JTextField(10);
	private JTextField heightTextField = new JTextField(10);
	private JTextField weightTextField = new JTextField(10);

	// Output label
	private JLabel shippingCostLabel = new JLabel("?");

	// Button
	private JButton calcButton = new JButton("Calculate");

	public CalculatorArea() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Add description labels and input fields
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("Length:"), gbc);

		gbc.gridx = 1;
		add(lengthTextField, gbc);

		gbc.gridx = 2;
		add(new JLabel("mm"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("Width:"), gbc);

		gbc.gridx = 1;
		add(widthTextField, gbc);

		gbc.gridx = 2;
		add(new JLabel("mm"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("Height:"), gbc);

		gbc.gridx = 1;
		add(heightTextField, gbc);

		gbc.gridx = 2;
		add(new JLabel("mm"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("Weight:"), gbc);

		gbc.gridx = 1;
		add(weightTextField, gbc);

		gbc.gridx = 2;
		add(new JLabel("g"), gbc);

		// Add shipping cost calculation line
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new JLabel("Shipping Costs:"), gbc);

		gbc.gridx = 1;
		add(shippingCostLabel, gbc);

		gbc.gridx = 2;
		add(calcButton, gbc);

		// Add action listener to calculate button
		calcButton.addActionListener(e -> calcShippingCosts());
	}

	private void calcShippingCosts() {
		try {
			// Initialize calculator
			Calculator calc = new Calculator();

			// Get user input values
			int length = Integer.parseInt(lengthTextField.getText());
			int width = Integer.parseInt(widthTextField.getText());
			int height = Integer.parseInt(heightTextField.getText());
			int weight = Integer.parseInt(weightTextField.getText());

			// Perform calculation
			Packet packet = new Packet(length, width, height, weight);
			Double costs = calc.calcShippingCosts(packet);

			// Show result
			shippingCostLabel.setText(costs.toString());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
