package control;

import data.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Calculator} class, verifying the correctness of shipping cost calculations.
 *
 * <p>These tests cover equivalence classes, edge cases, and invalid input scenarios.</p>
 *
 * @author Benni
 */
public class CalculatorTest {

    /**
     * Sets up the test environment by loading the default configuration file.
     */
    @BeforeEach
    void setUp() {
        ConfigHandler configHandler = new ConfigHandler();
        File defaultFile = new File("default.properties");
        configHandler.loadFile(defaultFile);
    }

    /**
     * Verifies the calculation for a small packet with dimensions 30x30x15cm and weight 1kg.
     */
    @Test
    public void testSmallPacket() {
        Packet packet = new Packet(30, 30, 15, 1000);
        double shippingCost = Calculator.calcShippingCosts(packet);
        assertEquals(3.89, shippingCost, "Incorrect shipping cost for a small packet.");
    }

    /**
     * Verifies the calculation for a medium packet with dimensions 60x30x15cm and weight 2kg.
     */
    @Test
    public void testMediumPacket() {
        Packet packet = new Packet(60, 30, 15, 2000);
        double shippingCost = Calculator.calcShippingCosts(packet);
        assertEquals(4.39, shippingCost, "Incorrect shipping cost for a medium packet.");
    }

    /**
     * Verifies the calculation for a small package with dimensions 60x30x30cm, weight 5kg, and girth ≤ 300cm.
     */
    @Test
    public void testSmallPackage() {
        Packet packet = new Packet(60, 30, 30, 5000);
        double shippingCost = Calculator.calcShippingCosts(packet);
        assertEquals(5.89, shippingCost, "Incorrect shipping cost for a small package.");
    }

    /**
     * Verifies the calculation for a medium package with dimensions 100x40x40cm, weight 10kg, and girth ≤ 300cm.
     */
    @Test
    public void testMediumPackage() {
        Packet packet = new Packet(100, 40, 40, 10000);
        double shippingCost = Calculator.calcShippingCosts(packet);
        assertEquals(7.99, shippingCost, "Incorrect shipping cost for a medium package.");
    }

    /**
     * Verifies the calculation for a large package with dimensions 100x40x40cm and weight 31kg.
     */
    @Test
    public void testLargePackage() {
        Packet packet = new Packet(100, 40, 40, 31000);
        double shippingCost = Calculator.calcShippingCosts(packet);
        assertEquals(14.99, shippingCost, "Incorrect shipping cost for a large package.");
    }

    /**
     * Verifies that a package with negative dimensions throws an exception.
     */
    @Test
    public void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Packet(-1, 60, 60, 5000),
                "Dimensions and weight must be greater than zero.");
    }

    /**
     * Verifies that a package with an invalid girth (> 300cm) throws an exception.
     */
    @Test
    public void testInvalidGirth() {
        Packet packet = new Packet(1500, 100, 100, 5000);
        assertThrows(IllegalArgumentException.class, () -> Calculator.calcShippingCosts(packet),
                "Invalid girth should throw an exception.");
    }

    /**
     * Verifies that a package with zero dimensions or weight throws an exception.
     */
    @Test
    public void testInvalidSizes() {
        assertAll("Zero dimensions or weight should throw exceptions",
                () -> assertThrows(IllegalArgumentException.class, () -> Calculator.calcShippingCosts(new Packet(0, 100, 100, 5000)), "Zero length should throw an exception."),
                () -> assertThrows(IllegalArgumentException.class, () -> Calculator.calcShippingCosts(new Packet(100, 0, 100, 5000)), "Zero width should throw an exception."),
                () -> assertThrows(IllegalArgumentException.class, () -> Calculator.calcShippingCosts(new Packet(100, 100, 0, 5000)), "Zero height should throw an exception."),
                () -> assertThrows(IllegalArgumentException.class, () -> Calculator.calcShippingCosts(new Packet(100, 100, 100, 0)), "Zero weight should throw an exception.")
        );
    }

    /**
     * Verifies that a package with weight exceeding 31kg throws an exception.
     */
    @Test
    public void testOverweightPackage() {
        Packet packet = new Packet(120, 60, 60, 32000);
        assertThrows(IllegalArgumentException.class, () -> Calculator.calcShippingCosts(packet),
                "Overweight packages should throw an exception.");
    }

    /**
     * Verifies if an error is thrown on a corrupted config file
     */
    @Test
    public void testWrongConfig() {
        Packet packet = new Packet(120, 60, 60, 32000);
        ConfigHandler configHandler = new ConfigHandler();
        File wrongConfigFile = new File("test/control/wrongConfig.properties");
        configHandler.loadFile(wrongConfigFile);
        ConfigTableModel tableModel = new ConfigTableModel(configHandler.getConfigEntries());
        JTable configTable = new JTable(tableModel);
        File configFile = new File("config.properties");
        configHandler.saveConfigToFile(configTable, configFile);
        assertThrows(RuntimeException.class, () -> Calculator.calcShippingCosts(packet));
    }
}
