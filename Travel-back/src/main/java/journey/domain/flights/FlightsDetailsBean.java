package journey.domain.flights;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "flights_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "flight_id", "class_id" })
})
public class FlightsDetailsBean {

    @Id
    @Column(name = "flights_details_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flightsDetailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    @JsonIgnoreProperties({ "flightsDetails" })
    private FlightsBean flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    @JsonIgnoreProperties({ "flightDetails" })
    private FlightClassesBean flightclass;

    @Column(name = "class_price", nullable = false)
    private Integer classPrice;

    @Column(name = "class_capacity", nullable = false)
    private Integer classCapacity;

    @Column(name = "class_available", nullable = false)
    private Integer classAvailable;

    @Column(name = "baggage_allowance", nullable = false)
    private Integer baggageAllowance;

    // @OneToMany(mappedBy = "flightsDetailsId")
    // @JsonManagedReference
    // private Set<FlightsDetailsBean> flightsDetails;

    public Integer getFlightsDetailsId() {
        return flightsDetailsId;
    }

    public void setFlightsDetailsId(Integer flightsDetailsId) {
        this.flightsDetailsId = flightsDetailsId;
    }

    public FlightsBean getFlight() {
        return flight;
    }

    public void setFlight(FlightsBean flightId) {
        this.flight = flightId;
    }

    public FlightClassesBean getFlightclass() {
        return flightclass;
    }

    public void setFlightclass(FlightClassesBean classId) {
        this.flightclass = classId;
    }

    public Integer getClassPrice() {
        return classPrice;
    }

    public void setClassPrice(Integer classPrice) {
        this.classPrice = classPrice;
    }

    public Integer getClassCapacity() {
        return classCapacity;
    }

    public void setClassCapacity(Integer classCapacity) {
        this.classCapacity = classCapacity;
    }

    public Integer getClassAvailable() {
        return classAvailable;
    }

    public void setClassAvailable(Integer classAvailable) {
        this.classAvailable = classAvailable;
    }

    public Integer getBaggageAllowance() {
        return baggageAllowance;
    }

    public void setBaggageAllowance(Integer baggageAllowance) {
        this.baggageAllowance = baggageAllowance;
    }

    @Override
    public String toString() {
        return "FlightsDetailsBean [flightsDetailsId=" + flightsDetailsId + ", flightId=" + flight + ", classId="
                + flightclass + ", classPrice=" + classPrice + ", classCapacity=" + classCapacity + ", classAvailable="
                + classAvailable + ", baggageAllowance=" + baggageAllowance + "]";
    }

    // public Set<FlightsDetailsBean> getFlightsDetails() {
    // return flightsDetails;
    // }

    // public void setFlightsDetails(Set<FlightsDetailsBean> flightsDetails) {
    // this.flightsDetails = flightsDetails;
    // }

}
