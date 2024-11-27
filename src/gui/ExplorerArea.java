package gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class ExplorerArea extends JPanel {
	private JTree fileTree;

	public ExplorerArea() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		fileTree = new JTree();
		JScrollPane scrollPane = new JScrollPane(fileTree);
		add(scrollPane);
	}

	public void loadNewTree(String rootPath) {
		File rootFile = new File(rootPath);
		DefaultMutableTreeNode rootNode = createNode(rootFile);
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		fileTree.setModel(treeModel);
	}

	private DefaultMutableTreeNode createNode(File file) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File child : files) {
					if (child.isDirectory() || child.getName().toLowerCase().endsWith(".txt")) {
						node.add(createNode(child));
					}
				}
			}
		}
		return node;
	}
}
