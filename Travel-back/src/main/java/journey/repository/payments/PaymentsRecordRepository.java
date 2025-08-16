package journey.repository.payments;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.payments.PaymentsRecordBean;

@Repository
public interface PaymentsRecordRepository extends JpaRepository<PaymentsRecordBean, Integer> {

    Optional<PaymentsRecordBean> findByTransactionNumberAndType_TypeName(String transactionNumber, String typeName);

    /**
     * 根據交易編號查詢 PaymentsRecordBean。
     * 
     * @param transactionNumber 交易編號 (MerchantTradeNo / TradeNo)
     * @return 符合條件的 PaymentsRecordBean
     */
    Optional<PaymentsRecordBean> findByTransactionNumber(String transactionNumber);

    Optional<PaymentsRecordBean> findByCouponInstance_CouponInstanceId(UUID couponInstanceId);
}