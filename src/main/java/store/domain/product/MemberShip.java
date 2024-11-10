package store.domain.product;

public class MemberShip {

    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;

    public static int calculateDiscount(int nonPromotionAmount) {
        double rawDiscount = nonPromotionAmount * DISCOUNT_RATE;
        return Math.min((int) rawDiscount, MAX_DISCOUNT);
    }
}
