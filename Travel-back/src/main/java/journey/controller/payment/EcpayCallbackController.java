package journey.controller.payment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import journey.domain.orders.OrderEntityInterface;
import journey.domain.payments.PaymentsRecordBean;
import journey.repository.payments.PaymentsRecordRepository;
import journey.service.order.OrderService;
import journey.service.payment.EcpayService;

@Controller
@RequestMapping("/ecpay")
public class EcpayCallbackController {

    @Autowired
    private EcpayService ecpayService;
    @Autowired
    private PaymentsRecordRepository paymentsRecordRepository;
    @Autowired
    private OrderService orderService;

    private static final String FRONTEND_PAYMENT_RESULT_URL = "http://localhost:5173/payment-result";
    private static final String FRONTEND_PAYMENT_CANCEL_URL = "http://localhost:5173/payment-cancel";

    /*
     * 處理綠界後端通知的回傳（伺服器對伺服器）
     * 綠界會 POST 到 /ecpay/notify
     */

    @PostMapping("/notify")
    @ResponseBody
    public String paymentNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values.length > 0) {
                params.put(key, values[0]);
            }
        });
        System.out.println("收到綠界後端通知 (EcpayCallbackController)：" + params);

        String merchantTradeNo = params.get("MerchantTradeNo"); // 獲取綠界回傳的訂單號

        if (merchantTradeNo == null || merchantTradeNo.isEmpty()) {
            System.err.println("綠界通知缺少 MerchantTradeNo 參數。");
            return "0|Missing MerchantTradeNo";
        }

        try {
            Optional<OrderEntityInterface> orderOpt = orderService.findOrderByOrderNo(merchantTradeNo);

            if (!orderOpt.isPresent()) {
                System.err.println("錯誤：未找到 OrderNo 為 " + merchantTradeNo + " 的訂單，無法處理綠界通知。");
                return "0|Order Not Found"; // 返回給綠界，表示未找到訂單
            }

            OrderEntityInterface orderEntity = orderOpt.get();

            boolean success = ecpayService.processEcpayNotification(params, orderEntity);

            if (success) {
                Optional<PaymentsRecordBean> paymentRecordOpt = paymentsRecordRepository
                        .findByTransactionNumber(merchantTradeNo);

                if (paymentRecordOpt.isPresent()) {
                    PaymentsRecordBean paymentRecord = paymentRecordOpt.get();
                    orderService.updateOrderStatusFromPayment(paymentRecord, paymentRecord.getPaymentStatus());
                } else {
                    System.err.println("錯誤：處理成功但未找到對應的支付記錄 (OrderNo: " + merchantTradeNo + ")，無法更新訂單狀態。");
                }
                return "1|OK"; // 綠界要求的回應
            } else {
                System.err.println("綠界支付通知處理失敗。");
                return "0|FAIL"; // 綠界要求的回應
            }
        } catch (Exception e) {
            System.err.println("處理綠界通知時發生異常: " + e.getMessage());
            e.printStackTrace();
            return "0|FAIL"; // 綠界要求的回應
        }
    }

    /*
     * 處理綠界前端跳轉回傳（用戶瀏覽器回傳）
     * 綠界會 POST 到 /ecpay/result
     */

    // 這裡非常重要，一定要在，才會在用戶導往此頁面之下，(付款狀態完成的頁面)，然後才會去更新對應的訂單和付款紀錄資料
    @PostMapping("/result")
    public RedirectView fakeNotifyHandler(@RequestParam Map<String, String> params) {
        System.out.println("通知收到參數：" + params);
        ecpayService.processFakeNotification(params);

        return redirectToFrontendResultPage(params);
    }

    /*
     * 用戶取消付款的返回頁面
     * 綠界在用戶點擊返回或取消時會導回此頁面。
     * 綠界會 GET 到 /ecpay/cancel
     */
    @GetMapping("/cancel")
    public RedirectView paymentCancel(@RequestParam Map<String, String> params) {
        System.out.println("收到綠界取消導向 (GET /ecpay/cancel in EcpayCallbackController)：" + params);
        return new RedirectView(FRONTEND_PAYMENT_CANCEL_URL);
    }

    /*
     * 將綠界回傳的參數組裝成 URL 參數，導向到前端頁面
     */
    private RedirectView redirectToFrontendResultPage(Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(FRONTEND_PAYMENT_RESULT_URL);
        urlBuilder.append("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }

        if (urlBuilder.length() > 0 && urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        String finalRedirectUrl = urlBuilder.toString();
        System.out.println("準備導向前端的 URL (EcpayCallbackController): " + finalRedirectUrl);

        return new RedirectView(finalRedirectUrl, true);
    }
}