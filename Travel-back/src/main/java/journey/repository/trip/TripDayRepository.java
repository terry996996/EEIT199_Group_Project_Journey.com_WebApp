package journey.repository.trip;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.plans.TripDayBean;
import journey.domain.plans.TripPlanBean;

@Repository
public interface TripDayRepository extends JpaRepository<TripDayBean, Integer> {
    List<TripDayBean> findByTripId(TripPlanBean tripId);
}
