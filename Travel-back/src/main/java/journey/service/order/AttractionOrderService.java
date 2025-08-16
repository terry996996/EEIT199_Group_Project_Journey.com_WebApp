package journey.service.order;

import java.util.List;

import journey.domain.orders.AttractionTicketsOrderBean;
import journey.dto.attractions.OrderResponseDto;
import journey.domain.orders.AttractionTicketOrderItemsBean;

public interface AttractionOrderService {

    /**
     * 儲存景點訂單主表
     */
    AttractionTicketsOrderBean saveAttractionOrder(AttractionTicketsOrderBean order);

    /**
     * 根據訂單號查詢主表
     */
    AttractionTicketsOrderBean getAttractionOrderByOrderNo(String orderNo);

    /**
     * 同時儲存訂單主表與明細列表
     */
    AttractionTicketsOrderBean saveOrderWithItems(AttractionTicketsOrderBean order, List<AttractionTicketOrderItemsBean> items);

    /**
     * 根據訂單 ID 查詢所有明細
     */
    List<AttractionTicketOrderItemsBean> getOrderItemsByOrderId(Integer orderId);

    /**
     * 查詢指定使用者尚未付款的所有訂單（類似購物車）
     */
    List<AttractionTicketsOrderBean> findCartOrdersByTripId(Integer tripId);

    OrderResponseDto getFullOrderByOrderNo(String orderNo);

    List<OrderResponseDto> getFullOrdersByTripId(Integer tripId);

    //失傳資料
    public boolean deleteOrderById(Integer orderId);
}
