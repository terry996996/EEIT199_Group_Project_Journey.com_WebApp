package journey.domain.orders;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
@Table(name = "ticket_order")
public class TicketOrderBean implements OrderEntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private TripPlanBean tripplan;

    @Column(name = "order_no", nullable = false, unique = true, columnDefinition = "nvarchar(255)")
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    @JsonBackReference
    private PaymentsRecordBean paymentId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true, referencedColumnName = "admin_id")
    @JsonBackReference
    private AdminBean updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ticketorder", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketOrderDetailBean> details;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public PaymentsRecordBean getPaymentsRecord() {
        return paymentId;
    }

    public void setPaymentsRecord(PaymentsRecordBean paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public PaymentsRecordBean getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(PaymentsRecordBean paymentId) {
        this.paymentId = paymentId;
    }

    public Set<TicketOrderDetailBean> getDetails() {
        return details;
    }

    public void setDetails(Set<TicketOrderDetailBean> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "TicketOrderBean [orderId=" + orderId + ", tripId=" + tripplan + ", orderNo=" + orderNo + ", paymentId="
                + paymentId + ", orderDate=" + orderDate + ", status=" + status + ", updatedBy=" + updatedBy
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

    @PrePersist
    public void prePersist() {
        if (this.orderDate == null)
            this.orderDate = LocalDateTime.now();
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
        this.paymentId = paymentRecord;
    }

    public TripPlanBean getTripplan() {
        return tripplan;
    }

    public void setTripplan(TripPlanBean tripplan) {
        this.tripplan = tripplan;
    }

}
