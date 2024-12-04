package control;

import data.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testfälle für die Klasse Calculator, die die Berechnung der Versandkosten überprüft.
 * Es werden Äquivalenzklassen und Grenzfälle getestet.
 *
 * @author Benni
 */
public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        ConfigHandler configHandler = new ConfigHandler();
        File defaultFile = new File("default.properties");
        configHandler.loadFile(defaultFile);
    }

    /**
     * Testet die Berechnung für ein kleines Päckchen (30x30x15, 1kg).
     */
    @Test
    public void testSmallPacket() {
        Packet packet = new Packet(30, 30, 15, 1000);
        double shippingCost = calculator.calcShippingCosts(packet);
        assertEquals(3.89, shippingCost, "Kosten für kleines Päckchen sind falsch.");
    }

    /**
     * Testet die Berechnung für ein mittelgroßes Päckchen (60x30x15, 2kg).
     */
    @Test
    public void testMediumPacket() {
        Packet packet = new Packet(60, 30, 15, 2000);
        double shippingCost = calculator.calcShippingCosts(packet);
        assertEquals(4.39, shippingCost, "Kosten für mittelgroßes Päckchen sind falsch.");
    }

    /**
     * Testet die Berechnung für ein Paket (120x60x60, 5kg, Gurtmaß <= 300cm).
     */
    @Test
    public void testSmallPackage() {
        Packet packet = new Packet(120, 60, 60, 5000);
        double shippingCost = calculator.calcShippingCosts(packet);
        assertEquals(5.89, shippingCost, "Kosten für kleines Paket sind falsch.");
    }

    /**
     * Testet die Berechnung für ein Paket (120x60x60, 10kg, Gurtmaß <= 300cm).
     */
    @Test
    public void testMediumPackage() {
        Packet packet = new Packet(120, 60, 60, 10000);
        double shippingCost = calculator.calcShippingCosts(packet);
        assertEquals(7.99, shippingCost, "Kosten für mittelgroßes Paket sind falsch.");
    }

    /**
     * Testet die Berechnung für ein großes Paket (120x60x60, 31kg).
     */
    @Test
    public void testLargePackage() {
        Packet packet = new Packet(120, 60, 60, 31000);
        double shippingCost = calculator.calcShippingCosts(packet);
        assertEquals(14.99, shippingCost, "Kosten für großes Paket sind falsch.");
    }

    /**
     * Testet die Ablehnung eines Pakets mit ungültigen Dimensionen (negativer Wert).
     */
    @Test
    public void testInvalidDimensions() {
        Packet packet = new Packet(-1, 60, 60, 5000);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet),
                "Ungültige Dimensionen sollten eine Exception werfen.");
    }

    /**
     * Testet die Ablehnung eines Pakets mit ungültigem Gurtmaß (> 300cm).
     */
    @Test
    public void testInvalidGirth() {
        Packet packet = new Packet(1500, 100, 100, 5000);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet),
                "Ungültiges Gurtmaß sollte eine Exception werfen.");
    }

    /**
     * Testet die Ablehnung eines Pakets mit null maßen.
     */
    @Test
    public void testInvalidSizes() {
        Packet packet1 = new Packet(0, 100, 100, 5000);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet1),
                "Ungültige Größe sollte eine Exception werfen.");
        Packet packet2 = new Packet(100, 0, 100, 5000);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet2),
                "Ungültige Größe sollte eine Exception werfen.");
        Packet packet3 = new Packet(100, 100, 0, 5000);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet3),
                "Ungültige Größe sollte eine Exception werfen.");
        Packet packet4 = new Packet(100, 100, 100, 0);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet4),
                "Ungültige Größe sollte eine Exception werfen.");
    }

    /**
     * Testet die Ablehnung eines Pakets mit zu hohem Gewicht (> 31kg).
     */
    @Test
    public void testOverweightPackage() {
        Packet packet = new Packet(120, 60, 60, 32000);
        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet),
                "Zu hohes Gewicht sollte eine Exception werfen.");
    }
}
