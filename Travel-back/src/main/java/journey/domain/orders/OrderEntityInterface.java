package journey.domain.orders;

import journey.domain.payments.PaymentsRecordBean;

// 定義了一個所有訂單共同實作的界面，當中可以get或set所有訂單都會有的一些共同資料欄位
public interface OrderEntityInterface {
    Integer getOrderIdForPayment();

    String getOrderNoForPayment();

    Boolean getStatusForPayment();

    void setStatusForPayment(String status);

    void setPaymentIdForPayment(PaymentsRecordBean paymentRecord);
}
