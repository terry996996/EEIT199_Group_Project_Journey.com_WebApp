package journey.service.order;

import java.util.Optional;

import journey.domain.orders.OrderEntityInterface;
import journey.domain.payments.PaymentsRecordBean;
import journey.dto.PaymentRequestInfo;

public interface OrderService {

    /**
     * 根據支付請求資訊獲取或創建一個訂單。
     * 如果訂單已存在且未支付，則返回該訂單；如果訂單已支付，則拋出異常；如果訂單不存在，則創建新訂單。
     *
     * @param requestInfo 包含訂單號、訂單類型、金額等資訊的支付請求 DTO
     * @return 創建或找到的訂單實體
     * @throws IllegalArgumentException 如果請求資訊無效
     * @throws IllegalStateException    如果訂單已支付
     * @throws RuntimeException         如果訂單創建失敗
     */
    OrderEntityInterface getOrCreateOrder(PaymentRequestInfo requestInfo);

    /**
     * 根據訂單號和訂單類型查找訂單。
     *
     * @param orderNo     訂單號
     * @param orderTypeId 訂單類型 ID
     * @return 找到的訂單實體，如果未找到則為 null
     */
    OrderEntityInterface findOrder(String orderNo, Integer orderTypeId);

    /**
     * 根據訂單號查找訂單。
     * 此方法將在 OrderService 的實現類中處理查找不同類型訂單的邏輯。
     *
     * @param orderNo 訂單號
     * @return 包含找到的訂單實體的 Optional，如果未找到則為 Optional.empty()
     */
    Optional<OrderEntityInterface> findOrderByOrderNo(String orderNo);

    /**
     * 保存訂單實體的更新。
     *
     * @param orderEntity 要保存的訂單實體
     */
    void saveOrder(OrderEntityInterface orderEntity);

    /**
     * 根據支付記錄的資訊查找對應的訂單並更新其狀態。
     * 這個方法通常在支付回調中被調用。
     *
     * @param paymentRecord 支付記錄實體，包含訂單相關資訊
     * @param statusString  要設置的訂單狀態字符串 (例如 "PAID", "FAILED")
     * @throws IllegalArgumentException 如果訂單類型無效
     * @throws RuntimeException         如果未找到對應訂單或更新失敗
     */
    void updateOrderStatusFromPayment(PaymentsRecordBean paymentRecord, String statusString);
}