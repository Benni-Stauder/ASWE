package control;

import data.Packet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * The Calculator class calculates the shipping costs based on the dimensions and weight of a package.
 * Shipping rates are loaded from a configuration file.
 *
 * @author Benni
 * @version 2.1
 */
public class Calculator {

	private static final String CONFIG_FILE = "config.properties"; // default config file

	/**
	 * Calculates the shipping costs for a package based on its dimensions and weight.
	 *
	 * @param pack The package containing length, width, height, and weight.
	 * @return The calculated shipping cost.
	 * @throws IllegalArgumentException if the package dimensions or weight are invalid.
	 */
	public static double calcShippingCosts(Packet pack) {
		int girth = calculateGirth(pack);

		if (girth > 300) { // Adjusted to cm for realistic girth constraints
			throw new IllegalArgumentException("The girth of the package must not exceed 300 cm.");
		}

		Properties properties = loadConfigFile();

		int[] sortedDimensions = getSortedDimensions(pack);

		return calculateCostFromConfig(properties, sortedDimensions, pack.weight());
	}

	/**
	 * Calculates the girth of the package.
	 *
	 * @param pack The package.
	 * @return The calculated girth.
	 */
	private static int calculateGirth(Packet pack) {
		return pack.length() + 2 * pack.width() + 2 * pack.height();
	}

	/**
	 * Loads the configuration file containing shipping cost rules.
	 *
	 * @return A Properties object containing the loaded configuration.
	 * @throws RuntimeException if the configuration file cannot be loaded.
	 */
	private static Properties loadConfigFile() {
		Properties properties = new Properties();
		try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE)) {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error loading configuration file: " + e.getMessage());
		}
		return properties;
	}

	/**
	 * Retrieves and sorts the package dimensions in ascending order.
	 *
	 * @param pack The package.
	 * @return An array of sorted dimensions.
	 */
	private static int[] getSortedDimensions(Packet pack) {
		int[] dimensions = {pack.length(), pack.width(), pack.height()};
		Arrays.sort(dimensions);
		return dimensions;
	}

	/**
	 * Calculates the shipping cost based on the configuration file rules.
	 *
	 * @param properties      The configuration properties.
	 * @param sortedDimensions The package dimensions sorted in ascending order.
	 * @param weight          The package weight.
	 * @return The calculated shipping cost.
	 * @throws IllegalArgumentException if no suitable rate is found in the configuration.
	 */
	private static double calculateCostFromConfig(Properties properties, int[] sortedDimensions, int weight) {
		for (int i = 0; ; i++) {
			String dimensionsKey = "entry." + i + ".dimensions";
			String priceKey = "entry." + i + ".price";

			String dimensions = properties.getProperty(dimensionsKey);
			String priceStr = properties.getProperty(priceKey);

			if (dimensions == null || priceStr == null) {
				break; // End of configuration entries
			}

			String[] limits = dimensions.split("x");
			if (limits.length != 4) {
				throw new RuntimeException("Invalid format in configuration file for entry: " + dimensionsKey);
			}

			int lengthLimit = Integer.parseInt(limits[0]);
			int widthLimit = Integer.parseInt(limits[1]);
			int heightLimit = Integer.parseInt(limits[2]);
			int weightLimit = Integer.parseInt(limits[3]);

			if (sortedDimensions[0] <= lengthLimit && sortedDimensions[1] <= widthLimit &&
					sortedDimensions[2] <= heightLimit && weight <= weightLimit) {
				return Double.parseDouble(priceStr);
			}
		}

		throw new IllegalArgumentException("The package exceeds the allowed dimensions or weight.");
	}
}