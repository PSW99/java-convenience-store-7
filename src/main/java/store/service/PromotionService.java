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
        int totalQuantity = order.getQuantity();

        if (promoProduct.getQuantity() > totalQuantity) {
            return applyFullPromotion(order, promoProduct);
        }

        return applyPartialPromotion(order, promoProduct, noPromoProduct);
    }

    private PurchasedProduct applyFullPromotion(Order order, Product promoProduct) {
        promoProduct.decreaseQuantity(order.getQuantity());
        int freeQuantity = processFreeQuantity(order, promoProduct);
        return createPurchasedProduct(order, promoProduct.getPrice() * order.getQuantity(),
                getDiscount(order.getQuantity(), promoProduct), freeQuantity, 0);
    }

    private PurchasedProduct applyPartialPromotion(Order order, Product promoProduct, Product noPromoProduct) {
        int totalQuantity = order.getQuantity();
        int promoQuantity = calculatePromotionalQuantity(promoProduct.getQuantity(), promoProduct.getPromotion());
        int freeQuantity = calculateFreeItems(promoProduct.getQuantity(), promoProduct.getPromotion());

        if (!confirmNoPromoPurchase(order, totalQuantity, promoQuantity)) {
            order.decreaseQuantity(totalQuantity - promoQuantity);
            return createPurchasedProduct(order, promoProduct.getPrice() * order.getQuantity(),
                    getDiscount(order.getQuantity(), promoProduct), freeQuantity, 0);
        }


        return applyMixedPurchase(order, promoProduct, noPromoProduct, totalQuantity, promoQuantity, freeQuantity);
    }

    private PurchasedProduct applyMixedPurchase(Order order, Product promoProduct, Product noPromoProduct,
                                                int totalQuantity, int promoQuantity, int freeQuantity) {
        int promoAmount = promoQuantity * promoProduct.getPrice();
        int noPromoAmount = (totalQuantity - promoQuantity) * noPromoProduct.getPrice();
        promoProduct.decreaseQuantity(promoProduct.getQuantity());
        noPromoProduct.decreaseQuantity(totalQuantity - promoQuantity);

        return createPurchasedProduct(order, promoAmount, getDiscount(totalQuantity, promoProduct), freeQuantity, noPromoAmount);
    }

    private int processFreeQuantity(Order order, Product promoProduct) {
        if (hasFreeQuantityAvailable(order.getQuantity(), promoProduct.getPromotion()) && InputView.askForAdditionalFreeItem(order.getName())) {
            order.increaseQuantity();
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

    private boolean confirmNoPromoPurchase(Order order, int totalQuantity, int promoQuantity) {
        return totalQuantity - promoQuantity > 0 && InputView.askForNonPromotionalPurchase(order.getName(), totalQuantity - promoQuantity);
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

    private int getDiscount(int quantity, Product promoProduct) {
        Promotion promotion = promoProduct.getPromotion();
        if (promotion == null || !promotion.isAvailable(today)) {
            return 0;
        }

        return (quantity / (promotion.getBuy() + promotion.getGet())) * promotion.getGet() * promoProduct.getPrice();
    }

    private boolean hasFreeQuantityAvailable(int quantity, Promotion promotion) {
        return quantity >= promotion.getBuy() && quantity != calculatePromotionalQuantity(quantity, promotion);
    }

    private boolean isPromotionAvailable(Product promoProduct) {
        Promotion promotion = promoProduct.getPromotion();

        return promotion != null && promotion.isAvailable(today);
    }

    private int calculateFreeItems(int quantity, Promotion promotion) {
        return quantity / (promotion.getBuy() + promotion.getGet());
    }

    private int calculatePromotionalQuantity(int quantity, Promotion promotion) {
        return (quantity / (promotion.getBuy() + promotion.getGet())) * (promotion.getBuy() + promotion.getGet());
    }
}
