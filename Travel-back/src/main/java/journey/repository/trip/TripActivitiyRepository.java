package journey.repository.trip;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.plans.TripActivityBean;
import journey.domain.plans.TripDayBean;

@Repository
public interface TripActivitiyRepository extends JpaRepository<TripActivityBean, Integer> {
    List<TripActivityBean> findByDayId(TripDayBean dayId);
}
