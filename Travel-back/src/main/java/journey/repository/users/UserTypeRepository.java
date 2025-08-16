package journey.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.UserTypeBean;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeBean, Integer> {

}
