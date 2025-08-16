package journey.repository.flights;

import java.time.LocalDateTime;

public interface FlightInfoAdminView {
    Integer getFlightId();

    String getFlightNumber();

    String getDepartureCountryNameZh();

    String getDepartureAirportName();

    String getDepartureAirportCode();

    String getDepartureTerminal();

    LocalDateTime getDepartureTime();

    String getArrivalCountryNameZh();

    String getArrivalAirportName();

    String getArrivalAirportCode();

    String getArrivalTerminal();

    LocalDateTime getArrivalTime();

    Integer getClassPrice();

    Integer getClassCapacity();

    Integer getClassAvailable();

    Integer getBaggageAllowance();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
