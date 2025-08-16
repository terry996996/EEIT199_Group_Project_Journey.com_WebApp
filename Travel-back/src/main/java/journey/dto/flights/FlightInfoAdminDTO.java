package journey.dto.flights;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "flightId", "flightNumber",
        "departureCountryNameZh", "departureAirportName", "departureAirportCode", "departureTerminal", "departureTime",
        "arrivalCountryNameZh", "arrivalAirportName", "arrivalAirportCode", "arrivalTerminal", "arrivalTime",
        "classPrice", "classCapacity", "classAvailable", "baggageAllowance",
        "createdAt", "updatedAt"
})
public class FlightInfoAdminDTO {
    private Integer flightId;
    private String flightNumber;

    private String departureCountryNameZh;
    private String departureAirportName;
    private String departureAirportCode;
    private String departureTerminal;
    private LocalDateTime departureTime;

    private String arrivalCountryNameZh;
    private String arrivalAirportName;
    private String arrivalAirportCode;
    private String arrivalTerminal;
    private LocalDateTime arrivalTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer classPrice;
    private Integer classCapacity;
    private Integer classAvailable;
    private Integer baggageAllowance;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getDepartureCountryNameZh() {
        return departureCountryNameZh;
    }

    public void setDepartureCountryNameZh(String departureCountryNameZh) {
        this.departureCountryNameZh = departureCountryNameZh;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getArrivalCountryNameZh() {
        return arrivalCountryNameZh;
    }

    public void setArrivalCountryNameZh(String arrivalCountryNameZh) {
        this.arrivalCountryNameZh = arrivalCountryNameZh;
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

}
