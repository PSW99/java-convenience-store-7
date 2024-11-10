package store.util;

import store.domain.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String DATE_REGEX = "-";

    private Parser() {
    }

    public static Integer convertStringToInteger(String target) {
        return Integer.parseInt(target);
    }

    public static LocalDateTime convertStringToLocalDate(String target) {
        String[] values = target.split(DATE_REGEX);
        int year = convertStringToInteger(values[0]);
        int month = convertStringToInteger(values[1]);
        int day = convertStringToInteger(values[2]);
        int hour = 0;
        int minute = 0;

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public static List<Order> convertStringToOrders(String target) {
        List<Order> orders = new ArrayList<>();

        String[] items = target.split("],\\[");
        for (String item : items) {
            item = item.replaceAll("[\\[\\]]", "");
            String[] productDetails = item.split("-");
            orders.add(new Order(productDetails[0], convertStringToInteger(productDetails[1])));
        }

        return orders;
    }
}
