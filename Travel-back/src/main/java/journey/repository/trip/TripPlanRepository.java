package journey.repository.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.plans.TripPlanBean;
import java.util.List;
import journey.domain.users.UserBean;

@Repository
public interface TripPlanRepository extends JpaRepository<TripPlanBean, Integer> {
    List<TripPlanBean> findByCreatedBy(UserBean createdBy);
}
