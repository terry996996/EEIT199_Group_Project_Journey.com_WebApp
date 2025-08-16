package journey.service.payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.coupons.CouponInstancesBean;
import journey.domain.orders.OrderEntityInterface;
import journey.domain.payments.PaymentsBean;
import journey.domain.payments.PaymentsRecordBean;
import journey.domain.payments.TypeEnumBean;
import journey.dto.PaymentRequestInfo;
import journey.repository.coupons.CouponInstancesRepository;
import journey.repository.payments.PaymentsRecordRepository;
import journey.repository.payments.PaymentsRepository;
import journey.repository.payments.TypeEnumRepository;
import journey.service.coupons.CouponService;
import journey.service.order.OrderService;
import journey.service.pointcards.PointCardsService;

@Service
public class EcpayService {

    private static final String LOCAL_URL = "http://localhost:8080";

    @Autowired
    private PaymentsRecordRepository paymentsRecordRepository;

    @Autowired
    private EcpayCheckMacValue ecpayCheckMacValue;

    @Autowired
    private TypeEnumRepository typeEnumRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private CouponInstancesRepository couponInstancesRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private PointCardsService pointCardsService;

    public Map<String, String> generateEcpayForm(PaymentRequestInfo orderInfo, OrderEntityInterface orderEntity) {
        Map<String, String> map = new HashMap<>();

        String generatedOrderNo = "ORDER" + System.currentTimeMillis();
        orderInfo.setOrderNo(generatedOrderNo);
        System.out.println("產生新的 MerchantTradeNo: " + generatedOrderNo);

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        PaymentsRecordBean paymentsRecord = new PaymentsRecordBean();
        paymentsRecord.setOrderId(orderEntity.getOrderIdForPayment());
        paymentsRecord.setCurrency("TWD");
        paymentsRecord.setPaymentStatus("PENDING");
        paymentsRecord.setRecordTime(LocalDateTime.now());
        paymentsRecord.setTransactionNumber(generatedOrderNo);

        TypeEnumBean orderType = typeEnumRepository.findByTypeId(orderInfo.getOrderTypeId())
                .orElseThrow(() -> new RuntimeException("無法找到 ID 為 " + orderInfo.getOrderTypeId()));
        paymentsRecord.setType(orderType);

        String chosenPaymentMethod = orderInfo.getPaymentMethod();
        String dbPaymentToolName = switch (chosenPaymentMethod.toLowerCase()) {
            case "creditcard" -> "credit_card";
            case "transfer" -> "transfer";
            default -> throw new IllegalArgumentException("不支援的支付方式: " + chosenPaymentMethod);
        };

        PaymentsBean ecpayPaymentTool = paymentsRepository.findByPaymentTool(dbPaymentToolName)
                .orElseThrow(() -> new RuntimeException("找不到支付工具: " + dbPaymentToolName));
        paymentsRecord.setPaymentTool(ecpayPaymentTool);

        if (orderInfo.getSelectedCouponInstanceId() != null) {
            CouponInstancesBean coupon = couponInstancesRepository.findById(orderInfo.getSelectedCouponInstanceId())
                    .orElseThrow(() -> new RuntimeException(
                            "找不到 ID 為 " + orderInfo.getSelectedCouponInstanceId() + " 的優惠券實例。"));
            paymentsRecord.setCouponInstance(coupon);
        }

        paymentsRecord = paymentsRecordRepository.save(paymentsRecord);

        orderEntity.setPaymentIdForPayment(paymentsRecord);
        orderService.saveOrder(orderEntity);

        map.put("MerchantID", "2000132");
        map.put("MerchantTradeNo", orderInfo.getOrderNo());
        map.put("MerchantTradeDate", currentTime);
        map.put("PaymentType", "aio");
        map.put("TotalAmount", String.valueOf(orderInfo.getTotalAmount().intValue()));
        map.put("TradeDesc", "Order Payment Test");
        map.put("ItemName", "Travel Order");
        map.put("ReturnURL", LOCAL_URL + "/ecpay/notify");
        map.put("OrderResultURL", LOCAL_URL + "/ecpay/result");
        map.put("ClientBackURL", "http://localhost:5173/home");
        map.put("ChoosePayment", switch (chosenPaymentMethod.toLowerCase()) {
            case "creditcard" -> "Credit";
            case "transfer" -> "ATM";
            default -> "ALL";
        });
        map.put("EncryptType", "1");
        map.put("NeedExtraPaidInfo", "N");

        String checkMacValue = ecpayCheckMacValue.generateCheckMacValue(map);
        map.put("CheckMacValue", checkMacValue);

        return map;
    }

