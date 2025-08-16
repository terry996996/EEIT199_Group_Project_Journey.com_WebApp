package journey.domain.flights;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.Table;
import journey.domain.plans.TripActivityBean;

@Entity
@Table(name = "flights")
public class FlightsBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id", nullable = false)
    private Integer flightId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_code", nullable = false)
    @JsonIgnoreProperties({ "flights" })
    private AirlinesBean airline;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport", nullable = false)
    @JsonIgnoreProperties({ "departureFlights", "arrivalFlights" })
    private AirportsBean departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport", nullable = false)
    @JsonIgnoreProperties({ "departureFlights", "arrivalFlights", })
    private AirportsBean arrivalAirport;

    @Column(name = "departure_time", nullable = false)
    @JsonFormat(pattern = "MM-dd HH:mm")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    @JsonFormat(pattern = "MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<FlightsDetailsBean> flightsDetails;

    @OneToMany(mappedBy = "flight")
    @JsonManagedReference
    private Set<TripActivityBean> tripActivities;

    @Column(name = "departure_terminal", nullable = false)
    private String departureTerminal;

    @Column(name = "arrival_terminal", nullable = false)
    private String arrivalTerminal;

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public AirlinesBean getAirline() {
        return airline;
    }

    public void setAirline(AirlinesBean airlinCode) {
        this.airline = airlinCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public AirportsBean getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(AirportsBean departureAirport) {
        this.departureAirport = departureAirport;
    }

    public AirportsBean getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(AirportsBean arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Set<FlightsDetailsBean> getFlightsDetails() {
        return flightsDetails;
    }

    public void setFlightsDetails(Set<FlightsDetailsBean> flightsDetails) {
        this.flightsDetails = flightsDetails;
    }

    public Set<TripActivityBean> getTripActivities() {
        return tripActivities;
    }

    public void setTripActivities(Set<TripActivityBean> tripActivities) {
        this.tripActivities = tripActivities;
    }

    @Override
    public String toString() {
        return "FlightsBean [flightId=" + flightId + ", airline=" + airline + ", flightNumber=" + flightNumber
                + ", departureAirport=" + departureAirport + ", arrivalAirport=" + arrivalAirport + ", departureTime="
                + departureTime + ", arrivalTime=" + arrivalTime + ", duration=" + duration + ", tripActivities="
                + tripActivities + ", departureTerminal=" + departureTerminal
                + ", arrivalTerminal=" + arrivalTerminal + "]";
    }

}