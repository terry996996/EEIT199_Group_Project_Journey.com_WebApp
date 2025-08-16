package journey.repository.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.locations.ContinentBean;

@Repository
public interface ContinentRepository extends JpaRepository<ContinentBean, String> {

}
