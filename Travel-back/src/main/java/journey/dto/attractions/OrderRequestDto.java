package journey.dto.attractions;

import java.util.List;

public class OrderRequestDto {

    private OrderDto order;
    private List<OrderItemDto> items;

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
