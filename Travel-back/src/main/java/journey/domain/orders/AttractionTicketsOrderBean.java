package journey.domain.orders;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import journey.domain.payments.PaymentsRecordBean;
import journey.domain.plans.TripPlanBean;
import journey.domain.users.AdminBean;

@Entity
@Table(name = "attraction_ticket_orders")
public class AttractionTicketsOrderBean implements OrderEntityInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonBackReference
    private TripPlanBean trip;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonBackReference
    private PaymentsRecordBean payment;

    @Column(name = "order_no", nullable = false, unique = true, length = 100)
    private String orderNo;
    // 可不加ManyToOne 因為單純記錄 如果修改者有需要查詢資料再ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private AdminBean updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems;

    public AttractionTicketsOrderBean() {
    }

    public AttractionTicketsOrderBean(Integer orderId,
            TripPlanBean trip, Boolean status,
            PaymentsRecordBean payment, String orderNo,
            AdminBean updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.trip = trip;
        this.status = status;
        this.payment = payment;
        this.orderNo = orderNo;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public TripPlanBean getTrip() {
        return trip;
    }

    public void setTrip(TripPlanBean trip) {
        this.trip = trip;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public PaymentsRecordBean getPayment() {
        return payment;
    }

    public void setPayment(PaymentsRecordBean payment) {
        this.payment = payment;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public AdminBean getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AdminBean updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<AttractionTicketOrderItemsBean> getAttractionTicketOrderItems() {
        return attractionTicketOrderItems;
    }

    public void setAttractionTicketOrderItems(Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems) {
        this.attractionTicketOrderItems = attractionTicketOrderItems;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null)
            this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Integer getOrderIdForPayment() {
        return this.orderId;
    }

    @Override
    public String getOrderNoForPayment() {
        return this.orderNo; // 假設 BookingOrdersBean 有一個名為 orderNo 的 String 屬性
    }

    @Override
    public Boolean getStatusForPayment() {
        return this.status; // 假設 BookingOrdersBean 有一個名為 status 的 Boolean 屬性
    }

    @Override
    public void setStatusForPayment(String statusString) {
        if ("PAID".equalsIgnoreCase(statusString)) {
            this.status = true; // 將 Boolean 類型的 status 設為 true
        } else {
            this.status = false; // 將 Boolean 類型的 status 設為 false (例如 PENDING_PAYMENT 或 FAILED)
        }
        // 您也可以在這裡更新 updatedAt 字段：
        // this.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public void setPaymentIdForPayment(PaymentsRecordBean paymentRecord) {
        this.payment = paymentRecord;
    }

}
