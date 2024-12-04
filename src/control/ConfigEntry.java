package control;

/**
 * Represents a configuration entry for package dimensions, weight, and price.
 * Used to store and retrieve package-specific data.
 */
public class ConfigEntry {

    private int length; // Length of the package in millimeters
    private int width;  // Width of the package in millimeters
    private int height; // Height of the package in millimeters
    private int weight; // Weight of the package in grams
    private double price; // Price of the package in Euros

    /**
     * Constructs a new ConfigEntry with the specified dimensions, weight, and price.
     *
     * @param length The length of the package in millimeters
     * @param width  The width of the package in millimeters
     * @param height The height of the package in millimeters
     * @param weight The weight of the package in grams
     * @param price  The price of the package in Euros
     */
    public ConfigEntry(int length, int width, int height, int weight, double price) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.price = price;
    }

    /**
     * Gets the length of the package.
     *
     * @return The length in millimeters
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the package.
     *
     * @param length The length in millimeters
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the width of the package.
     *
     * @return The width in millimeters
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the package.
     *
     * @param width The width in millimeters
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the package.
     *
     * @return The height in millimeters
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the package.
     *
     * @param height The height in millimeters
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the weight of the package.
     *
     * @return The weight in grams
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the package.
     *
     * @param weight The weight in grams
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the price of the package.
     *
     * @return The price in Euros
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the package.
     *
     * @param price The price in Euros
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the ConfigEntry object.
     *
     * @return A string containing the package details
     */
    @Override
    public String toString() {
        return String.format("ConfigEntry[length=%d, width=%d, height=%d, weight=%d, price=%.2f]",
                length, width, height, weight, price);
    }
}
