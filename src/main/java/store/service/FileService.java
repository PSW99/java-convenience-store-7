package store.service;

import store.domain.product.Product;
import store.domain.product.Promotion;
import store.util.FilePath;
import store.util.Parser;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileService {
    private static final String REGEX = ",";
    private static final String NULL = "null";

    public static List<Product> loadProducts() {
        Map<String, Promotion> promotions = loadPromotions();
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FilePath.productFilePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(REGEX);
                products.add(createProduct(values, promotions));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    private static Map<String, Promotion> loadPromotions() {
        Map<String, Promotion> promotions = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FilePath.promotionFilePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(REGEX);
                promotions.put(values[0], createPromotion(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return promotions;
    }

    private static Product createProduct(String[] values, Map<String, Promotion> promotions) {
        String name = values[0];
        int price = Parser.convertStringToInteger(values[1]);
        int quantity = Parser.convertStringToInteger(values[2]);

        if (values[3].equals(NULL)) {
            return new Product(name, price, quantity);
        }
        String promotion = values[3];

        return new Product(name, price, quantity, promotions.get(promotion));
    }

    private static Promotion createPromotion(String[] values) {
        String name = values[0];
        int buy = Parser.convertStringToInteger(values[1]);
        int get = Parser.convertStringToInteger(values[2]);
        LocalDateTime start_date = Parser.convertStringToLocalDate(values[3]);
        LocalDateTime end_date = Parser.convertStringToLocalDate(values[4]);

        return new Promotion(name, buy, get, start_date, end_date);
    }
}
