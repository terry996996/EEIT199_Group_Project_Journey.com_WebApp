package journey.repository.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.orders.BookingOrderItemsBean;
import journey.domain.orders.BookingOrdersBean;

public interface BookingOrderItemsRepository extends JpaRepository<BookingOrderItemsBean, Integer> {

    // 可依 bookingId 查詢明細清單O
    List<BookingOrderItemsBean> findByBookingOrder(BookingOrdersBean bookingOrder);

    // 可依 roomTypeId 查詢明細清單
    List<BookingOrderItemsBean> findByRoomTypeId(Integer roomTypeId);
}