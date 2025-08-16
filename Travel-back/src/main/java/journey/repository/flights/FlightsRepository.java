package journey.repository.flights;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import journey.domain.flights.FlightsBean;

public interface FlightsRepository extends JpaRepository<FlightsBean, Integer> {

    // 後端使用者查詢航班所有資訊
    @Query(value = """
            SELECT
                f.flight_id AS flightId,
                f.flight_number AS flightNumber,
                dc.name_zh AS departureCountryNameZh,
                da.airport_name AS departureAirportName,
                da.airport_code AS departureAirportCode,
                f.departure_terminal AS departureTerminal,
                f.departure_time AS departureTime,
                ac.name_zh AS arrivalCountryNameZh,
                aa.airport_name AS arrivalAirportName,
                aa.airport_code AS arrivalAirportCode,
                f.arrival_terminal AS arrivalTerminal,
                f.arrival_time AS arrivalTime,
                d.class_price AS classPrice,
                d.class_capacity AS classCapacity,
                d.class_available AS classAvailable,
                d.baggage_allowance AS baggageAllowance,
                f.created_at AS createdAt,
                f.updated_at AS updatedAt
            FROM flights f
            JOIN airports da ON f.departure_airport = da.airport_code
            JOIN countries dc ON da.country_id = dc.id
            JOIN airports aa ON f.arrival_airport = aa.airport_code
            JOIN countries ac ON aa.country_id = ac.id
            JOIN flights_details d ON f.flight_id = d.flight_id
            """, nativeQuery = true)
    List<FlightInfoAdminView> findAllFlightInfoForAdmin();

}
