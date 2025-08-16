package journey.repository.flights;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.flights.AirlinesBean;

public interface AirlinesRepository extends JpaRepository<AirlinesBean, String> {

}
