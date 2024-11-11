package store.service;

import store.domain.product.Product;
import store.domain.product.Promotion;
import store.util.FilePath;
import store.util.Parser;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class FileService {
    private static final String REGEX = ",";
    private static final String NULL = "null";

    public static List<Product> loadProducts() {
        Map<String, Promotion> promotions = loadPromotions();
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FilePath.productFilePath))) {
            reader.readLine();
            reader.lines().forEach(line -> processLine(line, promotions, products));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    private static void processLine(String line, Map<String, Promotion> promotions, List<Product> products) {
        Product currentProduct = getProduct(line, promotions);
        if (!products.isEmpty()) {
            Product lastProduct = products.getLast();
            if (shouldRemoveLastProduct(lastProduct, currentProduct)) {
                products.removeLast();
            }
        }

        products.add(currentProduct);
        if (currentProduct.getPromotion() != null && !hasNonPromotionProduct(products, currentProduct.getName())) {
            products.add(new Product(currentProduct.getName(), currentProduct.getPrice(), 0));
        }
    }

    private static boolean shouldRemoveLastProduct(Product lastProduct, Product currentProduct) {
        return lastProduct.getQuantity() == 0 && Objects.equals(lastProduct.getName(), currentProduct.getName());
    }

    private static Product getProduct(String line, Map<String, Promotion> promotions) {
        String[] values = line.split(REGEX);
        return createProduct(values, promotions);
    }

    private static boolean hasNonPromotionProduct(List<Product> products, String name) {
        return products.stream().anyMatch(product -> product.getName().equals(name) && product.getPromotion() == null);
    }

    private static Map<String, Promotion> loadPromotions() {
        Map<String, Promotion> promotions = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FilePath.promotionFilePath))) {
            reader.readLine(); // 헤더 스킵
            reader.lines().forEach(line -> {
                String[] values = line.split(REGEX);
                promotions.put(values[0], createPromotion(values));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return promotions;
    }

    private static Product createProduct(String[] values, Map<String, Promotion> promotions) {
        String name = values[0];
        int price = Parser.convertStringToInteger(values[1]);
        int quantity = Parser.convertStringToInteger(values[2]);
        String promotionName = null;
        if (!values[3].equals(NULL)) {
            promotionName = values[3];
        }
        Promotion promotion = null;
        if (promotionName != null) {
            promotion = promotions.get(promotionName);
        }

        return new Product(name, price, quantity, promotion);
    }

    private static Promotion createPromotion(String[] values) {
        String name = values[0];
        int buy = Parser.convertStringToInteger(values[1]);
        int get = Parser.convertStringToInteger(values[2]);
        LocalDateTime startDate = Parser.convertStringToLocalDate(values[3]);
        LocalDateTime endDate = Parser.convertStringToLocalDate(values[4]);

        return new Promotion(name, buy, get, startDate, endDate);
    }
}