    @Transactional
    public boolean processEcpayNotification(Map<String, String> ecpayParams, OrderEntityInterface orderEntity) {
        String merchantTradeNo = ecpayParams.get("MerchantTradeNo");
        String rtnCode = ecpayParams.get("RtnCode");
        String ecpayTradeNo = ecpayParams.get("TradeNo");
        String tradeAmt = ecpayParams.get("TradeAmt");
        String paymentDate = ecpayParams.get("PaymentDate");
        String checkMacValue = ecpayParams.get("CheckMacValue");

        Map<String, String> paramsWithoutCheckMacValue = new HashMap<>(ecpayParams);
        paramsWithoutCheckMacValue.remove("CheckMacValue");
        String calculatedCheckMacValue = ecpayCheckMacValue.generateCheckMacValue(paramsWithoutCheckMacValue);

        if (!calculatedCheckMacValue.equals(checkMacValue)) {
            return false;
        }

        Optional<PaymentsRecordBean> paymentsRecordOpt = paymentsRecordRepository
                .findByTransactionNumber(merchantTradeNo);
        if (paymentsRecordOpt.isEmpty())
            return false;

        PaymentsRecordBean paymentRecord = paymentsRecordOpt.get();

        String paymentStatus = "1".equals(rtnCode) ? "PAID" : "FAILED";

        if (!"PAID".equals(paymentRecord.getPaymentStatus()) && !"FAILED".equals(paymentRecord.getPaymentStatus())) {
            paymentRecord.setPaymentStatus(paymentStatus);
            paymentRecord.setNote("綠界回傳參數: " + ecpayParams);
            paymentRecord.setRecordTime(parseDateTime(paymentDate));
            paymentsRecordRepository.save(paymentRecord);
            paymentsRecordRepository.flush();

            // 如果付款成功，且 paymentRecord 中有記錄優惠券，則將優惠券標記為已使用
            if ("PAID".equals(paymentStatus) && paymentRecord.getNote() != null
                    && paymentRecord.getNote().contains("Used Coupon UUID:")) {
                try {
                    // 從 note 中解析出 coupon UUID
                    String note = paymentRecord.getNote();
                    String uuidString = note.substring(note.indexOf("Used Coupon UUID:") + "Used Coupon UUID:".length())
                            .trim();
                    UUID couponInstanceId = UUID.fromString(uuidString);
                    couponService.markCouponAsUsed(couponInstanceId);
                } catch (Exception e) {
                    System.err.println("Error marking coupon as used: " + e.getMessage());
                }
            }

            orderService.updateOrderStatusFromPayment(paymentRecord, paymentStatus);
        }

        return true;
    }

    @Transactional
    public boolean processFakeNotification(Map<String, String> ecpayParams) {
        String merchantTradeNo = ecpayParams.get("MerchantTradeNo");
        String rtnCode = ecpayParams.get("RtnCode");
        String ecpayTradeNo = ecpayParams.get("TradeNo");
        String tradeAmt = ecpayParams.get("TradeAmt");
        String paymentDate = ecpayParams.get("PaymentDate");

        Optional<PaymentsRecordBean> paymentsRecordOpt = paymentsRecordRepository
                .findByTransactionNumber(merchantTradeNo);
        if (paymentsRecordOpt.isEmpty()) {
            System.err.println("未找到支付紀錄: " + merchantTradeNo);
            return false;
        }

        PaymentsRecordBean paymentRecord = paymentsRecordOpt.get();

        String paymentStatus = "1".equals(rtnCode) ? "PAID" : "FAILED";

        if (!"PAID".equals(paymentRecord.getPaymentStatus()) && !"FAILED".equals(paymentRecord.getPaymentStatus())) {
            paymentRecord.setPaymentStatus(paymentStatus);
            paymentRecord.setTransactionNumber(ecpayTradeNo);
            paymentRecord.setNote("模擬綠界回傳參數: " + ecpayParams.toString());
            paymentRecord.setRecordTime(parseDateTime(paymentDate));
            paymentsRecordRepository.saveAndFlush(paymentRecord);

            orderService.updateOrderStatusFromPayment(paymentRecord, paymentStatus);
            System.out.println("通知更新完成: 訂單與付款紀錄皆更新為 " + paymentStatus);
        }

        return true;
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }
}