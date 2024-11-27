package gui;

import javax.swing.*;
import java.awt.*;

public class InspectorArea extends JPanel {

    public InspectorArea() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add placeholder content for InspectorArea
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Inspector Placeholder"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JTextField(15), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JButton("Inspect"), gbc);
    }
}