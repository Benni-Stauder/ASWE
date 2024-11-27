package control;

import gui.PackageCalculator;

import javax.swing.*;
import java.io.File;

public class ProjectHandling {

	public static void openProject(String rootPath) {
		// Update window title
		JFrame primaryFrame = PackageCalculator.getInstance().getPrimaryFrame();
		primaryFrame.setTitle(PackageCalculator.AppName + " – " + rootPath);

		// Load tree in explorer
		PackageCalculator.getInstance().explorerArea.loadNewTree(rootPath);

		// Remember open project
		PackageCalculator.getInstance().rootPath = rootPath;
	}

	public static void openProject() {
		JFileChooser directoryChooser = new JFileChooser();
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnValue = directoryChooser.showOpenDialog(PackageCalculator.getInstance().getPrimaryFrame());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File projectDirectory = directoryChooser.getSelectedFile();
			if (projectDirectory != null) {
				openProject(projectDirectory.getAbsolutePath());
			}
		}
	}

	public static void newFile() {
		// Placeholder for new file functionality
		JOptionPane.showMessageDialog(
				PackageCalculator.getInstance().getPrimaryFrame(),
				"New file functionality not implemented yet.",
				"Information",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
}