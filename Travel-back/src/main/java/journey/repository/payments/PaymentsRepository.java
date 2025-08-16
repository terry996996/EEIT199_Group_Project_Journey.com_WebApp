package journey.repository.payments;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.payments.PaymentsBean;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentsBean, Integer> {
    /**
     * 根據支付工具名稱查詢 PaymentsBean。
     * 
     * @param paymentTool 支付工具名稱，例如 "credit_card", "line_pay", "transfer"
     * @return 符合條件的 PaymentsBean
     */
    Optional<PaymentsBean> findByPaymentTool(String paymentTool);
}
