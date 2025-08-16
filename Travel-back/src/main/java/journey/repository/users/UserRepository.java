package journey.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.UserBean;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Integer> {
    Optional<UserBean> findById(Integer userId);
}
