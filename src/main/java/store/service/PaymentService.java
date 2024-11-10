package store.service;

import store.domain.order.Order;
import store.domain.order.Orders;
import store.domain.product.NoPromotionProducts;
import store.domain.product.Product;
import store.domain.product.PromotionProducts;
import store.domain.purchasedProduct.PurchasedProduct;
import store.util.PaymentValidator;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private final PromotionService promotionService;

    public PaymentService() {
        promotionService = new PromotionService();
    }

    public List<PurchasedProduct> payment(Orders orders, PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        List<PurchasedProduct> purchasedProducts = new ArrayList<>();

        for (Order order : orders.orders()) {
            PaymentValidator.validatePayment(order, promotionProducts, noPromotionProducts);
            purchasedProducts.add(processOrder(order, promotionProducts, noPromotionProducts));
        }

        return purchasedProducts;
    }

    private PurchasedProduct processOrder(Order order, PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        String name = order.getName();
        if (promotionProducts.containsProduct(name)) {
            return promotionService.calculatePromotionOrder(order, promotionProducts.getProduct(name), noPromotionProducts.getProduct(name));
        }

        return processRegularPayment(order, noPromotionProducts.getProduct(name));
    }

    private PurchasedProduct processRegularPayment(Order order, Product noPromotionProduct) {
        int totalAmount = noPromotionProduct.getPrice() * order.getQuantity();
        noPromotionProduct.decreaseQuantity(order.getQuantity());

        return new PurchasedProduct.Builder(order.getName(), order.getQuantity())
                .noPromoAmount(totalAmount)
                .build();
    }
}
