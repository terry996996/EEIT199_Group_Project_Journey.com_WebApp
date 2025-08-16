package journey.repository.users;

/**
 * 商家資料存取層
 * 
 * @author Max
 * @since 2025-07-08
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.VendorBean;

@Repository
public interface VendorRepository extends JpaRepository<VendorBean, Integer> {

}
