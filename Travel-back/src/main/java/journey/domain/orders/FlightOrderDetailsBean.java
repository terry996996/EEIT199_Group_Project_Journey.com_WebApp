package journey.domain.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import journey.domain.flights.FlightsDetailsBean;
import journey.domain.users.PartnerBean;

@Entity
@Table(name = "flight_order_details")
public class FlightOrderDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id", nullable = false)
    private Integer orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private FlightsOrdersBean orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flights_details_id", nullable = false)
    @JsonBackReference
    private FlightsDetailsBean flightsDetailsId;

    @Column(name = "passenger_type", nullable = false)
    private String passengerType;

    @Column(name = "seat_number", length = 10)
    private String seatNumber;

    @Column(name = "baggage_weight", nullable = false)
    private Integer baggageWeight;

    @Column(name = "baggage_fee", nullable = false)
    private Integer baggageFee;

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    @JsonBackReference
    private PartnerBean partnerId;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public FlightsOrdersBean getOrderId() {
        return orderId;
    }

    public void setOrderId(FlightsOrdersBean orderId) {
        this.orderId = orderId;
    }

    public FlightsDetailsBean getFlightsDetailsId() {
        return flightsDetailsId;
    }

    public void setFlightsDetailsId(FlightsDetailsBean flightsDetailsId) {
        this.flightsDetailsId = flightsDetailsId;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getBaggageWeight() {
        return baggageWeight;
    }

    public void setBaggageWeight(Integer baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    public Integer getBaggageFee() {
        return baggageFee;
    }

    public void setBaggageFee(Integer baggageFee) {
        this.baggageFee = baggageFee;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public PartnerBean getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(PartnerBean partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String toString() {
        return "FlightOrderDetailsBean [orderDetailId=" + orderDetailId + ", orderId=" + orderId + ", flightsDetailsId="
                + flightsDetailsId + ", passengerType=" + passengerType + ", seatNumber=" + seatNumber
                + ", baggageWeight=" + baggageWeight + ", baggageFee=" + baggageFee + ", price=" + price
                + ", partnerId=" + partnerId + "]";
    }

    @PrePersist
    public void prePersist() {
        if (this.baggageWeight == null)
            this.baggageWeight = 0;
        if (this.baggageFee == null)
            this.baggageFee = 0;
    }
}
