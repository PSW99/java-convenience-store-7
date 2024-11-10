package store.util;

import java.util.regex.Pattern;

public class InputValidator {
    private static final String ORDER_PATTERN = "^\\[(\\p{L}+)-(\\d+)](,\\[(\\p{L}+)-(\\d+)])*$";

    public static void validateYesNoInput(String input) {
        isNullOrEmpty(input);
        if (!(input.equals(YesNoOption.YES) || input.equals(YesNoOption.NO))) {
            throw new IllegalArgumentException(ErrorMessage.DEFAULT_ERROR_MESSAGE.getMessage());
        }
    }

    public static void validateOrderInput(String input) {
        isNullOrEmpty(input);
        Pattern pattern = Pattern.compile(ORDER_PATTERN);
        if (!pattern.matcher(input).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT_MESSAGE.getMessage());
        }
    }

    private static void isNullOrEmpty(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NULL_OR_EMPTY_MESSAGE.getMessage());
        }
    }
}
