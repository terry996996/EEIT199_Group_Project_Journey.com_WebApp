package journey.repository.flights;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import journey.domain.flights.FlightsDetailsBean;

public interface FlightsDetailsRepository extends JpaRepository<FlightsDetailsBean, Integer> {

        // @Query("SELECT fd FROM FlightsDetailsBean fd " +
        // "WHERE (:departure IS NULL OR " +
        // "LOWER(fd.flight.departureAirport.city.name) LIKE LOWER(CONCAT('%',
        // :departure, '%')) " +
        // "OR LOWER(fd.flight.departureAirport.city.nameZh) LIKE LOWER(CONCAT('%',
        // :departure, '%'))) " +
        // "AND (:destination IS NULL OR " +
        // "LOWER(fd.flight.arrivalAirport.city.name) LIKE LOWER(CONCAT('%',
        // :destination, '%')) " +
        // "OR LOWER(fd.flight.arrivalAirport.city.nameZh) LIKE LOWER(CONCAT('%',
        // :destination, '%'))) " +
        // "AND (:departDate IS NULL OR CAST(fd.flight.departureTime AS date) =
        // :departDate) " +
        // "AND (:cabinClass IS NULL OR fd.flightclass.classType = :cabinClass)")
        // List<FlightsDetailsBean> findByCriteria(@Param("departure") String departure,
        // @Param("destination") String destination,
        // @Param("departDate") LocalDate departDate,
        // @Param("cabinClass") String cabinClass);

        @Query("SELECT fd FROM FlightsDetailsBean fd " +
                        "WHERE (:departure IS NULL OR (" +
                        "LOWER(fd.flight.departureAirport.city.nameZh) LIKE LOWER(CONCAT('%', :departure, '%')))) " +
                        "AND (:destination IS NULL OR (" +
                        "LOWER(fd.flight.arrivalAirport.city.nameZh) LIKE LOWER(CONCAT('%', :destination, '%')))) " +
                        "AND (:departDate IS NULL OR CAST(fd.flight.departureTime AS date) = :departDate) " +
                        "AND (:cabinClass IS NULL OR fd.flightclass.classType = :cabinClass)")
        List<FlightsDetailsBean> findByCriteria(
                        @Param("departure") String departure,
                        @Param("destination") String destination,
                        @Param("departDate") LocalDate departDate,
                        @Param("cabinClass") String cabinClass);

}
