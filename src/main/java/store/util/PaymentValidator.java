package store.util;

import store.domain.order.Order;
import store.domain.product.NoPromotionProducts;
import store.domain.product.PromotionProducts;

public class PaymentValidator {

    public static void validatePayment(Order order, PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        validateExistProduct(order, promotionProducts, noPromotionProducts);
        validateStockAvailability(order, promotionProducts, noPromotionProducts);
    }

    private static void validateExistProduct(Order order, PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        String name = order.getName();

        if (!promotionProducts.containsProduct(name) && !noPromotionProducts.containsProduct(name)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_PRODUCT_NAME_MESSAGE.getMessage());
        }
    }

    private static void validateStockAvailability(Order order, PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        String name = order.getName();
        int quantity = order.getQuantity();
        int totalStock = getProductStock(name, promotionProducts, noPromotionProducts);

        if (totalStock < quantity) {
            throw new IllegalArgumentException(ErrorMessage.STOCK_LIMIT_EXCEEDED_MESSAGE.getMessage());
        }
    }

    private static int getProductStock(String name, PromotionProducts promotionProducts, NoPromotionProducts noPromotionProducts) {
        int totalStock = 0;

        if (promotionProducts.containsProduct(name)) {
            totalStock += promotionProducts.getProduct(name).getQuantity();
        }

        if (noPromotionProducts.containsProduct(name)) {
            totalStock += noPromotionProducts.getProduct(name).getQuantity();
        }

        return totalStock;
    }
}
