package journey.domain.payments;

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
import jakarta.persistence.Table;
import journey.domain.coupons.CouponInstancesBean;
import journey.domain.orders.AttractionTicketsOrderBean;
import journey.domain.orders.BookingOrdersBean;
import journey.domain.orders.FlightsOrdersBean;
import journey.domain.orders.TicketOrderBean;
import journey.domain.pointcards.PointCardsBean;
import journey.domain.pointcards.PointCardsStatusBean;
import journey.domain.pointcards.RewardsBean;
import journey.domain.pointcards.RewardsRecordBean;

// 付款紀錄資料表 (payments_record)

@Entity
@Table(name = "payments_record")
public class PaymentsRecordBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "pending";

    @Column(name = "transaction_number", unique = true, length = 500)
    private String transactionNumber;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime = LocalDateTime.now();

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    @JsonBackReference
    private TypeEnumBean type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_instance_id")
    @JsonBackReference
    private CouponInstancesBean couponInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_card_id")
    @JsonBackReference
    private PointCardsBean pointCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id")
    @JsonBackReference
    private RewardsBean reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_tool_id", nullable = false)
    @JsonBackReference
    private PaymentsBean paymentTool;

    @OneToMany(mappedBy = "paymentId")
    @JsonManagedReference
    private Set<FlightsOrdersBean> flightsOrders;

    @OneToMany(mappedBy = "paymentId")
    @JsonManagedReference
    private Set<TicketOrderBean> ticketOrders;

    @OneToMany(mappedBy = "paymentRecord")
    @JsonManagedReference
    private Set<BookingOrdersBean> bookingOrders;

    @OneToMany(mappedBy = "payment")
    @JsonManagedReference
    private Set<AttractionTicketsOrderBean> attractionTicketsOrders;

    @OneToMany(mappedBy = "payment")
    @JsonManagedReference
    private Set<PointCardsStatusBean> pointCardsStatus;

    @OneToMany(mappedBy = "payment")
    @JsonManagedReference
    private Set<RewardsRecordBean> rewardsRecords;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public TypeEnumBean getType() {
        return type;
    }

    public void setType(TypeEnumBean type) {
        this.type = type;
    }

    public CouponInstancesBean getCouponInstance() {
        return couponInstance;
    }

    public void setCouponInstance(CouponInstancesBean couponInstance) {
        this.couponInstance = couponInstance;
    }

    public PointCardsBean getPointCard() {
        return pointCard;
    }

    public void setPointCard(PointCardsBean pointCard) {
        this.pointCard = pointCard;
    }

    public RewardsBean getReward() {
        return reward;
    }

    public void setReward(RewardsBean reward) {
        this.reward = reward;
    }

    public PaymentsBean getPaymentTool() {
        return paymentTool;
    }

    public void setPaymentTool(PaymentsBean paymentTool) {
        this.paymentTool = paymentTool;
    }

    @Override
    public String toString() {
        return "PaymentsRecordBean [paymentId=" + paymentId + ", orderId=" + orderId + ", currency=" + currency
                + ", paymentStatus=" + paymentStatus + ", transactionNumber=" + transactionNumber + ", recordTime="
                + recordTime + ", note=" + note + ", type=" + type + ", couponInstance=" + couponInstance
                + ", pointCard=" + pointCard + ", reward=" + reward + ", paymentTool=" + paymentTool + "]";
    }

    public Set<FlightsOrdersBean> getFlightsOrders() {
        return flightsOrders;
    }

    public void setFlightsOrders(Set<FlightsOrdersBean> flightsOrders) {
        this.flightsOrders = flightsOrders;
    }

    public Set<TicketOrderBean> getTicketOrders() {
        return ticketOrders;
    }

    public void setTicketOrders(Set<TicketOrderBean> ticketOrders) {
        this.ticketOrders = ticketOrders;
    }

    public Set<BookingOrdersBean> getBookingOrders() {
        return bookingOrders;
    }

    public void setBookingOrders(Set<BookingOrdersBean> bookingOrders) {
        this.bookingOrders = bookingOrders;
    }

    public Set<AttractionTicketsOrderBean> getAttractionTicketsOrders() {
        return attractionTicketsOrders;
    }

    public void setAttractionTicketsOrders(Set<AttractionTicketsOrderBean> attractionTicketsOrders) {
        this.attractionTicketsOrders = attractionTicketsOrders;
    }

    public Set<PointCardsStatusBean> getPointCardsStatus() {
        return pointCardsStatus;
    }

    public void setPointCardsStatus(Set<PointCardsStatusBean> pointCardsStatus) {
        this.pointCardsStatus = pointCardsStatus;
    }

    public Set<RewardsRecordBean> getRewardsRecords() {
        return rewardsRecords;
    }

    public void setRewardsRecords(Set<RewardsRecordBean> rewardsRecords) {
        this.rewardsRecords = rewardsRecords;
    }

}
