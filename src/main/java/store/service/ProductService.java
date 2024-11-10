package store.service;

import store.domain.product.NoPromotionProducts;
import store.domain.product.Product;
import store.domain.product.PromotionProducts;

import java.util.List;

public class ProductService {
    private final List<Product> products;
    private final PromotionProducts promotionProducts = new PromotionProducts();
    private final NoPromotionProducts noPromotionProducts = new NoPromotionProducts();

    public ProductService(List<Product> products) {
        categorizeProducts(products);
        this.products = products;
    }

    public void categorizeProducts(List<Product> products) {
        for (Product product : products) {
            if (productHasPromotion(product)) {
                promotionProducts.addProduct(product);
            }
            if (!productHasPromotion(product)) {
                noPromotionProducts.addProduct(product);
            }
        }
    }

    private boolean productHasPromotion(Product product) {
        return product.getPromotion() != null;
    }

    public PromotionProducts getPromotionProducts() {
        return promotionProducts;
    }

    public NoPromotionProducts getNoPromotionProducts() {
        return noPromotionProducts;
    }

    public List<Product> getProducts() {
        return products;
    }
}
