package journey.repository.flights;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.flights.AirportsBean;

public interface AirportsRepository extends JpaRepository<AirportsBean, String> {

}
