package store.domain.product;

import java.util.HashMap;
import java.util.Map;

public class PromotionProducts {
    private final Map<String, Product> promotionProducts = new HashMap<>();

    public void addProduct(Product product) {
        promotionProducts.put(product.getName(), product);
    }

    public Product getProduct(String name) {
        return promotionProducts.get(name);
    }

    public boolean containsProduct(String name) {
        return promotionProducts.containsKey(name);
    }
}
