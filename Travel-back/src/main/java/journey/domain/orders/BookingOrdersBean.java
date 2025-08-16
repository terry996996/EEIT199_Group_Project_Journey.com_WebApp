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
@Table(name = "booking_orders")
public class BookingOrdersBean implements OrderEntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer orderId;

    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @Column(name = "order_no", nullable = false, length = 300)
    private String orderNo;

    @Column(name = "created_at") // 對應table
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false, referencedColumnName = "id") // 資料表是Not NULL
    @JsonBackReference
    private TripPlanBean tripPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id") // 我刪掉 insertable = false, updatable = false
                                                                          // 不建議加這個，可能會導致CRUD有問題
    @JsonBackReference
    private PaymentsRecordBean paymentRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "admin_id")
    @JsonBackReference
    private AdminBean updatedBy;

    @OneToMany(mappedBy = "bookingOrder")
    @JsonManagedReference
    private Set<BookingOrderItemsBean> bookingOrderItems;

    // constructors
    public BookingOrdersBean() {

    }

    public BookingOrdersBean(Integer orderId, Boolean status, String orderNo,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            TripPlanBean tripPlan, PaymentsRecordBean paymentRecord,
            AdminBean updatedBy) {
        this.status = status;
        this.orderNo = orderNo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tripPlan = tripPlan;
        this.paymentRecord = paymentRecord;
        this.updatedBy = updatedBy;
    }

    // getters and setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public TripPlanBean getTripPlan() {
        return tripPlan;
    }

    public void setTripPlan(TripPlanBean tripPlan) {
        this.tripPlan = tripPlan;
    }

    public PaymentsRecordBean getPaymentRecord() {
        return paymentRecord;
    }

    public void setPaymentRecord(PaymentsRecordBean paymentRecord) {
        this.paymentRecord = paymentRecord;
    }

    public AdminBean getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AdminBean updatedBy) {
        this.updatedBy = updatedBy;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Set<BookingOrderItemsBean> getBookingOrderItems() {
        return bookingOrderItems;
    }

    public void setBookingOrderItems(Set<BookingOrderItemsBean> bookingOrderItems) {
        this.bookingOrderItems = bookingOrderItems;
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
        this.paymentRecord = paymentRecord; // 設定關聯對象
    }
}
