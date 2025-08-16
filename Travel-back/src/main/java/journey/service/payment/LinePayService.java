package journey.service.payment;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import journey.domain.coupons.CouponInstancesBean;
import journey.domain.orders.AttractionTicketOrderItemsBean;
import journey.domain.orders.AttractionTicketsOrderBean;
import journey.domain.orders.BookingOrderItemsBean;
import journey.domain.orders.BookingOrdersBean;
import journey.domain.orders.FlightOrderDetailsBean;
import journey.domain.orders.FlightsOrdersBean;
import journey.domain.orders.OrderEntityInterface;
import journey.domain.orders.TicketOrderBean;
import journey.domain.payments.PaymentsBean;
import journey.domain.payments.PaymentsRecordBean;
import journey.domain.payments.TypeEnumBean;
import journey.dto.PaymentRequestInfo;
import journey.dto.Linepay.LinePayConfirmRequest;
import journey.dto.Linepay.LinePayConfirmResponse;
import journey.dto.Linepay.LinePayRequest;
import journey.dto.Linepay.LinePayResponse;
import journey.repository.coupons.CouponInstancesRepository;
import journey.repository.orders.AttractionTicketsOrderRepository;
import journey.repository.orders.BookingOrdersRepository;
import journey.repository.orders.FlightsOrdersRepository;
import journey.repository.orders.TicketOrderRepository;
import journey.repository.payments.PaymentsRecordRepository;
import journey.repository.payments.PaymentsRepository;
import journey.repository.payments.TypeEnumRepository;
import journey.service.coupons.CouponService;
import journey.service.order.OrderService;
import journey.service.pointcards.PointCardsService;

@Service
public class LinePayService {

    // 從組態檔撈token資料進來
    @Value("${linepay.channel-id}")
    private String channelId;
    @Value("${linepay.channel-secret}")
    private String channelSecret;
    @Value("${linepay.api.base-url}")
    private String linePayApiBaseUrl;
    @Value("${linepay.confirm-url}")
    private String confirmUrl;
    @Value("${linepay.cancel-url}")
    private String cancelUrl;
    @Value("${linepay.return-url}")
    private String frontendReturnUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PaymentsRecordRepository paymentsRecordRepository;
    private final PaymentsRepository paymentsRepository;
    private final TypeEnumRepository typeEnumRepository;

    private final TicketOrderRepository ticketOrderRepository;
    private final BookingOrdersRepository bookingOrdersRepository;
    private final AttractionTicketsOrderRepository attractionTicketsOrderRepository;
    private final FlightsOrdersRepository flightsOrdersRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponInstancesRepository couponInstancesRepository;

    @Autowired
    private PointCardsService pointCardsService;

