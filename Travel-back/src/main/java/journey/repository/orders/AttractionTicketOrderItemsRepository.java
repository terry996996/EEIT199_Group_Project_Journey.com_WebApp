package journey.repository.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.orders.AttractionTicketOrderItemsBean;

@Repository
public interface AttractionTicketOrderItemsRepository extends JpaRepository<AttractionTicketOrderItemsBean, Integer> {
    List<AttractionTicketOrderItemsBean> findByOrder_OrderId(Integer orderId);
}
