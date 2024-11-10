package store.domain.purchasedProduct;

public class PurchasedProduct {
    private final String name;
    private final int quantity;
    private final int promoAmount;
    private final int noPromoAmount;
    private final int discountAmount;
    private final int givenQuantity;

    private PurchasedProduct(Builder builder) {
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.promoAmount = builder.promoAmount;
        this.noPromoAmount = builder.noPromoAmount;
        this.discountAmount = builder.discountAmount;
        this.givenQuantity = builder.givenQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getPromoAmount() {
        return promoAmount;
    }

    public int getNoPromoAmount() {
        return noPromoAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getGivenQuantity() {
        return givenQuantity;
    }

    public static class Builder {
        private final String name;
        private final int quantity;
        private int promoAmount;
        private int noPromoAmount;
        private int discountAmount;
        private int givenQuantity;

        public Builder(String name, int quantity) { // 필수 필드인 name을 받는 생성자
            this.name = name;
            this.quantity = quantity;
        }


        public Builder promoAmount(int promoAmount) {
            this.promoAmount = promoAmount;
            return this;
        }

        public Builder noPromoAmount(int noPromoAmount) {
            this.noPromoAmount = noPromoAmount;
            return this;
        }

        public Builder discountAmount(int discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public Builder givenQuantity(int givenQuantity) {
            this.givenQuantity = givenQuantity;
            return this;
        }

        public PurchasedProduct build() {
            return new PurchasedProduct(this);
        }
    }
}
