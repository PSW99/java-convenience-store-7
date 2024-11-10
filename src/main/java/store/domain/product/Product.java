package store.domain.product;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void decreaseQuantity(int newQuantity) {
        quantity -= newQuantity;
    }

    @Override
    public String toString() {
        if (promotion != null && quantity > 0) {
            return String.format("- %s %,d원 %d개 %s", name, price, quantity, promotion.getName());
        }

        if (quantity == 0) {
            return String.format("- %s %,d원 재고 없음", name, price);
        }

        return String.format("- %s %,d원 %d개", name, price, quantity);
    }
}
