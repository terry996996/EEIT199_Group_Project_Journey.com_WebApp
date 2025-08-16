package journey.service.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.domain.orders.AttractionTicketsOrderBean;
import journey.dto.attractions.OrderItemResponseDto;
import journey.dto.attractions.OrderResponseDto;
import journey.domain.orders.AttractionTicketOrderItemsBean;
import journey.repository.orders.AttractionTicketsOrderRepository;
import journey.repository.attractions.TicketPackagesRepository;
import journey.repository.orders.AttractionTicketOrderItemsRepository;

/**
 * 景點票券訂單服務實作。
 * 提供訂單主表與明細的儲存與查詢功能。
 */
@Service
public class AttractionOrderServiceImpl implements AttractionOrderService {

    @Autowired
    private AttractionTicketsOrderRepository attractionTicketsOrderRepository;

    @Autowired
    private AttractionTicketOrderItemsRepository itemsRepository;

    @Autowired
    private TicketPackagesRepository ticketPackagesRepository;

    /**
     * 儲存景點訂單主表。
     * 
     * @param order 主訂單物件
     * @return 儲存後的主訂單
     */
    @Override
    public AttractionTicketsOrderBean saveAttractionOrder(AttractionTicketsOrderBean order) {
        if (order == null) {
            throw new IllegalArgumentException("訂單資料不能為 null");
        }
        return attractionTicketsOrderRepository.save(order);
    }

    /**
     * 根據訂單號碼查詢主訂單。
     * 
     * @param orderNo 訂單號碼
     * @return 查詢結果（如無資料回傳 null）
     */
    @Override
    public AttractionTicketsOrderBean getAttractionOrderByOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            throw new IllegalArgumentException("訂單號碼不能為空");
        }
        Optional<AttractionTicketsOrderBean> orderOpt = attractionTicketsOrderRepository.findByOrderNo(orderNo);
        return orderOpt.orElse(null);
    }

    /**
     * 同時儲存訂單主表與多筆明細。
     * 
     * @param order 主訂單
     * @param items 明細列表
     * @return 儲存後的主訂單
     */
    @Override
    public AttractionTicketsOrderBean saveOrderWithItems(AttractionTicketsOrderBean order,
            List<AttractionTicketOrderItemsBean> items) {
        if (order == null || items == null) {
            throw new IllegalArgumentException("訂單或明細不能為 null");
        }

        // 無論有沒有傳入，強制保證訂單編號唯一
        String orderNo = order.getOrderNo();
        if (orderNo == null || orderNo.isEmpty()
                || attractionTicketsOrderRepository.findByOrderNo(orderNo).isPresent()) {
            // 迴圈保證唯一
            do {
                orderNo = generateUniqueOrderNo();
            } while (attractionTicketsOrderRepository.findByOrderNo(orderNo).isPresent());
            order.setOrderNo(orderNo);
        }

        // 儲存主訂單
        AttractionTicketsOrderBean savedOrder = attractionTicketsOrderRepository.save(order);

        // 每個明細綁定主表
        for (AttractionTicketOrderItemsBean item : items) {
            item.setOrder(savedOrder);
        }
        itemsRepository.saveAll(items);

        return savedOrder;
    }

    /**
     * 自動產生唯一訂單編號（格式：AyyyyMMddHHmmssSSS）
     */
    private String generateUniqueOrderNo() {
        return "A" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    /**
     * 查詢主訂單下的所有明細項目。
     * 
     * @param orderId 主訂單 ID
     * @return 明細列表
     */
    @Override
    public List<AttractionTicketOrderItemsBean> getOrderItemsByOrderId(Integer orderId) {
        return itemsRepository.findByOrder_OrderId(orderId);
    }

    /**
     * 查詢指定 tripId 的所有未付款訂單（購物車）。
     * 
     * @param tripId 行程規劃 ID
     * @return 該行程的未付款訂單列表
     */
    @Override
    public List<AttractionTicketsOrderBean> findCartOrdersByTripId(Integer tripId) {
        return attractionTicketsOrderRepository.findByTrip_Id(tripId);
    }

    @Override
    public OrderResponseDto getFullOrderByOrderNo(String orderNo) {
        AttractionTicketsOrderBean order = getAttractionOrderByOrderNo(orderNo);
        if (order == null)
            return null;

        List<AttractionTicketOrderItemsBean> items = getOrderItemsByOrderId(order.getOrderId());
        return convertToResponseDto(order, items);
    }

    public OrderResponseDto convertToResponseDto(AttractionTicketsOrderBean order,
            List<AttractionTicketOrderItemsBean> items) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderNo(order.getOrderNo());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemResponseDto> itemDtos = items.stream().map(item -> {
            OrderItemResponseDto d = new OrderItemResponseDto();
            d.setItemId(item.getItemId());
            d.setQuantity(item.getQuantity());
            d.setUnitPrice(item.getUnitPrice());
            d.setUseDate(item.getUseDate());

            d.setAttractionId(item.getAttraction().getId());
            d.setAttractionName(item.getAttraction().getName());

            d.setTicketPackageId(item.getTicketPackage().getId());
            d.setTicketPackageName(item.getTicketPackage().getName());
            Integer pkgId = item.getTicketPackage().getId();
            ticketPackagesRepository.findById(pkgId).ifPresent(pkg -> {
                d.setImageUrl(pkg.getImageUrl()); // ✅ 設定 imageUrl
            });
            d.setOptionId(item.getOption().getId());
            d.setOptionName(item.getOption().getTicketName());

            return d;
        }).toList();

        dto.setItems(itemDtos);
        return dto;
    }

    // 購物車查詢
    @Override
    public List<OrderResponseDto> getFullOrdersByTripId(Integer tripId) {
        List<AttractionTicketsOrderBean> orders = attractionTicketsOrderRepository.findByTrip_Id(tripId);

        return orders.stream().map(order -> {
            List<AttractionTicketOrderItemsBean> items = getOrderItemsByOrderId(order.getOrderId());
            return convertToResponseDto(order, items);
        }).toList();
    }

    // 取消訂單
    @Override
    public boolean deleteOrderById(Integer orderId) {
        if (orderId == null)
            return false;

        // 先刪除明細（避免 FK 限制）
        List<AttractionTicketOrderItemsBean> items = itemsRepository.findByOrder_OrderId(orderId);
        itemsRepository.deleteAll(items);

        // 再刪除主訂單
        attractionTicketsOrderRepository.deleteById(orderId);
        return true;
    }

}
