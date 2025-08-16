package journey.repository.attractions;

import journey.domain.attractiontickets.TicketFaqsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketFaqsRepository extends JpaRepository<TicketFaqsBean, Integer> {

    // 根據票券 ID 查詢對應的常見問題
    List<TicketFaqsBean> findByTicketId(Integer ticketId);
}
