package store.view;

import store.domain.product.Product;
import store.domain.purchasedProduct.PurchasedProduct;
import store.domain.receipt.Receipt;

import java.util.List;

public class OutputView {
    private static final String STORE_INVENTORY_WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";

    public static void printWelcomeMessage() {
        System.out.println(STORE_INVENTORY_WELCOME_MESSAGE);
    }
    public static void printProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public static void printErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    public static void printReceipt(Receipt receipt) {
        printReceiptHeader();
        printPurchasedItems(receipt.getPurchasedProducts());
        printGivenItems(receipt.getPurchasedProducts());
        printTotals(receipt);
    }

    private static void printReceiptHeader() {
        System.out.println("==============W 편의점================");
        System.out.printf("%-10s %-10s %-10s\n", "상품명", "수량", "금액");
    }

    private static void printPurchasedItems(List<PurchasedProduct> purchasedProducts) {
        for (PurchasedProduct product : purchasedProducts) {
            System.out.printf("%-10s %-10d %-10s\n",
                    product.getName(),
                    product.getQuantity(),
                    formatCurrency(product.getPromoAmount() + product.getNoPromoAmount())
            );
        }
    }

    private static void printGivenItems(List<PurchasedProduct> purchasedProducts) {
        System.out.println("=============증      정===============");
        for (PurchasedProduct product : purchasedProducts) {
            if (product.getGivenQuantity() > 0) {
                System.out.printf("%-10s %-10d\n", product.getName(), product.getGivenQuantity());
            }
        }
    }

    private static void printTotals(Receipt receipt) {
        System.out.println("====================================");
        System.out.printf("%-10s %-10s %-10s\n", "총구매액", formatCurrency(receipt.getTotalQuantity()), formatCurrency(receipt.getTotalAmount()));
        System.out.printf("%-10s\t\t\t%-10s\n", "행사할인", "-" + formatCurrency(receipt.getDiscountAmount()));
        System.out.printf("%-10s\t\t\t %-10s\n", "멤버십할인", "-" + formatCurrency(receipt.getMembershipDiscount()));
        System.out.printf("%-10s\t\t\t %-10s\n", "내실돈", formatCurrency(receipt.getFinalAmount()));
    }

    private static String formatCurrency(int amount) {
        return String.format("%,d", amount);
    }

}
