package journey.repository.flights;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.flights.FlightClassesBean;

public interface FlightClassesRepository extends JpaRepository<FlightClassesBean, Integer> {

}
