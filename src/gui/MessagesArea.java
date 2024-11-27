package gui;

import javax.swing.*;
import java.awt.*;

public class MessagesArea extends JPanel {

    private DefaultListModel<String> messageListModel;
    private JList<String> messageList;

    public MessagesArea() {
        setLayout(new BorderLayout());

        // Initialize the list model and list
        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(messageList);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessage(String message) {
        messageListModel.addElement(message);
    }

    public void clearMessages() {
        messageListModel.clear();
    }
}