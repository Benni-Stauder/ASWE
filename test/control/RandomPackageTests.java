package control;

import data.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für zufällig generierte Pakete.
 * Überprüft die Berechnung der Versandkosten mit zufälligen Eingabewerten.
 *
 * @author Benni
 */
public class RandomPackageTests {

    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        ConfigHandler configHandler = new ConfigHandler();
        File defaultConfig = new File("default.properties");
        configHandler.loadFile(defaultConfig);
    }

    /**
     * Testet 1000 zufällig generierte Pakete mit verschiedenen Größen und Gewichten.
     */
    @Test
    public void testRandomPackages() {
        for (int i = 0; i < 1000; i++) {
            // Generiere zufällige Werte für Paketmaße und Gewicht
            int length = random.nextInt(1300) + 1; // 1 bis 1200 mm
            int width = random.nextInt(700) + 1;   // 1 bis 600 mm
            int height = random.nextInt(700) + 1;  // 1 bis 600 mm
            int weight = random.nextInt(32000) + 1; // 1 bis 31000 g

            Packet packet = new Packet(length, width, height, weight);
            double actualCost;

            try {
                actualCost = Calculator.calcShippingCosts(packet);
            } catch (IllegalArgumentException e) {
                // Wenn eine Exception geworfen wird, überspringen wir diesen Testfall
                continue;
            }

            // Berechne das erwartete Porto
            double expectedCost = calculateExpectedShippingCost(packet);

            // Überprüfe, ob die berechneten Kosten korrekt sind
            assertEquals(expectedCost, actualCost, 0.01, "Falsches Porto für Paket: " + packet);

            // Zusätzliche Überprüfung: Porto darf nie kleiner 0 oder größer 100 sein
            assertTrue(actualCost >= 0 && actualCost <= 100, "Porto außerhalb des erlaubten Bereichs: " + actualCost);
        }
    }

    /**
     * Berechnet das Soll-Resultat (erwartetes Porto) basierend auf den Daten eines Pakets.
     *
     * @param packet Das Paket, für das das erwartete Porto berechnet werden soll.
     * @return Das erwartete Porto.
     */
    private double calculateExpectedShippingCost(Packet packet) {
        int girth = packet.length() + 2 * packet.width() + 2 * packet.height();

        if (packet.length() <= 300 && packet.width() <= 300 && packet.height() <= 150 && packet.weight() <= 1000) {
            return 3.89;
        } else if (packet.length() <= 600 && packet.width() <= 300 && packet.height() <= 150 && packet.weight() <= 2000) {
            return 4.39;
        } else if (packet.length() <= 1200 && packet.width() <= 600 && packet.height() <= 600 && girth <= 3000 && packet.weight() <= 5000) {
            return 5.89;
        } else if (packet.length() <= 1200 && packet.width() <= 600 && packet.height() <= 600 && girth <= 3000 && packet.weight() <= 10000) {
            return 7.99;
        } else if (packet.length() <= 1200 && packet.width() <= 600 && packet.height() <= 600 && packet.weight() <= 31000) {
            return 14.99;
        } else {
            throw new IllegalArgumentException("Ungültiges Paket: " + packet);
        }
    }
}