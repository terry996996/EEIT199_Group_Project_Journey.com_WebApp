package journey.repository.users;

/**
 * 商家狀態資料存取層
 * 
 * @author Max
 * @since 2025-07-08
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.users.VendorStatusBean;

@Repository
public interface VendorStatus extends JpaRepository<VendorStatusBean, Integer> {

}
