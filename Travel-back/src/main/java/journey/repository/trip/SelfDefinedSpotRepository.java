package journey.repository.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.plans.SelfDefinedSpotBean;
import java.util.List;
import journey.domain.users.UserBean;

@Repository
public interface SelfDefinedSpotRepository extends JpaRepository<SelfDefinedSpotBean, Integer> {
    List<SelfDefinedSpotBean> findByCreatedBy(UserBean createdBy);
}
