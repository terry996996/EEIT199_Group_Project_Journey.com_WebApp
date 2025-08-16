package journey.dto.flights;

public class FlightDetailsRequestDTO {
    private Integer flightId;
    private Integer classId;
    private Integer classPrice;
    private Integer classCapacity;
    private Integer baggageAllowance;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
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

    public Integer getBaggageAllowance() {
        return baggageAllowance;
    }

    public void setBaggageAllowance(Integer baggageAllowance) {
        this.baggageAllowance = baggageAllowance;
    }
}
