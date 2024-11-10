package store.util;

public enum ErrorMessage {
    INVALID_INPUT_FORMAT_MESSAGE("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_PRODUCT_NAME_MESSAGE("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    STOCK_LIMIT_EXCEEDED_MESSAGE("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    DEFAULT_ERROR_MESSAGE("잘못된 입력입니다. 다시 입력해 주세요."),
    NULL_OR_EMPTY_MESSAGE("입력값이 비어 있습니다.");

    private final String message;
    private static final String PREFIX = "[ERROR] ";

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
