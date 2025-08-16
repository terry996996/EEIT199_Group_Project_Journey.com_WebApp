package journey.repository.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.orders.FlightOrderDetailsBean;

@Repository
public interface FlightOrderDetailsRepository extends JpaRepository<FlightOrderDetailsBean, Integer> {
    List<FlightOrderDetailsBean> findByOrderId_OrderId(Integer orderId);

}
