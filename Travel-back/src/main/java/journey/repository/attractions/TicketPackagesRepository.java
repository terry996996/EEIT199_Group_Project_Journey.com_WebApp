package journey.repository.attractions;

import journey.domain.attractiontickets.TicketPackagesBean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketPackagesRepository extends JpaRepository<TicketPackagesBean, Integer> {

    // 查詢某票券下的所有套票
    List<TicketPackagesBean> findByTicket_Id(Integer ticketId);

}
