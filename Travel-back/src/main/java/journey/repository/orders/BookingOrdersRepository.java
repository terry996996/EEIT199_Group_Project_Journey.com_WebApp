package journey.repository.orders;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.orders.BookingOrdersBean;

public interface BookingOrdersRepository extends JpaRepository<BookingOrdersBean, Integer> {

    Optional<BookingOrdersBean> findByOrderNo(String orderNo);

}
