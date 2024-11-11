package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.domain.purchasedProduct.PurchasedProduct;
import store.view.InputView;

import java.time.LocalDateTime;

public class PromotionService {
    private final LocalDateTime today;

    public PromotionService() {
        this.today = DateTimes.now();
    }

    public PurchasedProduct calculatePromotionOrder(Order order, Product promoProduct, Product noPromoProduct) {
        if (isPromotionAvailable(promoProduct)) {
            return calculateWithPromotion(order, promoProduct, noPromoProduct);
        }
        int regularPrice = payRegularPrice(order, promoProduct, noPromoProduct);

        return createPurchasedProduct(order, regularPrice, 0, 0, 0);
    }

    private PurchasedProduct calculateWithPromotion(Order order, Product promoProduct, Product noPromoProduct) {
        if (promoProduct.getQuantity() >= order.getQuantity()) {
            return applyFullPromotion(order, promoProduct);
        }

        return applyPartialPromotion(order, promoProduct, noPromoProduct);
    }

    private PurchasedProduct applyFullPromotion(Order order, Product promoProduct) {
        int freeQuantity = processFreeQuantity(order, promoProduct);
        int promoQuantity = calculatePromotionalQuantity(order.getQuantity(), promoProduct.getPromotion());

        if (order.getQuantity() != promoQuantity) {
            promoProduct.decreaseQuantity(order.getQuantity());

            return createPurchasedProduct(order, promoProduct.getPrice() * promoQuantity,
                    getDiscount(freeQuantity, promoProduct.getPrice()), freeQuantity, Math.abs(((order.getQuantity()) - promoQuantity) * promoProduct.getPrice()));
        }

        promoProduct.decreaseQuantity(order.getQuantity());
        return createPurchasedProduct(order, promoProduct.getPrice() * order.getQuantity(),
                getDiscount(freeQuantity, promoProduct.getPrice()), freeQuantity, 0);
    }

    private PurchasedProduct applyPartialPromotion(Order order, Product promoProduct, Product noPromoProduct) {
        int freeQuantity = calculateFreeItems(promoProduct.getQuantity(), promoProduct.getPromotion());
        int promoQuantity = calculatePromotionalQuantity(promoProduct.getQuantity(), promoProduct.getPromotion());

        if (!confirmNoPromoPurchase(order, order.getQuantity(), promoQuantity)) {
            order.decreaseQuantity(order.getQuantity() - promoQuantity);

            return createPurchasedProduct(order, promoProduct.getPrice() * promoQuantity,
                    getDiscount(freeQuantity, promoProduct.getPrice()), freeQuantity, 0);
        }

        return applyMixedPurchase(order, promoProduct, noPromoProduct, promoQuantity, freeQuantity);
    }

    private PurchasedProduct applyMixedPurchase(Order order, Product promoProduct, Product noPromoProduct,
                                                int promoQuantity, int freeQuantity) {
        int promoAmount = promoQuantity * promoProduct.getPrice();
        int noPromoAmount = (order.getQuantity() - promoQuantity) * noPromoProduct.getPrice();

        noPromoProduct.decreaseQuantity(order.getQuantity() - promoProduct.getQuantity());
        promoProduct.decreaseQuantity(promoProduct.getQuantity());

        return createPurchasedProduct(order, promoAmount, getDiscount(freeQuantity, promoProduct.getPrice()), freeQuantity, noPromoAmount);
    }

    private int processFreeQuantity(Order order, Product promoProduct) {
        if (hasFreeQuantityAvailable(order.getQuantity(), promoProduct)) {
            if (InputView.askForAdditionalFreeItem(order.getName())) {
                order.increaseQuantity();
            }
        }
        return calculateFreeItems(order.getQuantity(), promoProduct.getPromotion());
    }

    private PurchasedProduct createPurchasedProduct(Order order, int promoAmount, int discountAmount,
                                                    int freeQuantity, int noPromoAmount) {
        return new PurchasedProduct.Builder(order.getName(), order.getQuantity())
                .promoAmount(promoAmount)
                .discountAmount(discountAmount)
                .givenQuantity(freeQuantity)
                .noPromoAmount(noPromoAmount)
                .build();
    }

    private boolean confirmNoPromoPurchase(Order order, int orderQuantity, int promoQuantity) {
        return orderQuantity - promoQuantity > 0 && InputView.askForNonPromotionalPurchase(order.getName(), orderQuantity - promoQuantity);
    }

    private int payRegularPrice(Order order, Product promoProduct, Product noPromoProduct) {
        int totalQuantity = order.getQuantity();

        if (promoProduct.getQuantity() >= totalQuantity) {
            promoProduct.decreaseQuantity(totalQuantity);
            return totalQuantity * promoProduct.getPrice();
        }

        int remainingQuantity = totalQuantity - promoProduct.getQuantity();
        noPromoProduct.decreaseQuantity(remainingQuantity);

        return totalQuantity * promoProduct.getPrice();
    }

    private int getDiscount(int quantity, int price) {
        return quantity * price;
    }

    private boolean hasFreeQuantityAvailable(int quantity, Product promoProduct) {
        int buy = promoProduct.getPromotion().getBuy();
        int get = promoProduct.getPromotion().getGet();

        if (quantity < buy) {
            return false;
        }

        int promoBundles = quantity / (buy + get);

        return quantity - promoBundles * (buy + get) == buy;
    }

    private boolean isPromotionAvailable(Product promoProduct) {
        Promotion promotion = promoProduct.getPromotion();

        return promotion != null && promotion.isAvailable(today);
    }

    private int calculateFreeItems(int quantity, Promotion promotion) {
        return quantity / (promotion.getBuy() + promotion.getGet());
    }

    private int calculatePromotionalQuantity(int quantity, Promotion promotion) {
        if (quantity == promotion.getBuy()) {
            return quantity - 1;
        }

        return (quantity / (promotion.getBuy() + promotion.getGet())) * (promotion.getBuy() + promotion.getGet());
    }
}
