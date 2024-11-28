package gui;

import control.ProjectHandling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarArea extends JPanel {

	public ToolbarArea() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		// Initialize buttons
		JButton openProjectButton = new JButton("Open Project");
		JButton newFileButton = new JButton("New File");
		JButton saveFileButton = new JButton("Save File");
		JButton saveFileAsButton = new JButton("Save File As");
		JButton settingsButton = new JButton("Settings");
		JButton aboutButton = new JButton("About");
		JButton infoButton = new JButton("Info");

		// Add action listeners
		openProjectButton.addActionListener(e -> ProjectHandling.openProject());
		newFileButton.addActionListener(e -> ProjectHandling.newFile());
		infoButton.addActionListener(e -> showInfoDialog());

		// Add buttons to the toolbar
		add(openProjectButton);
		add(newFileButton);
		add(saveFileButton);
		add(saveFileAsButton);
		add(new JSeparator(SwingConstants.VERTICAL));
		add(settingsButton);
		add(aboutButton);
		add(infoButton);
	}

	private void showInfoDialog() {
		JDialog dialog = new JDialog((Frame) null, "Info", true);
		dialog.setLayout(new BorderLayout());
		JLabel infoLabel = new JLabel("Package Calculator v0.3 \n\n (c) 2024 Benni Stauder", SwingConstants.CENTER);
		infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dialog.add(infoLabel, BorderLayout.CENTER);
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		dialog.add(closeButton, BorderLayout.SOUTH);
		dialog.setSize(400, 250);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
}
