package data;

public class Packet {

	// length of package in millimeters
	public int length;

	// width of package in millimeters
	public int width;
	
	// height of package in millimeters
	public int height;
	
	// weight of package in grams
	public int weight;

	// constructor
	public Packet(int length, int width, int height, int weight) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;
	}
	
}
