package journey.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.PartnerBean;
import journey.domain.users.UserBean;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerBean, Integer> {
    List<PartnerBean> findByRelatesTo(UserBean relatesTo);
}
