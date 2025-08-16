package journey.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.AllUserBean;

@Repository
public interface AllUserRepository extends JpaRepository<AllUserBean, Integer> {
    Optional<AllUserBean> findByUsername(String username);

    Optional<AllUserBean> findByEmail(String email);

}
