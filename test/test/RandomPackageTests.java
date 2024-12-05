package test;

import control.Calculator;
import data.Packet;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
    /**
     * Testet 1000 zufällig generierte Pakete mit verschiedenen Größen und Gewichten.
     */
    @Test
    public void testRandomPackages() {
        for (int i = 0; i < 1000; i++) {
            // Generiere zufällige Werte für Paketmaße und Gewicht
            int length = random.nextInt(700) + 1; // 1 bis 600 mm
            int width = random.nextInt(700) + 1;   // 1 bis 600 mm
            int height = random.nextInt(1300) + 1;  // 1 bis 1200 mm
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

        int[] dimensions = {packet.length(), packet.width(), packet.height()};
        Arrays.sort(dimensions);

        if (dimensions[0] <= 150 && dimensions[1] <= 300 && dimensions[2] <= 300 && packet.weight() <= 1000) {
            return 3.89;
        } else if (dimensions[0] <= 150 && dimensions[1] <= 300 && dimensions[2] <= 600 && packet.weight() <= 2000) {
            return 4.39;
        } else if (dimensions[0] <= 600 && dimensions[1] <= 600 && dimensions[2] <= 1200 && girth <= 3000 && packet.weight() <= 5000) {
            return 5.89;
        } else if (dimensions[0] <= 600 && dimensions[1] <= 600 && dimensions[2] <= 1200 && girth <= 3000 && packet.weight() <= 10000) {
            return 7.99;
        } else if (dimensions[0] <= 600 && dimensions[1] <= 600 && dimensions[2] <= 1200 && packet.weight() <= 31000) {
            return 14.99;
        } else {
            throw new IllegalArgumentException("Ungültiges Paket: " + packet);
        }
    }
}