package data;

/**
 * @param length length of package in centimeters
 * @param width  width of package in centimeters
 * @param height height of package in centimeters
 * @param weight weight of package in kilogramms
 */
public record Packet(int length, int width, int height, int weight) {}
