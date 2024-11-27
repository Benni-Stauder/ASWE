package control;

import data.Packet;

public class Calculator {

	public double calcShippingCosts(Packet pack) {
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

		// Porto-Berechnung nach den Regeln
		if (pack.length <= 300 && pack.width <= 300 && pack.height <= 150 && pack.weight <= 1000) {
			return 3.89;
		} else if (pack.length <= 600 && pack.width <= 300 && pack.height <= 150 && pack.weight <= 2000) {
			return 4.39;
		} else if (pack.length <= 1200 && pack.width <= 600 && pack.height <= 600 && girth <= 300 && pack.weight <= 5000) {
			return 5.89;
		} else if (pack.length <= 1200 && pack.width <= 600 && pack.height <= 600 && girth <= 300 && pack.weight <= 10000) {
			return 7.99;
		} else if (pack.length <= 1200 && pack.width <= 600 && pack.height <= 600 && pack.weight <= 31000) {
			return 14.99;
		} else {
			throw new IllegalArgumentException("Das Paket überschreitet die zulässigen Maße oder das Gewicht.");
		}
	}
}