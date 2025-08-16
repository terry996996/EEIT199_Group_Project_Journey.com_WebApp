package journey.dto.coupons;

import java.util.UUID;

public class CalculateDiscountRequestDTO {
    private UUID couponInstanceId;
    private int orderSubtotal;

    // Getters
    public UUID getCouponInstanceId() {
        return couponInstanceId;
    }

    public int getOrderSubtotal() {
        return orderSubtotal;
    }

    // Setters
    public void setCouponInstanceId(UUID couponInstanceId) {
        this.couponInstanceId = couponInstanceId;
    }

    public void setOrderSubtotal(int orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }
}
