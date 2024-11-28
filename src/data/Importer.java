package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Importer {

    public List<Double> importShippingCosts() {
        List<Double> shippingCosts = new ArrayList<>();
        String filePath = "shippingCosts.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read the first line
            if (line != null) {
                String[] values = line.split(";");
                for (String value : values) {
                    shippingCosts.add(Double.parseDouble(value));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading shipping costs file: " + e.getMessage());
        }

        return shippingCosts;
    }
}
