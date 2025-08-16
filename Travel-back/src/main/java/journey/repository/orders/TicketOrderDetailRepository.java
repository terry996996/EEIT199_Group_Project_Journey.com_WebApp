package journey.repository.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.orders.TicketOrderDetailBean;

public interface TicketOrderDetailRepository extends JpaRepository<TicketOrderDetailBean, Integer> {
        List<TicketOrderDetailBean> findByTicketorder_OrderId(Integer orderId);
}
