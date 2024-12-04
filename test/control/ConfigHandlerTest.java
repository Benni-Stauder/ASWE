package control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ConfigHandler} class.
 * <p>
 * These tests verify loading, saving, validating, and sorting configuration entries, as well as handling JTable models.
 * </p>
 */
public class ConfigHandlerTest {

    private ConfigHandler configHandler;

    /**
     * Sets up the test environment by initializing {@link ConfigHandler} and mocking dialogs.
     */
    @BeforeEach
    public void setUp() {
        configHandler = new ConfigHandler();

        try (MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
            // Suppress showMessageDialog and showInputDialog
            mockedJOptionPane.when(() -> JOptionPane.showMessageDialog(any(), any())).thenAnswer(invocation -> null);
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(any())).thenReturn(null);

            File defaultFile = new File("default.properties");
            configHandler.loadFile(defaultFile);
        }
    }


    /**
     * Cleans up after each test by clearing static mocks.
     */
    @AfterEach
    public void tearDown() {
        clearAllCaches();
    }

    /**
     * Tests loading a valid configuration file.
     */
    @Test
    public void testLoadFile_validFile() throws IOException {
        File tempFile = File.createTempFile("test-config", ".properties");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("entry.0.dimensions=10x20x30x40\nentry.0.price=99.99\n");
        }

        configHandler.getConfigEntries().clear(); // Ensure the list is empty
        configHandler.loadFile(tempFile);

        List<ConfigEntry> entries = configHandler.getConfigEntries();
        assertEquals(1, entries.size());
        ConfigEntry entry = entries.getFirst();
        assertEquals(10, entry.getLength());
        assertEquals(20, entry.getWidth());
        assertEquals(30, entry.getHeight());
        assertEquals(40, entry.getWeight());
        assertEquals(99.99, entry.getPrice());
    }

    /**
     * Tests loading an invalid configuration file.
     */
    @Test
    public void testLoadFile_invalidFile() {
        File tempFile = new File("invalid-file.properties");

        assertThrows(RuntimeException.class, () -> configHandler.loadFile(tempFile));
    }

    /**
     * Tests validation and sorting of configuration entries.
     */
    @Test
    public void testValidateAndSortConfig_normalizationAndSorting() {
        configHandler.getConfigEntries().add(new ConfigEntry(30, 10, 20, 50, 100.0));
        configHandler.getConfigEntries().add(new ConfigEntry(20, 40, 10, 40, 200.0));

        configHandler.validateAndSortConfig();

        List<ConfigEntry> entries = configHandler.getConfigEntries();
        assertEquals(7, entries.size());
        assertEquals(10, entries.getFirst().getLength()); // Sorted by length and normalized
        assertEquals(20, entries.getFirst().getWidth());
        assertEquals(30, entries.getFirst().getHeight());
    }

    /**
     * Tests applying the configuration to a mock JTable and saving it to a file.
     */
    @Test
    public void testApplyConfig_success() throws IOException {
        configHandler.getConfigEntries().add(new ConfigEntry(10, 20, 30, 40, 50.0));

        ConfigTableModel tableModel = new ConfigTableModel(configHandler.getConfigEntries());
        JTable configTable = new JTable(tableModel);

        File tempFile = File.createTempFile("config", ".properties");
        tempFile.deleteOnExit();
        System.setProperty("config.file", tempFile.getAbsolutePath());

        configHandler.saveConfigToFile(configTable, tempFile);

        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(tempFile)) {
            properties.load(inputStream);
        }
        assertEquals("10x20x30x40", properties.getProperty("entry.0.dimensions"));
        assertEquals("50.0", properties.getProperty("entry.0.price"));
    }

    /**
     * Tests saving the configuration to a file.
     */
    @Test
    public void testSaveConfigToFile_success() throws IOException {
        configHandler.getConfigEntries().add(new ConfigEntry(10, 20, 30, 40, 50.0));

        ConfigTableModel tableModel = new ConfigTableModel(configHandler.getConfigEntries());
        JTable configTable = new JTable(tableModel);

        File tempFile = File.createTempFile("test-config-save", ".properties");
        tempFile.deleteOnExit();

        configHandler.saveConfigToFile(configTable, tempFile);

        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(tempFile)) {
            properties.load(inputStream);
        }
        assertEquals("10x20x30x40", properties.getProperty("entry.0.dimensions"));
        assertEquals("50.0", properties.getProperty("entry.0.price"));
    }

    /**
     * Tests normalization of configuration entries during validation.
     */
    @Test
    public void testConfigEntryNormalization() {
        ConfigEntry entry = new ConfigEntry(30, 10, 20, 40, 100.0);
        configHandler.getConfigEntries().add(entry);

        configHandler.validateAndSortConfig();

        assertEquals(10, entry.getLength());
        assertEquals(20, entry.getWidth());
        assertEquals(30, entry.getHeight());
    }

    /**
     * Tests handling of conflicting dimensions during validation.
     */
    @Test
    public void testValidateConfig_conflictingDimensions() {
        configHandler.getConfigEntries().add(new ConfigEntry(10, 10, 10, 50, 100.0));
        configHandler.getConfigEntries().add(new ConfigEntry(10, 10, 10, 40, 200.0));

        configHandler.validateAndSortConfig();

        assertEquals(7, configHandler.getConfigEntries().size());
    }

    /**
     * Tests adding a configuration entry to the {@link ConfigTableModel}.
     */
    @Test
    public void testAddConfigEntry() {
        ConfigTableModel model = new ConfigTableModel(configHandler.getConfigEntries());

        model.addEntry(new ConfigEntry(10, 20, 30, 40, 50.0));

        assertEquals(6, model.getRowCount());
        assertEquals(10, model.getValueAt(5, 0));
        assertEquals(50.0, model.getValueAt(5, 4));
    }
}