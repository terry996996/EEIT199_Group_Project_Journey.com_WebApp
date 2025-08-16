package journey.repository.orders;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.orders.AttractionTicketsOrderBean;

@Repository
public interface AttractionTicketsOrderRepository extends JpaRepository<AttractionTicketsOrderBean, Integer> {

    /**
     * 根據訂單號查詢景點門票訂單。
     * 訂單號通常是唯一的業務標識符。
     * 
     * @param orderNo 訂單號
     * @return 匹配的景點門票訂單，如果不存在則為 Optional.empty()
     */
    Optional<AttractionTicketsOrderBean> findByOrderNo(String orderNo);

    List<AttractionTicketsOrderBean> findByTrip_Id(Integer tripId);

    

}
