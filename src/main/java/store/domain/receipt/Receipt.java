package store.domain.receipt;

import store.domain.product.MemberShip;
import store.domain.purchasedProduct.PurchasedProduct;

import java.util.List;

public class Receipt {
    private final List<PurchasedProduct> purchasedProducts;
    private final int totalAmount;
    private final int totalQuantity;
    private final int discountAmount;
    private int membershipDiscount = 0;
    private final int finalAmount;

    public Receipt(List<PurchasedProduct> purchasedProducts, boolean hasMembershipDiscount) {
        this.purchasedProducts = purchasedProducts;
        this.totalAmount = calculateTotalAmount(purchasedProducts);
        this.totalQuantity = calculateTotalQuantity(purchasedProducts);
        this.discountAmount = calculatePromotionDiscount(purchasedProducts);

        if (hasMembershipDiscount) {
            this.membershipDiscount = calculateMembershipDiscount();
        }

        this.finalAmount = totalAmount - discountAmount - membershipDiscount;
    }

    private int calculateTotalQuantity(List<PurchasedProduct> purchasedProducts) {
        return purchasedProducts.stream()
                .mapToInt(PurchasedProduct::getQuantity)
                .sum();
    }

    private int calculateTotalAmount(List<PurchasedProduct> purchasedProducts) {
        return purchasedProducts.stream()
                .mapToInt(product -> product.getPromoAmount() + product.getNoPromoAmount())
                .sum();
    }

    private int calculatePromotionDiscount(List<PurchasedProduct> purchasedProducts) {
        return purchasedProducts.stream()
                .mapToInt(PurchasedProduct::getDiscountAmount)
                .sum();
    }

    private int calculateNoPromAmount(List<PurchasedProduct> purchasedProducts) {
        return purchasedProducts.stream()
                .mapToInt(PurchasedProduct::getNoPromoAmount)
                .sum();
    }

    private int calculateMembershipDiscount() {
        int nonPromotionAmount = calculateNoPromAmount(purchasedProducts);
        return MemberShip.calculateDiscount(nonPromotionAmount);
    }

    public List<PurchasedProduct> getPurchasedProducts() {
        return purchasedProducts;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }
}
