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
@Table(name = "flights_orders")
public class FlightsOrdersBean implements OrderEntityInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference // 07-13修正-原本沒加這段，會導致前端傳JSON資料寫進資料庫後，下次在到訂單填寫頁面時，整個爆炸
    private TripPlanBean tripId;

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    @JsonBackReference
    private PaymentsRecordBean paymentId;

    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true, referencedColumnName = "admin_id ")
    @JsonBackReference
    private AdminBean updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "orderId")
    @JsonManagedReference
    private Set<FlightOrderDetailsBean> flightOrderDetails;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public TripPlanBean getTripId() {
        return tripId;
    }

    public void setTripId(TripPlanBean tripId) {
        this.tripId = tripId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public PaymentsRecordBean getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(PaymentsRecordBean paymentId) {
        this.paymentId = paymentId;
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

    @Override
    public String toString() {
        return "FlightsOrdersBean [orderId=" + orderId + ", tripId=" + tripId + ", orderNo=" + orderNo
                + ", orderDate=" + orderDate + ", paymentId=" + paymentId + ", status=" + status + ", updatedBy="
                + updatedBy + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

    public Set<FlightOrderDetailsBean> getFlightOrderDetails() {
        return flightOrderDetails;
    }

    public void setFlightOrderDetails(Set<FlightOrderDetailsBean> flightOrderDetails) {
        this.flightOrderDetails = flightOrderDetails;
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
    }

    @Override
    public void setPaymentIdForPayment(PaymentsRecordBean paymentRecord) {
        this.paymentId = paymentRecord;
    }

}
