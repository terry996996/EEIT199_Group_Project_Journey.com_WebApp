package journey.repository.attractions;
import journey.domain.attractiontickets.TicketTypesBean;
import journey.domain.attractiontickets.TicketPackagesBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTypesRepository extends JpaRepository<TicketTypesBean, Integer> {

    // 根據套票查詢其下所有票種
    List<TicketTypesBean> findByTicketPackage(TicketPackagesBean ticketPackage);
   
    // 也可以加上：根據套票 ID 查詢（可避免傳整個 Entity）
    List<TicketTypesBean> findByTicketPackage_Id(Integer packageId);
}

