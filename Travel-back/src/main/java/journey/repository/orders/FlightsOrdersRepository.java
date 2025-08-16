package journey.repository.orders;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.orders.FlightsOrdersBean;

public interface FlightsOrdersRepository extends JpaRepository<FlightsOrdersBean, Integer> {

    /**
     * 根據訂單號查詢機票訂單。
     * 
     * @param order_no 訂單號
     * @return 匹配的機票訂單，如果不存在則為 Optional.empty()
     */
    Optional<FlightsOrdersBean> findByOrderNo(String orderNo);

}
