package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Diese Klasse repräsentiert den Bereich der GUI, in dem Info-Nachrichten angezeigt werden.
 * Sie ermöglicht das Hinzufügen, Abrufen und Löschen von Nachrichten.
 *
 * @author Benni
 * @version 1.0
 */
public class MessagesArea extends JPanel {

    private DefaultListModel<String> messageListModel;
    private JList<String> messageList;

    /**
     * Konstruktor zur Initialisierung der GUI-Komponenten für die MessagesArea.
     */
    public MessagesArea() {
        setLayout(new BorderLayout());

        // Initialisiere das Listenmodell und die Liste
        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Füge die Liste zu einem Scroll-Pane hinzu
        JScrollPane scrollPane = new JScrollPane(messageList);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Fügt eine Nachricht zur Liste der Nachrichten hinzu.
     *
     * @param message Die hinzuzufügende Nachricht. Wenn null oder leer, wird keine Nachricht hinzugefügt.
     */
    public void addMessage(String message) {
        if (message != null && !message.isEmpty()) {
            messageListModel.addElement(message);
        }
    }

    /**
     * Löscht alle Nachrichten aus der Liste.
     */
    public void clearMessages() {
        messageListModel.clear();
    }

    /**
     * Setzt eine einzelne Nachricht. Überschreibt alle bestehenden Nachrichten.
     *
     * @param text Die anzuzeigende Nachricht. Wenn null oder leer, wird die Liste geleert.
     */
    public void setMessage(String text) {
        clearMessages();
        if (text != null && !text.isEmpty()) {
            addMessage(text);
        }
    }

    /**
     * Gibt die aktuell angezeigte Nachricht zurück. Wenn mehrere Nachrichten vorhanden sind, wird die erste zurückgegeben.
     *
     * @return Die aktuell angezeigte Nachricht oder ein leerer String, wenn keine Nachricht vorhanden ist.
     */
    public String getMessage() {
        return messageListModel.isEmpty() ? "" : messageListModel.getElementAt(0);
    }

    /**
     * Löscht die aktuell angezeigte Nachricht (die erste in der Liste).
     */
    public void clearMessage() {
        if (!messageListModel.isEmpty()) {
            messageListModel.remove(0);
        }
    }
}