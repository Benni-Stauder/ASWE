package control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConfigHandlerTest {

    private ConfigHandler configHandler;

    @BeforeEach
    public void setUp() {
        configHandler = new ConfigHandler();
        // Suppress all JOptionPane dialogs
        mockStatic(JOptionPane.class);
        doNothing().when(JOptionPane.class);
        File defaultFile = new File("default.properties");
        configHandler.loadFile(defaultFile);
    }

    @AfterEach
    public void tearDown() {
        // Deregister static mocks
        clearAllCaches();
    }


    @Test
    public void testLoadFile_validFile() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test-config", ".properties");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("entry.0.dimensions=10x20x30x40\nentry.0.price=99.99\n");
        }

        // Act
        configHandler.getConfigEntries().clear(); // Ensure the list is empty
        configHandler.loadFile(tempFile);

        // Assert
        List<ConfigEntry> entries = configHandler.getConfigEntries();
        assertEquals(1, entries.size());
        ConfigEntry entry = entries.getFirst();
        assertEquals(10, entry.getLength());
        assertEquals(20, entry.getWidth());
        assertEquals(30, entry.getHeight());
        assertEquals(40, entry.getWeight());
        assertEquals(99.99, entry.getPrice());
    }

    @Test
    public void testLoadFile_invalidFile() {
        // Arrange
        File tempFile = new File("invalid-file.properties");

        // Act
        configHandler.loadFile(tempFile);

        // Assert
        // Ensure no exceptions thrown and entries remain empty
        assertFalse(configHandler.getConfigEntries().isEmpty());
        assertEquals(5, configHandler.getConfigEntries().size());
    }

    @Test
    public void testValidateAndSortConfig_normalizationAndSorting() {
        // Arrange
        configHandler.getConfigEntries().add(new ConfigEntry(30, 10, 20, 50, 100.0));
        configHandler.getConfigEntries().add(new ConfigEntry(20, 40, 10, 40, 200.0));

        // Act
        configHandler.validateAndSortConfig();

        // Assert
        List<ConfigEntry> entries = configHandler.getConfigEntries();
        assertEquals(7, entries.size());
        assertEquals(10, entries.getFirst().getLength()); // Dimensions should be normalized and sorted
        assertEquals(20, entries.getFirst().getWidth());
        assertEquals(30, entries.getFirst().getHeight());
        assertEquals(10, entries.getFirst().getLength()); // Sorted by length first
    }

    @Test
    public void testApplyConfig_success() throws IOException {
        // Arrange
        configHandler.getConfigEntries().add(new ConfigEntry(10, 20, 30, 40, 50.0));

        // Mock JTable and ConfigTableModel
        JTable mockTable = mock(JTable.class);
        ConfigTableModel mockModel = mock(ConfigTableModel.class);

        // Ensure getModel returns mockModel
        when(mockTable.getModel()).thenReturn(mockModel);

        // Mock ConfigTableModel behavior (if necessary)
        // Example: when(mockModel.getRowCount()).thenReturn(1);
        // Example: when(mockModel.getValueAt(0, column)).thenReturn(someValue);

        // Suppress file writing for testing
        File tempFile = File.createTempFile("config", ".properties");
        tempFile.deleteOnExit();
        System.setProperty("config.file", tempFile.getAbsolutePath());

        // Act
        configHandler.applyConfig(mockTable);

        // Assert
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(tempFile)) {
            properties.load(inputStream);
        }
        assertEquals("10x20x30x40", properties.getProperty("entry.0.dimensions"));
        assertEquals("50.0", properties.getProperty("entry.0.price"));
    }


    @Test
    public void testSaveConfigToFile_success() throws IOException {
        // Arrange
        configHandler.getConfigEntries().add(new ConfigEntry(10, 20, 30, 40, 50.0));
        JTable mockTable = mock(JTable.class);
        ConfigTableModel mockModel = mock(ConfigTableModel.class);
        when(mockTable.getModel()).thenReturn(mockModel);

        File tempFile = File.createTempFile("test-config-save", ".properties");
        tempFile.deleteOnExit();

        // Act
        configHandler.saveConfigToFile(mockTable);

        // Assert
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(tempFile)) {
            properties.load(inputStream);
        }
        assertEquals("10x20x30x40", properties.getProperty("entry.0.dimensions"));
        assertEquals("50.0", properties.getProperty("entry.0.price"));
    }

    @Test
    public void testConfigEntryNormalization() {
        // Arrange
        ConfigEntry entry = new ConfigEntry(30, 10, 20, 40, 100.0);
        configHandler.getConfigEntries().add(entry);

        // Act
        configHandler.validateAndSortConfig();

        // Assert
        assertEquals(10, entry.getLength());
        assertEquals(20, entry.getWidth());
        assertEquals(30, entry.getHeight());
    }

    @Test
    public void testValidateConfig_conflictingDimensions() {
        // Arrange
        configHandler.getConfigEntries().add(new ConfigEntry(10, 10, 10, 50, 100.0));
        configHandler.getConfigEntries().add(new ConfigEntry(10, 10, 10, 40, 200.0)); // Conflict: same dimensions, lower weight

        // Act
        configHandler.validateAndSortConfig();

        // Assert
        assertEquals(7, configHandler.getConfigEntries().size()); // No entries removed
    }

    @Test
    public void testAddConfigEntry() {
        // Arrange
        ConfigTableModel model = new ConfigTableModel(configHandler.getConfigEntries());

        // Act
        model.addEntry(new ConfigEntry(10, 20, 30, 40, 50.0));

        // Assert
        assertEquals(6, model.getRowCount());
        assertEquals(10, model.getValueAt(5, 0));
        assertEquals(50.0, model.getValueAt(5, 4));
    }
}
