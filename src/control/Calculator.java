package control;

import data.Packet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Die Klasse Calculator berechnet die Versandkosten basierend auf den Dimensionen und dem Gewicht eines Pakets.
 * Die Versandkosten werden aus einer Konfigurationsdatei geladen.
 *
 * @author Benni
 * @version 2.0
 */
public class Calculator {

	private static final String CONFIG_FILE = "config.properties";

	/**
	 * Berechnet die Versandkosten eines Pakets basierend auf Dimensionen und Gewicht.
	 *
	 * @param pack Das Paket mit Länge, Breite, Höhe und Gewicht.
	 * @return Die berechneten Versandkosten.
	 * @throws IllegalArgumentException, wenn ungültige Eingaben gemacht werden.
	 */
	public static double calcShippingCosts(Packet pack) {
		// Defensiv: Prüfen auf ungültige Eingaben
		if (pack.length <= 0 || pack.width <= 0 || pack.height <= 0 || pack.weight <= 0) {
			throw new IllegalArgumentException("Alle Dimensionen und das Gewicht müssen positiv sein.");
		}

		// Berechnung des Gurtmaßes (L + 2xB + 2xH)
		int girth = pack.length + 2 * pack.width + 2 * pack.height;

		// Defensiv: Prüfen auf unrealistische Werte
		if (girth > 3000) {
			throw new IllegalArgumentException("Das Gurtmaß darf 300 cm nicht überschreiten.");
		}

		// Laden der Versandkosten aus der Konfigurationsdatei
		Properties properties = new Properties();
		try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE)) {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Fehler beim Laden der Konfigurationsdatei: " + e.getMessage());
		}

		// Sortieren der Dimensionen des Pakets nach Größe
		int[] packDimensions = {pack.length, pack.width, pack.height};

		// Dimensionen sortieren
		Arrays.sort(packDimensions);

		// Porto-Berechnung basierend auf den Regeln aus der Konfigurationsdatei
		for (int i = 0; ; i++) {
			String dimensions = properties.getProperty("entry." + i + ".dimensions");
			String priceStr = properties.getProperty("entry." + i + ".price");

			if (dimensions == null || priceStr == null) {
				break; // Ende der Konfigurationsdatei erreicht
			}

			String[] parts = dimensions.split("x");
			if (parts.length != 4) {
				throw new RuntimeException("Ungültiges Format in der Konfigurationsdatei für entry." + i);
			}

			int lengthLimit = Integer.parseInt(parts[0]);
			int widthLimit = Integer.parseInt(parts[1]);
			int heightLimit = Integer.parseInt(parts[2]);
			int weightLimit = Integer.parseInt(parts[3]);

			if (packDimensions[0] <= lengthLimit && packDimensions[1] <= widthLimit && packDimensions[2] <= heightLimit && pack.weight <= weightLimit) {
				return Double.parseDouble(priceStr);
			}
		}

		// Kein passender Tarif gefunden
		throw new IllegalArgumentException("Das Paket überschreitet die zulässigen Maße oder das Gewicht.");
	}
}