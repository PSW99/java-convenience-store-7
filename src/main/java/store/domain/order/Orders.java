package store.domain.order;

import java.util.List;

public record Orders(List<Order> orders) {
    @Override
    public List<Order> orders() {
        return orders;
    }
}
