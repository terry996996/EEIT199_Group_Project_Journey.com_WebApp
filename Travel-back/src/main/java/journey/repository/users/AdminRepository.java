package journey.repository.users;

/**
 * 管理員資料存取層
 * 
 * @author Max
 * @since 2025-07-08
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.AdminBean;

@Repository
public interface AdminRepository extends JpaRepository<AdminBean, Integer> {

}
