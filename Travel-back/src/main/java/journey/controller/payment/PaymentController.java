package journey.controller.payment;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import journey.domain.orders.OrderEntityInterface;
import journey.dto.PaymentRequestInfo;
import journey.dto.Linepay.LinePayResponse;
import journey.repository.payments.PaymentsRecordRepository;
import journey.repository.payments.TypeEnumRepository;
import journey.service.order.OrderService;
import journey.service.payment.EcpayService;
import journey.service.payment.LinePayService;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final EcpayService ecpayService;
    private final LinePayService linePayService;
    private final PaymentsRecordRepository paymentsRecordRepository;
    private final TypeEnumRepository typeEnumRepository;
    private final OrderService orderService;

    public PaymentController(EcpayService ecpayService, LinePayService linePayService,
            PaymentsRecordRepository paymentsRecordRepository,
            TypeEnumRepository typeEnumRepository,
            OrderService orderService) {
        this.ecpayService = ecpayService;
        this.linePayService = linePayService;
        this.paymentsRecordRepository = paymentsRecordRepository;
        this.typeEnumRepository = typeEnumRepository;
        this.orderService = orderService;
    }

    // 綠界支付請求端點
    @PostMapping("/ecpay/create-payment")
    public ResponseEntity<?> createEcpayPayment(@RequestBody PaymentRequestInfo orderInfo) {
        System.out.println("Controller 接收到的 PaymentRequestInfo 物件: " + orderInfo.toString());

        if (orderInfo.getPaymentMethod() == null || orderInfo.getPaymentMethod().isEmpty()) {
            System.err.println("錯誤：Controller 接收到的 PaymentRequestInfo 中的 paymentMethod 為 null 或空！");
            return ResponseEntity.badRequest().body("Payment method is missing or invalid.");
        }

        try {
            // 使用 OrderService 獲取(查出)或創建訂單
            OrderEntityInterface orderToProcess = orderService.getOrCreateOrder(orderInfo);

            // 調用 EcpayService 來生成綠界支付參數
            Map<String, String> ecpayParams = ecpayService.generateEcpayForm(orderInfo, orderToProcess);

            return ResponseEntity.ok(ecpayParams);

        } catch (IllegalArgumentException e) {
            System.err.println("支付請求參數錯誤: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            System.err.println("訂單狀態不允許支付: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create ECPay payment form: " + e.getMessage());
        }
    }

    // LINE Pay 支付請求端點
    @PostMapping("/linepay/create-payment")
    public ResponseEntity<?> createLinePayPayment(@RequestBody PaymentRequestInfo orderInfo) {
        System.out.println("Controller 接收到的 PaymentRequestInfo 物件: " + orderInfo.toString());
        if (orderInfo.getPaymentMethod() == null || orderInfo.getPaymentMethod().isEmpty()) {
            System.err.println("錯誤：Controller 接收到的 PaymentRequestInfo 中的 paymentMethod 為 null 或空！");
            return ResponseEntity.badRequest().body("Payment method is missing or invalid.");
        }

        try {
            // 使用 OrderService 獲取((查出))或創建訂單
            OrderEntityInterface orderToProcess = orderService.getOrCreateOrder(orderInfo);

            // 調用 LinePayService 來發起支付
            LinePayResponse response = linePayService.initiatePayment(orderInfo, orderToProcess);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.err.println("支付請求參數錯誤: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            System.err.println("訂單狀態不允許支付: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to initiate LINE Pay payment: " + e.getMessage());
        }
    }
}