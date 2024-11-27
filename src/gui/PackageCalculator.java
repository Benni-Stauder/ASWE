package gui;

import javax.swing.*;
import java.awt.*;

public class PackageCalculator {

	public static final String AppName = "PackageCalculator";

	// Singleton instance
	private static PackageCalculator instance;


	public static PackageCalculator getInstance() {
		return instance;
	}

	// GUI components
	private JFrame primaryFrame;
	public ExplorerArea explorerArea;
	public CalculatorArea calculatorArea;
	public InspectorArea inspectorArea;
	public MessagesArea messagesArea;
	public StatusArea statusArea;
	public ToolbarArea toolbarArea;

	// Root path to the currently open project
	public String rootPath;

	public JFrame getPrimaryFrame() {
		return primaryFrame;
	}

	private PackageCalculator() {
		// Initialize GUI components
		explorerArea = new ExplorerArea();
		calculatorArea = new CalculatorArea();
		inspectorArea = new InspectorArea();
		messagesArea = new MessagesArea();
		statusArea = new StatusArea();
		toolbarArea = new ToolbarArea();
	}

	public void start() {
		// Initialize Singleton instance
		instance = this;

		// Create main frame
		primaryFrame = new JFrame(AppName);
		primaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Main panel with BorderLayout
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Add toolbar to the top
		mainPanel.add(toolbarArea, BorderLayout.NORTH);

		// Add a split pane for the main content area
		JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horizontalSplitPane.setLeftComponent(explorerArea);

		JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPane.setTopComponent(calculatorArea);
		verticalSplitPane.setBottomComponent(messagesArea);

		horizontalSplitPane.setRightComponent(verticalSplitPane);

		// Add split pane to the center
		mainPanel.add(horizontalSplitPane, BorderLayout.CENTER);

		// Add status area to the bottom
		mainPanel.add(statusArea, BorderLayout.SOUTH);

		// Set up and display the main frame
		primaryFrame.getContentPane().add(mainPanel);
		primaryFrame.setSize(1200, 800);
		primaryFrame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			PackageCalculator app = new PackageCalculator();
			app.start();
		});
	}
}