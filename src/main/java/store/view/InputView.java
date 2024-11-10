package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.order.Orders;
import store.util.InputValidator;
import store.util.Parser;
import store.util.YesNoOption;

public class InputView {

    private static final String ITEM_SELECTION_MESSAGE = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String PROMOTION_ELIGIBILITY_MESSAGE = "\n현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String PROMOTION_STOCK_INSUFFICIENT_MESSAGE = "\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String APPLY_MEMBERSHIP_DISCOUNT_MESSAGE = "\n멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ASK_ADDITIONAL_PURCHASE_MESSAGE = "\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    public static Orders readOrders() {
        return handleInputWithRetry(() -> {
            String inputOrder = readOrder();
            return new Orders(Parser.convertStringToOrders(inputOrder));
        });
    }

    public static boolean confirmAdditionalPurchase() {
        return handleInputWithRetry(() -> {
            String response = readYesNoInput(ASK_ADDITIONAL_PURCHASE_MESSAGE);
            return YesNoOption.YES.equals(response);
        });
    }

    public static boolean askForMembershipDiscount() {
        return handleInputWithRetry(() -> {
            String response = readYesNoInput(APPLY_MEMBERSHIP_DISCOUNT_MESSAGE);
            return YesNoOption.YES.equals(response);
        });
    }

    public static boolean askForNonPromotionalPurchase(String productName, int quantity) {
        return handleInputWithRetry(() -> {
            String response = readYesNoInput(PROMOTION_STOCK_INSUFFICIENT_MESSAGE, productName, quantity);
            return YesNoOption.YES.equals(response);
        });
    }

    public static boolean askForAdditionalFreeItem(String productName) {
        return handleInputWithRetry(() -> {
            String response = readYesNoInput(PROMOTION_ELIGIBILITY_MESSAGE, productName);
            return YesNoOption.YES.equals(response);
        });
    }

    private static String readOrder() {
        System.out.println(ITEM_SELECTION_MESSAGE);
        String input = Console.readLine();
        InputValidator.validateOrderInput(input);
        return input;
    }

    private static String readYesNoInput(String message, Object... args) {
        System.out.printf(message + "%n", args);
        String input = Console.readLine();
        InputValidator.validateYesNoInput(input);
        return input;
    }

    private static <T> T handleInputWithRetry(InputSupplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    @FunctionalInterface
    private interface InputSupplier<T> {
        T get();
    }
}
