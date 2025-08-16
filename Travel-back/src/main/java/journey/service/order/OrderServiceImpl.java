package journey.service.order;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.coupons.CouponInstancesBean;
import journey.domain.orders.AttractionTicketsOrderBean;
import journey.domain.orders.BookingOrdersBean;
import journey.domain.orders.FlightsOrdersBean;
import journey.domain.orders.OrderEntityInterface;
import journey.domain.orders.TicketOrderBean;
import journey.domain.payments.PaymentsRecordBean;
import journey.domain.plans.TripPlanBean;
import journey.dto.PaymentRequestInfo;
import journey.repository.coupons.CouponInstancesRepository;
import journey.repository.orders.AttractionTicketsOrderRepository;
import journey.repository.orders.BookingOrdersRepository;
import journey.repository.orders.FlightsOrdersRepository;
import journey.repository.orders.TicketOrderRepository;
import journey.repository.trip.TripPlanRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BookingOrdersRepository bookingOrdersRepository;
    @Autowired
    private FlightsOrdersRepository flightsOrdersRepository;
    @Autowired
    private TicketOrderRepository ticketOrderRepository;
    @Autowired
    private AttractionTicketsOrderRepository attractionTicketsOrderRepository;
    @Autowired
    private TripPlanRepository tripPlanRepository;
    @Autowired
    private CouponInstancesRepository couponInstancesRepository;

    /**
     * 根據支付請求資訊獲取或創建一個訂單。 *(主要是用id來判斷是哪一個類型的訂單，然後根據那種訂單來引入他的order_id)
     * 如果訂單已存在且未支付，則返回該訂單；如果訂單已支付，則拋出異常；如果訂單不存在，則創建新訂單。
     *
     * @param requestInfo 包含訂單號、訂單類型、金額等資訊的支付請求 DTO
     * @return 創建或找到的訂單實體
     * @throws IllegalArgumentException 如果請求資訊無效 (例如 orderNo 為空或 orderTypeId 無效)
     * @throws IllegalStateException    如果訂單已支付
     * @throws RuntimeException         如果訂單創建失敗或發生其他未預期錯誤
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 確保訂單創建在一個新事務中立即提交
    public OrderEntityInterface getOrCreateOrder(PaymentRequestInfo requestInfo) {
        Integer orderTypeId = requestInfo.getOrderTypeId();
        String orderNo = requestInfo.getOrderNo();

        if (orderTypeId == null) {
            throw new IllegalArgumentException("orderTypeId 不得為空。");
        }
        if (orderNo == null || orderNo.isEmpty()) {
            throw new IllegalArgumentException("orderNo 不得為空。");
        }

        Optional<? extends OrderEntityInterface> existingOrderOpt;
        switch (orderTypeId) {
            case 1: // 飯店訂單
                existingOrderOpt = bookingOrdersRepository.findByOrderNo(orderNo);
                break;
            case 2: // 機票訂單
                existingOrderOpt = flightsOrdersRepository.findByOrderNo(orderNo);
                break;
            case 3: // 交通票券訂單
                existingOrderOpt = ticketOrderRepository.findByOrderNo(orderNo);
                break;
            case 4: // 景點票券訂單
                existingOrderOpt = attractionTicketsOrderRepository.findByOrderNo(orderNo);
                break;
            default:
                throw new IllegalArgumentException("不支援的訂單類型 ID: " + orderTypeId);
        }

        if (existingOrderOpt.isPresent()) {
            OrderEntityInterface existingOrder = existingOrderOpt.get();
            System.out.println("OrderService: 找到現有訂單: " + existingOrder.getOrderNoForPayment() + " (ID: "
                    + existingOrder.getOrderIdForPayment() + ")");
            // *使用 getStatusForPayment() 判斷訂單是否已支付
            if (Boolean.TRUE.equals(existingOrder.getStatusForPayment())) {
                System.out.println("OrderService: 訂單 " + existingOrder.getOrderNoForPayment() + " 已支付，無需重新創建支付。");
                throw new IllegalStateException("訂單 " + existingOrder.getOrderNoForPayment() + " 已支付，請勿重複付款。");
            }
            return existingOrder;
        } else {
            System.out.println("OrderService: 未找到訂單 " + orderNo + " (類型ID: " + orderTypeId + ")，正在創建新訂單...");
            OrderEntityInterface newOrder;
            LocalDateTime now = LocalDateTime.now();

            Integer tripIdFromRequest = requestInfo.getTripId();
            if (tripIdFromRequest == null && (orderTypeId == 2 || orderTypeId == 3 || orderTypeId == 4)) {
                throw new IllegalArgumentException("訂單類型 " + orderTypeId + " 必須包含有效的行程計畫 ID (tripId)。");
            }

            TripPlanBean tripPlan = null;
            if (tripIdFromRequest != null) {
                tripPlan = tripPlanRepository.findById(tripIdFromRequest)
                        .orElseThrow(() -> new IllegalArgumentException("找不到對應的行程計畫 ID: " + tripIdFromRequest));
            }

            switch (orderTypeId) {
                case 1: // 飯店訂單 (BookingOrdersBean)
                    BookingOrdersBean bookingOrder = new BookingOrdersBean();
                    bookingOrder.setOrderNo(orderNo);
                    bookingOrder.setCreatedAt(now);
                    bookingOrder.setUpdatedAt(now);
                    bookingOrder.setStatus(false);
                    if (tripPlan == null) {
                        throw new IllegalArgumentException("機票訂單需要有效的行程計畫。");
                    }
                    bookingOrder.setTripPlan(tripPlan);
                    newOrder = bookingOrdersRepository.save(bookingOrder);
                    break;
                case 2: // 機票訂單 (FlightsOrdersBean)
                    FlightsOrdersBean flightsOrder = new FlightsOrdersBean();
                    flightsOrder.setOrderNo(orderNo);
                    flightsOrder.setCreatedAt(now);
                    flightsOrder.setUpdatedAt(now);
                    flightsOrder.setOrderDate(now);
                    flightsOrder.setStatus(false);
                    if (tripPlan == null) {
                        throw new IllegalArgumentException("機票訂單需要有效的行程計畫。");
                    }
                    flightsOrder.setTripId(tripPlan);
                    newOrder = flightsOrdersRepository.save(flightsOrder);
                    break;
                case 3: // 交通票訂單 (TicketOrderBean)
                    TicketOrderBean ticketOrder = new TicketOrderBean();
                    ticketOrder.setOrderNo(orderNo);
                    ticketOrder.setCreatedAt(now);
                    ticketOrder.setUpdatedAt(now);
                    ticketOrder.setOrderDate(now);
                    ticketOrder.setStatus(false);
                    if (tripPlan == null) {
                        throw new IllegalArgumentException("交通票訂單需要有效的行程計畫。");
                    }
                    ticketOrder.setTripplan(tripPlan);
                    newOrder = ticketOrderRepository.save(ticketOrder);
                    break;
                case 4: // 景點票訂單 (AttractionTicketsOrderBean)
                    AttractionTicketsOrderBean attractionOrder = new AttractionTicketsOrderBean();
                    attractionOrder.setOrderNo(orderNo);
                    attractionOrder.setCreatedAt(now);
                    attractionOrder.setUpdatedAt(now);
                    attractionOrder.setStatus(false);
                    if (tripPlan == null) {
                        throw new IllegalArgumentException("景點門票訂單需要有效的行程計畫。");
                    }
                    attractionOrder.setTrip(tripPlan);
                    newOrder = attractionTicketsOrderRepository.save(attractionOrder);
                    break;
                default:
                    throw new IllegalArgumentException("不支援的訂單類型 ID: " + orderTypeId);
            }
            if (newOrder == null || newOrder.getOrderIdForPayment() == null) {
                throw new RuntimeException("無法成功創建訂單，請檢查資料庫操作。");
            }
            System.out.println("OrderService: 新訂單已創建並保存: " + newOrder.getOrderNoForPayment() + " (ID: "
                    + newOrder.getOrderIdForPayment() + ")");
            return newOrder;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderEntityInterface findOrder(String orderNo, Integer orderTypeId) {
        if (orderNo == null || orderNo.isEmpty() || orderTypeId == null) {
            return null;
        }
        switch (orderTypeId) {
            case 1:
                return bookingOrdersRepository.findByOrderNo(orderNo).orElse(null);
            case 2:
                return flightsOrdersRepository.findByOrderNo(orderNo).orElse(null);
            case 3:
                return ticketOrderRepository.findByOrderNo(orderNo).orElse(null);
            case 4:
                return attractionTicketsOrderRepository.findByOrderNo(orderNo).orElse(null);
            default:
                System.err.println("OrderService: findOrder - 不支援的訂單類型 ID: " + orderTypeId);
                return null;
        }
    }

    /**
     * 根據訂單號查找訂單。
     * 此方法將處理查找不同類型訂單的邏輯。
     *
     * @param orderNo 訂單號
     * @return 包含找到的訂單實體的 Optional，如果未找到則為 Optional.empty()
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderEntityInterface> findOrderByOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            return Optional.empty();
        }

        // 嘗試從所有可能的訂單 Repository 中查找
        // 查找順序可以根據您的業務邏輯或訂單號的命名規則來優化
        Optional<BookingOrdersBean> bookingOrderOpt = bookingOrdersRepository.findByOrderNo(orderNo);
        if (bookingOrderOpt.isPresent()) {
            return bookingOrderOpt.map(order -> order);
        }

        Optional<FlightsOrdersBean> flightsOrderOpt = flightsOrdersRepository.findByOrderNo(orderNo);
        if (flightsOrderOpt.isPresent()) {
            return flightsOrderOpt.map(order -> order);
        }

        Optional<TicketOrderBean> ticketOrderOpt = ticketOrderRepository.findByOrderNo(orderNo);
        if (ticketOrderOpt.isPresent()) {
            return ticketOrderOpt.map(order -> order);
        }

        Optional<AttractionTicketsOrderBean> attractionOrderOpt = attractionTicketsOrderRepository
                .findByOrderNo(orderNo);
        if (attractionOrderOpt.isPresent()) {
            return attractionOrderOpt.map(order -> order);
        }

        // 如果所有 Repository 都沒有找到，則返回空的 Optional
        return Optional.empty();
    }

    /**
     * 輔助方法(非常核心*****)：根據訂單ID和訂單類型查找訂單。*(主要是用id來判斷是哪一個類型的訂單，然後根據那種訂單來引入他的order_id)
     *
     * @param orderId
     * @param orderTypeId
     * @return 找到的訂單實體，如果未找到則為 null
     */
    @Transactional(readOnly = true)
    private OrderEntityInterface findOrderByIdAndType(Integer orderId, Integer orderTypeId) {
        if (orderId == null || orderTypeId == null) {
            return null;
        }
        Optional<? extends OrderEntityInterface> result = Optional.empty();
        switch (orderTypeId) {
            case 1:
                result = bookingOrdersRepository.findById(orderId);
                break;
            case 2:
                result = flightsOrdersRepository.findById(orderId);
                break;
            case 3:
                result = ticketOrderRepository.findById(orderId);
                break;
            case 4:
                result = attractionTicketsOrderRepository.findById(orderId);
                break;
            default:
                break;
        }
        return result.orElse(null);
    }

    @Override
    @Transactional
    public void saveOrder(OrderEntityInterface orderEntity) {
        if (orderEntity == null) {
            throw new IllegalArgumentException("要保存的訂單實體不能為空。");
        }
        if (orderEntity instanceof BookingOrdersBean) {
            bookingOrdersRepository.saveAndFlush((BookingOrdersBean) orderEntity);
        } else if (orderEntity instanceof FlightsOrdersBean) {
            flightsOrdersRepository.saveAndFlush((FlightsOrdersBean) orderEntity);
        } else if (orderEntity instanceof TicketOrderBean) {
            ticketOrderRepository.saveAndFlush((TicketOrderBean) orderEntity);
        } else if (orderEntity instanceof AttractionTicketsOrderBean) {
            attractionTicketsOrderRepository.saveAndFlush((AttractionTicketsOrderBean) orderEntity);
        } else {
            System.err.println("OrderService: 無法保存未知類型的訂單實體: " + orderEntity.getClass().getName());
        }
    }

    /**
     * 根據支付記錄的資訊查找對應的訂單並更新其狀態。
     * 這個方法通常在支付回調中被調用。
     *
     * @param paymentRecord 支付記錄實體，包含訂單相關資訊 (orderId, typeId)
     * @param statusString  要設置的訂單狀態字符串 (例如 "PAID", "FAILED")
     * @throws IllegalArgumentException 如果訂單類型無效
     * @throws RuntimeException         如果未找到對應訂單或更新失敗
     */
    @Override
    @Transactional
    public void updateOrderStatusFromPayment(PaymentsRecordBean paymentRecord, String statusString) {
        if (paymentRecord == null || paymentRecord.getOrderId() == null || paymentRecord.getType() == null
                || paymentRecord.getType().getTypeId() == null) {
            System.err.println("OrderService: updateOrderStatusFromPayment - 支付記錄或其訂單資訊不完整，無法更新訂單狀態。");
            return;
        }

        Integer orderId = paymentRecord.getOrderId();
        Integer orderTypeId = paymentRecord.getType().getTypeId();

        OrderEntityInterface order = findOrderByIdAndType(orderId, orderTypeId);

        if (order != null) {
            order.setStatusForPayment(statusString);

            LocalDateTime now = LocalDateTime.now();

            if (order instanceof BookingOrdersBean) {
                BookingOrdersBean bookingOrder = (BookingOrdersBean) order;
                bookingOrder.setPaymentRecord(paymentRecord);
                bookingOrder.setUpdatedAt(now);
            } else if (order instanceof FlightsOrdersBean) {
                FlightsOrdersBean flightsOrder = (FlightsOrdersBean) order;
                flightsOrder.setPaymentId(paymentRecord);
                flightsOrder.setUpdatedAt(now);
            } else if (order instanceof TicketOrderBean) {
                TicketOrderBean ticketOrder = (TicketOrderBean) order;
                ticketOrder.setPaymentId(paymentRecord);
                ticketOrder.setUpdatedAt(now);
            } else if (order instanceof AttractionTicketsOrderBean) {
                AttractionTicketsOrderBean attractionOrder = (AttractionTicketsOrderBean) order;
                attractionOrder.setPayment(paymentRecord);
                attractionOrder.setUpdatedAt(now);
            }

            // 更新coupon_instances table當中use_at欄位的值(改成當前按下付款的時間，改變折價券的狀態)
            if ("PAID".equals(statusString) && paymentRecord.getCouponInstance() != null) {
                CouponInstancesBean usedCoupon = paymentRecord.getCouponInstance();
                // 檢查 useAt 是否已設定，避免重複更新
                if (usedCoupon.getUseAt() == null) {
                    usedCoupon.setUseAt(now); // 設定使用時間為當前時間
                    couponInstancesRepository.save(usedCoupon); // 儲存更新後的折價券實例
                    System.out.println(
                            "OrderService: 折價券 " + usedCoupon.getCouponInstanceId() + " 已更新使用時間為 " + now + "。");
                } else {
                    System.out.println("OrderService: 折價券 " + usedCoupon.getCouponInstanceId() + " 已被使用，無需重複更新。");
                }
            }

            saveOrder(order);
            System.out.println(
                    "OrderService: 訂單 " + order.getOrderNoForPayment() + " (ID: " + orderId + ", 類型: " + orderTypeId
                            + ") 狀態已更新為 " + statusString + "。");
        } else {
            System.err.println("OrderService: 未找到對應訂單 (ID: " + orderId + ", 類型ID: " + orderTypeId + ")，無法更新狀態。");
        }
    }
}