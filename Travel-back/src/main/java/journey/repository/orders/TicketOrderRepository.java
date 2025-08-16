package journey.repository.orders;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.orders.TicketOrderBean;

public interface TicketOrderRepository extends JpaRepository<TicketOrderBean, Integer> {

    Optional<TicketOrderBean> findByOrderNo(String orderNo);
}
