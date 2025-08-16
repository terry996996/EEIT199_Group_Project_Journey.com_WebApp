package journey.dto;

import java.util.UUID;

public class PaymentRequestInfo {
    private Long orderId;
    private String orderNo;
    private Integer totalAmount;
    private String paymentMethod;
    private String description;
    private Integer orderTypeId;
    private Integer tripId;
    private UUID selectedCouponInstanceId;

    public PaymentRequestInfo() {
    }

    public PaymentRequestInfo(Long orderId, String orderNo, Integer totalAmount, String paymentMethod,
            String description, Integer orderTypeId, Integer tripId, UUID selectedCouponInstanceId) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.orderTypeId = orderTypeId;
        this.tripId = tripId;
        this.selectedCouponInstanceId = selectedCouponInstanceId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(Integer orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public UUID getSelectedCouponInstanceId() {
        return selectedCouponInstanceId;
    }

    public void setSelectedCouponInstanceId(UUID selectedCouponInstanceId) {
        this.selectedCouponInstanceId = selectedCouponInstanceId;
    }

    @Override
    public String toString() {
        return "PaymentRequestInfo [orderId=" + orderId + ", orderNo=" + orderNo + ", totalAmount=" + totalAmount
                + ", paymentMethod=" + paymentMethod + ", description=" + description + ", orderTypeId=" + orderTypeId
                + ", tripId=" + tripId + ", selectedCouponInstanceId=" + selectedCouponInstanceId + "]";
    }

}