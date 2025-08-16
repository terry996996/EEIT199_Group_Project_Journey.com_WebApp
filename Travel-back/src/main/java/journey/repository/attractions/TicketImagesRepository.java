package journey.repository.attractions;

import journey.domain.attractiontickets.TicketImagesBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketImagesRepository extends JpaRepository<TicketImagesBean, Integer> {

    // 根據票券 ID 查詢圖片
    List<TicketImagesBean> findByTicketId(Integer ticketId);
    
     
}
