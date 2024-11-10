package store.domain.product;

import java.util.HashMap;
import java.util.Map;

public class NoPromotionProducts {
    private final Map<String, Product> noPromotionProducts = new HashMap<>();

    public void addProduct(Product product) {
        noPromotionProducts.put(product.getName(), product);
    }

    public Product getProduct(String name) {
        return noPromotionProducts.get(name);
    }

    public boolean containsProduct(String name) {
        return noPromotionProducts.containsKey(name);
    }
}
