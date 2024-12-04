package data;

/**
 * Represents a package with dimensions and weight, used for shipping cost calculations.
 *
 * <p>This record provides an immutable and concise representation of package data.</p>
 *
 * @param length The length of the package in centimeters
 * @param width  The width of the package in centimeters
 * @param height The height of the package in centimeters
 * @param weight The weight of the package in kilograms
 */
public record Packet(int length, int width, int height, int weight) {

	/**
	 * Validates the dimensions and weight of the package upon creation.
	 *
	 * @throws IllegalArgumentException if any dimension or weight is less than or equal to zero
	 */
	public Packet {
		if (length <= 0 || width <= 0 || height <= 0 || weight <= 0) {
			throw new IllegalArgumentException("Dimensions and weight must be greater than zero.");
		}
	}
}