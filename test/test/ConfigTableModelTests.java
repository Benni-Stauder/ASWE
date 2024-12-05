package test;

import control.ConfigHandler;
import control.ConfigTableModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTableModelTests {
    private ConfigHandler configHandler;

    /**
     * Sets up the test environment before each test case execution.
     * Initializes the ConfigHandler instance with the default properties
     * file to be used across various test methods in the test class.
     */
    @BeforeEach
    public void setUp()    {
        configHandler = new ConfigHandler("default.properties");
    }

    /**
     * Tests adding a configuration entry to the {@link ConfigTableModel}.
     */
    @Test
    public void testGetValueAt() {
        ConfigTableModel model = new ConfigTableModel(configHandler.getConfigEntries());
        model.setValueAt(0,0,0);
        assertEquals(0,model.getValueAt(0,0));

        model.setValueAt(0,0,1);
        assertEquals(0,model.getValueAt(0,1));

        model.setValueAt(0,0,2);
        assertEquals(0,model.getValueAt(0,2));

        model.setValueAt(0,0,3);
        assertEquals(0,model.getValueAt(0,3));

        model.setValueAt(0.0,0,4);
        assertEquals(0.0,model.getValueAt(0,4));

        assertNull(model.getValueAt(0, 5));

        assertThrows(IllegalArgumentException.class, () -> model.setValueAt(0,0, 5));
    }

    @Test
    public void testIsCellEditable() {
        ConfigTableModel model = new ConfigTableModel(configHandler.getConfigEntries());
        assertTrue(model.isCellEditable(0,0));
        assertTrue(model.isCellEditable(0,1));
        assertTrue(model.isCellEditable(0,2));
        assertTrue(model.isCellEditable(0,3));
        assertTrue(model.isCellEditable(0,4));
        assertTrue(model.isCellEditable(0,5));
    }

    @Test
    public void testGetColumnClass() {
        ConfigTableModel model = new ConfigTableModel(configHandler.getConfigEntries());
        assertEquals(Integer.class, model.getColumnClass(0));
        assertEquals(Integer.class, model.getColumnClass(1));
        assertEquals(Integer.class, model.getColumnClass(2));
        assertEquals(Integer.class, model.getColumnClass(3));
        assertEquals(Double.class, model.getColumnClass(4));
        assertEquals(Object.class, model.getColumnClass(5));
    }
}
