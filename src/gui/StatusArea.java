package gui;

import javax.swing.*;
import java.awt.*;

public class StatusArea extends JPanel {

    private JLabel statusLabel;

    public StatusArea() {
        setLayout(new BorderLayout());
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusLabel, BorderLayout.WEST);
    }

    public void setStatus(String status) {
        statusLabel.setText("Status: " + status);
    }
}