    private static final Logger logger = Logger.getLogger(LinePayService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @FunctionalInterface
    private interface OrderLookupStrategy {
        Optional<? extends OrderEntityInterface> findOrderByOrderNo(String orderNo);
    }

    private Map<Integer, OrderLookupStrategy> orderLookupStrategies;

    public LinePayService(RestTemplate restTemplate, ObjectMapper objectMapper,
            PaymentsRecordRepository paymentsRecordRepository,
            PaymentsRepository paymentsRepository,
            TypeEnumRepository typeEnumRepository,
            TicketOrderRepository ticketOrderRepository,
            BookingOrdersRepository bookingOrdersRepository,
            AttractionTicketsOrderRepository attractionTicketsOrderRepository,
            FlightsOrdersRepository flightsOrdersRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.paymentsRecordRepository = paymentsRecordRepository;
        this.paymentsRepository = paymentsRepository;
        this.typeEnumRepository = typeEnumRepository;
        this.ticketOrderRepository = ticketOrderRepository;
        this.bookingOrdersRepository = bookingOrdersRepository;
        this.attractionTicketsOrderRepository = attractionTicketsOrderRepository;
        this.flightsOrdersRepository = flightsOrdersRepository;
    }

    @PostConstruct
    public void initOrderLookupStrategies() {
        orderLookupStrategies = new HashMap<>();
        orderLookupStrategies.put(1, orderNo -> bookingOrdersRepository.findByOrderNo(orderNo));
        orderLookupStrategies.put(2, orderNo -> flightsOrdersRepository.findByOrderNo(orderNo));
        orderLookupStrategies.put(3, orderNo -> ticketOrderRepository.findByOrderNo(orderNo));
        orderLookupStrategies.put(4, orderNo -> attractionTicketsOrderRepository.findByOrderNo(orderNo));
    }

    @Transactional
    public LinePayResponse initiatePayment(PaymentRequestInfo orderInfo, OrderEntityInterface orderEntity)
            throws Exception {
        String productDescription = orderInfo.getDescription() != null ? orderInfo.getDescription() : "線上訂單";

        LinePayRequest.Product product = new LinePayRequest.Product(
                productDescription,
                1,
                orderInfo.getTotalAmount().intValue(),
                null);

        LinePayRequest.RedirectUrls urls = new LinePayRequest.RedirectUrls(
                confirmUrl,
                cancelUrl);

        LinePayRequest request = new LinePayRequest(
                productDescription,
                orderInfo.getTotalAmount().intValue(),
                "TWD",
                orderInfo.getOrderNo(),
                Collections.singletonList(new LinePayRequest.Package(
                        "package-" + orderInfo.getOrderNo(),
                        productDescription,
                        orderInfo.getTotalAmount().intValue(),
                        Collections.singletonList(product))),
                urls);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", channelId);

        String nonce = UUID.randomUUID().toString();
        headers.set("X-LINE-Authorization-Nonce", nonce);

        String requestBodyJson = objectMapper.writeValueAsString(request);

        String signature = generateSignature("/v3/payments/request", nonce, requestBodyJson);
        headers.set("X-LINE-Authorization", signature);

        HttpEntity<LinePayRequest> entity = new HttpEntity<>(request, headers);

        String requestUrl = linePayApiBaseUrl + "/v3/payments/request";
        ResponseEntity<LinePayResponse> responseEntity = restTemplate.postForEntity(requestUrl, entity,
                LinePayResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            LinePayResponse linePayResponse = responseEntity.getBody();
            if (linePayResponse != null && "0000".equals(linePayResponse.getReturnCode())) {
                PaymentsRecordBean paymentsRecord = new PaymentsRecordBean();

                paymentsRecord.setOrderId(orderEntity.getOrderIdForPayment());

                paymentsRecord.setTransactionNumber(linePayResponse.getInfo().getTransactionId());

                paymentsRecord.setCurrency(request.getCurrency());

                PaymentsBean linePayPaymentTool = paymentsRepository.findByPaymentTool("line_pay")
                        .orElseThrow(() -> new RuntimeException(
                                "無法找到支付工具 'line_pay'。請確保資料庫中存在 PaymentsBean (payment_tool='line_pay') 的紀錄。"));
                paymentsRecord.setPaymentTool(linePayPaymentTool);

                Integer orderTypeId = orderInfo.getOrderTypeId(); // 從 orderInfo 獲取 TypeId
                TypeEnumBean orderType = typeEnumRepository.findByTypeId(orderTypeId)
                        .orElseThrow(() -> new RuntimeException("無法找到 ID 為 " + orderTypeId
                                + " 的訂單類型。請確保資料庫中存在 TypeEnumBean (type_id=" + orderTypeId + ") 的紀錄。"));
                paymentsRecord.setType(orderType);

                paymentsRecord.setPaymentStatus("PENDING");
                paymentsRecord.setRecordTime(LocalDateTime.now());

                // 如果 orderInfo 包含 couponId，則設置到 paymentsRecord
                if (orderInfo.getSelectedCouponInstanceId() != null) {
                    // 根據 UUID 查詢出 CouponInstancesBean 物件
                    CouponInstancesBean coupon = couponInstancesRepository
                            .findById(orderInfo.getSelectedCouponInstanceId())
                            .orElseThrow(() -> new RuntimeException(
                                    "找不到 ID 為 " + orderInfo.getSelectedCouponInstanceId() + " 的優惠券實例。"));
                    paymentsRecord.setCouponInstance(coupon);
                }

                paymentsRecordRepository.save(paymentsRecord);
                paymentsRecordRepository.flush();

                // 調用 OrderService 更新訂單的 paymentId 和 updatedAt
                orderService.updateOrderStatusFromPayment(paymentsRecord, "PENDING");

                return linePayResponse;
            } else {
                throw new Exception("LINE Pay request failed: "
                        + (linePayResponse != null ? linePayResponse.getReturnMessage() : "null response body"));
            }
        } else {
            throw new Exception("LINE Pay API call failed with status: " + responseEntity.getStatusCode());
        }
    }

    @Transactional
    public LinePayConfirmResponse confirmPayment(String transactionId, String orderNoFromCallback) throws Exception {

        PaymentsBean linePayPaymentTool = paymentsRepository.findByPaymentTool("line_pay")
                .orElseThrow(() -> new RuntimeException("無法找到支付工具 'line_pay'。"));

        PaymentsRecordBean paymentsRecord = paymentsRecordRepository
                .findByTransactionNumber(transactionId)
                .filter(record -> record.getPaymentTool().getPaymentToolId()
                        .equals(linePayPaymentTool.getPaymentToolId()))
                .orElseThrow(() -> new RuntimeException(
                        "Payment record not found for transactionId: " + transactionId
                                + " with LINEPAY tool."));

        int amount;
        amount = 2820;

        LinePayConfirmRequest request = new LinePayConfirmRequest(amount, "TWD");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", channelId);

        String nonce = UUID.randomUUID().toString();
        headers.set("X-LINE-Authorization-Nonce", nonce);

        String requestBodyJson = objectMapper.writeValueAsString(request);

        String signature = generateSignature("/v3/payments/" + transactionId + "/confirm", nonce, requestBodyJson);
        headers.set("X-LINE-Authorization", signature);

        HttpEntity<LinePayConfirmRequest> entity = new HttpEntity<>(request, headers);

        String requestUrl = linePayApiBaseUrl + "/v3/payments/" + transactionId + "/confirm";
        ResponseEntity<LinePayConfirmResponse> responseEntity = restTemplate.postForEntity(requestUrl, entity,
                LinePayConfirmResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            LinePayConfirmResponse confirmResponse = responseEntity.getBody();
            if (confirmResponse != null && "0000".equals(confirmResponse.getReturnCode())) {
                paymentsRecord.setPaymentStatus("PAID");
                paymentsRecord.setRecordTime(LocalDateTime.now());
                paymentsRecordRepository.save(paymentsRecord);

                // 這邊先設計，付款成功時，會給2000點的點數交易回饋
                try {
                    pointCardsService.addPoints(
                            1,
                            2000,
                            "交易回饋",
                            paymentsRecord.getPaymentId());
                } catch (Exception e) {
                    System.err.println("加點失敗：" + e.getMessage());
                }

                // 如果付款成功，且 paymentRecord 中有記錄優惠券，則將優惠券標記為已使用
                if (paymentsRecord.getNote() != null && paymentsRecord.getNote().contains("Used Coupon UUID:")) {
                    try {
                        String note = paymentsRecord.getNote();
                        String uuidString = note
                                .substring(note.indexOf("Used Coupon UUID:") + "Used Coupon UUID:".length()).trim();
                        UUID couponInstanceId = UUID.fromString(uuidString);
                        couponService.markCouponAsUsed(couponInstanceId);
                    } catch (Exception e) {
                        System.err.println("Error marking coupon as used: " + e.getMessage());
                    }
                }

                // **統一使用 OrderService 更新訂單狀態，並處理 paymentId 和 updatedAt**
                orderService.updateOrderStatusFromPayment(paymentsRecord, "PAID");

                return confirmResponse;
            } else {
                paymentsRecord.setPaymentStatus("FAILED");
                paymentsRecordRepository.save(paymentsRecord);

                // **統一使用 OrderService 更新訂單狀態，並處理 paymentId 和 updatedAt**
                orderService.updateOrderStatusFromPayment(paymentsRecord, "FAILED");

                throw new Exception("LINE Pay confirm failed: "
                        + (confirmResponse != null ? confirmResponse.getReturnMessage() : "null response body"));
            }
        } else {
            throw new Exception("LINE Pay confirm API call failed with status: " + responseEntity.getStatusCode());
        }
    }

    // --- 輔助計算總金額的方法，已根據明細資料表資訊更新 ---
    private int calculateBookingOrderTotalAmount(BookingOrdersBean order) {
        if (order.getBookingOrderItems() == null || order.getBookingOrderItems().isEmpty()) {
            return 0;
        }
        int totalAmount = 0;
        for (BookingOrderItemsBean item : order.getBookingOrderItems()) {
            long nights = ChronoUnit.DAYS.between(item.getCheckInDate(), item.getCheckOutDate());
            totalAmount += item.getPricePerNight() * nights;
        }
        return totalAmount;
    }

    private int calculateFlightsOrderTotalAmount(FlightsOrdersBean order) {
        if (order.getFlightOrderDetails() == null || order.getFlightOrderDetails().isEmpty()) {
            return 0;
        }
        return order.getFlightOrderDetails().stream()
                .mapToInt(FlightOrderDetailsBean::getPrice)
                .sum();
    }

    private int calculateTicketOrderTotalAmount(TicketOrderBean order) {
        if (order.getDetails() == null || order.getDetails().isEmpty()) {
            return 0;
        }
        return order.getDetails().stream()
                .mapToInt(detail -> detail.getUnitPrice() * detail.getQuantity())
                .sum();
    }

    private int calculateAttractionTicketsOrderTotalAmount(AttractionTicketsOrderBean order) {
        if (order.getAttractionTicketOrderItems() == null || order.getAttractionTicketOrderItems().isEmpty()) {
            return 0;
        }
        int totalAmount = 0;
        for (AttractionTicketOrderItemsBean item : order.getAttractionTicketOrderItems()) {
            if (item.getOption() != null) { // 確保 option 不為空
                // 從 item 關聯的 TicketTypesBean (option) 中獲取 price 和 quantity
                totalAmount += item.getOption().getPrice() * item.getOption().getQuantity();
            } else {
            }
        }
        return totalAmount;
    }

    // --- 簽名生成方法 ---
    private String generateSignature(String uri, String nonce, String requestBody) throws Exception {
        String message = channelSecret + uri + requestBody + nonce;

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(channelSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(hash);
    }
}