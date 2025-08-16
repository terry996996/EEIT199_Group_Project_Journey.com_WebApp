package journey.repository.lodgings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.lodgings.RoomAvailabilityBean;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailabilityBean, Integer> {

}
