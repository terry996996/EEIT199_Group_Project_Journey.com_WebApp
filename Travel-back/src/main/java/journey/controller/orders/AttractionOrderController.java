package journey.controller.orders;

import journey.domain.orders.AttractionTicketsOrderBean;
import journey.domain.plans.TripPlanBean;
import journey.domain.orders.AttractionTicketOrderItemsBean;
import journey.service.order.AttractionOrderService;
import journey.dto.attractions.OrderRequestDto;
import journey.dto.attractions.OrderResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import journey.repository.attractions.AttractionTicketRepository;
import journey.repository.attractions.TicketPackagesRepository;
import journey.repository.attractions.TicketTypesRepository;
import journey.repository.orders.AttractionTicketsOrderRepository;
import journey.repository.trip.TripPlanRepository;

import java.util.List;

@RestController
@RequestMapping("/api/attractions/order")
public class AttractionOrderController {

    @Autowired
    private AttractionOrderService attractionOrderService;

    @Autowired
    private TripPlanRepository tripPlanRepository;

    @Autowired
    private TicketPackagesRepository ticketPackagesRepository;

    @Autowired
    private TicketTypesRepository ticketTypesRepository;
    @Autowired
    private AttractionTicketRepository attractionTicketRepository;
    @Autowired
    private AttractionTicketsOrderRepository attractionTicketsOrderRepository;

    /**
     * 儲存主訂單與明細資料（新增訂單 + 購物車送出）
     */
    @PostMapping("/save") // 這個controller路徑裡的方法(line45~line76)不應該寫在這裡，應該要搬到payment(綠界、linepay)service裡面，前端呼叫第三方付款程式時，才會建訂單資料;
                          // 而且controller裡面不會寫這麼複雜的邏輯，這部分會直接寫在某個service裡面，然後controller的不同路徑裡面再去調這個方法來用
    public ResponseEntity<AttractionTicketsOrderBean> saveOrderWithItems(@RequestBody OrderRequestDto dto) {

        // 1. 建立主訂單 Entity 並設 trip 關聯
        // AttractionTicketsOrderBean order = new AttractionTicketsOrderBean();
        // order.setOrderNo(dto.getOrder().getOrderNO());
        // order.setStatus(dto.getOrder().getStatus());

        // TripPlanBean trip =
        // tripPlanRepository.findById(dto.getOrder().getTripId()).orElse(null);
        // if (trip == null) {
        // return ResponseEntity.badRequest().body(null);
        // }
        // order.setTrip(trip);

        // 2. 建立所有明細 Entity
        // List<AttractionTicketOrderItemsBean> items = dto.getItems().stream().map(i ->
        // {
        // AttractionTicketOrderItemsBean item = new AttractionTicketOrderItemsBean();
        // item.setQuantity(i.getQuantity());

        // item.setTicketPackage(ticketPackagesRepository.findById(i.getTicketPackageId()).orElse(null));
        // item.setAttraction(attractionTicketRepository.findById(i.getAttractionId()).orElse(null));
        // item.setOption(ticketTypesRepository.findById(i.getOptionId()).orElse(null));
        // item.setUnitPrice(i.getUnitPrice());
        // item.setUseDate(i.getUseDate());
        // return item;
        // }).toList();

        // 3. 呼叫 Service 儲存
        // AttractionTicketsOrderBean saved =
        // attractionOrderService.saveOrderWithItems(order, items);

        // return ResponseEntity.ok(saved);

        AttractionTicketsOrderBean emptyOrder = new AttractionTicketsOrderBean();
        return ResponseEntity.ok(emptyOrder); // 改傳空物件 不執行以上建訂單table的邏輯
    }

    /**
     * 查詢單筆主訂單
     */
    @GetMapping("/{orderNo}")
    public ResponseEntity<AttractionTicketsOrderBean> getOrderByOrderNo(@PathVariable String orderNo) {
        AttractionTicketsOrderBean order = attractionOrderService.getAttractionOrderByOrderNo(orderNo);
        return order == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }

    /**
     * 查詢主訂單下的明細
     */
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<AttractionTicketOrderItemsBean>> getItemsByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(attractionOrderService.getOrderItemsByOrderId(orderId));
    }

    /**
     * 查詢購物車（tripId 未付款）
     */
    @GetMapping("/cart/{tripId}")
    public ResponseEntity<List<AttractionTicketsOrderBean>> getCartOrdersByTripId(@PathVariable Integer tripId) {
        return ResponseEntity.ok(attractionOrderService.findCartOrdersByTripId(tripId));
    }

    @GetMapping("/full/{orderNo}")
    public ResponseEntity<OrderResponseDto> getFullOrderByOrderNo(@PathVariable String orderNo) {
        OrderResponseDto dto = attractionOrderService.getFullOrderByOrderNo(orderNo);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    // 購物車查詢
    @GetMapping("/full/by-trip/{tripId}")
    public ResponseEntity<List<OrderResponseDto>> getFullOrdersByTripId(@PathVariable Integer tripId) {
        List<OrderResponseDto> result = attractionOrderService.getFullOrdersByTripId(tripId);
        return ResponseEntity.ok(result);

    }

    // 刪除訂單
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        boolean deleted = attractionOrderService.deleteOrderById(orderId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}