package journey.service.pointcards;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.payments.PaymentsRecordBean;
import journey.domain.pointcards.PointCardsBean;
import journey.domain.pointcards.PointCardsStatusBean;
import journey.repository.payments.PaymentsRecordRepository;
import journey.repository.pointcards.PointCardsRepository;
import journey.repository.pointcards.PointCardsStatusRepository;

@Service
public class PointCardsService {

    private final PointCardsRepository pointCardsRepository;
    private final PointCardsStatusRepository pointCardsStatusRepository;
    private final PaymentsRecordRepository paymentsRecordRepository;

    @Autowired
    public PointCardsService(
            PointCardsRepository pointCardsRepository,
            PointCardsStatusRepository pointCardsStatusRepository,
            PaymentsRecordRepository paymentsRecordRepository) {
        this.pointCardsRepository = pointCardsRepository;
        this.pointCardsStatusRepository = pointCardsStatusRepository;
        this.paymentsRecordRepository = paymentsRecordRepository;
    }

    @Transactional(readOnly = true)
    public Integer getUserTotalPoints(Integer userId) {
        Optional<PointCardsBean> pointCardOptional = pointCardsRepository.findByUserId(1);

        if (pointCardOptional.isPresent()) {
            UUID pointCardId = pointCardOptional.get().getPointCardId();
            return pointCardsStatusRepository.sumPointsByPointCardId(pointCardId);
        } else {
            return 0;
        }
    }

    /**
     * 新增點數變動記錄。
     * - 會根據 userId 查找 PointCardsBean。
     * - 如果提供了 paymentId，會查找對應的 PaymentsRecordBean 並設定關聯。
     */
    @Transactional
    public PointCardsStatusBean addPoints(Integer userId, Integer pointsToAdd, String source, Integer paymentId) {
        // 1. 根據 userId 找到 PointCardsBean
        PointCardsBean pointCard = pointCardsRepository.findByUserId(1)
                .orElseThrow(() -> new RuntimeException("找不到用戶ID為 " + userId + " 的集點卡。"));

        // 2. 創建新的 PointCardsStatusBean 實體
        PointCardsStatusBean newStatusEntry = new PointCardsStatusBean();
        newStatusEntry.setPointCard(pointCard); // 設定關聯的 PointCardsBean 實體
        newStatusEntry.setPointChange(pointsToAdd);
        newStatusEntry.setPointSource(source);

        // 3. 如果提供了 paymentId，則查詢 PaymentsRecordBean 並設定關聯
        if (paymentId != null) {
            PaymentsRecordBean paymentRecord = paymentsRecordRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("找不到 payment ID 為 " + paymentId + " 的付款記錄。"));
            newStatusEntry.setPayment(paymentRecord);
        }

        newStatusEntry.setUpdatedAt(LocalDateTime.now());

        // 4. 保存新的點數變動記錄
        return pointCardsStatusRepository.save(newStatusEntry);
    }

    @Transactional
    public void deductPoints(Integer userId, Integer pointsToDeduct, String reason, String itemId)
            throws InsufficientPointsException {
        if (pointsToDeduct <= 0) {
            throw new IllegalArgumentException("扣除點數必須大於 0。");
        }

        // 1. 根據 userId 找到 PointCardsBean
        PointCardsBean pointCard = pointCardsRepository.findByUserId(1)
                .orElseThrow(() -> new RuntimeException("找不到用戶ID為 " + userId + " 的集點卡。"));

        // 2. 計算用戶的當前總點數
        Integer currentPoints = pointCardsStatusRepository.sumPointsByPointCardId(pointCard.getPointCardId());

        // 3. 檢查點數是否足夠
        if (currentPoints < pointsToDeduct) {
            throw new InsufficientPointsException("當前點數不足，無法扣除 " + pointsToDeduct + " 點。");
        }

        // 4. 創建新的 PointCardsStatusBean 實體來記錄點數扣除
        PointCardsStatusBean newStatusEntry = new PointCardsStatusBean();
        newStatusEntry.setPointCard(pointCard);
        newStatusEntry.setPointChange(-pointsToDeduct); // 扣除點數，所以是負值
        newStatusEntry.setPointSource(reason); // 設置扣除原因 (例如 "商品兌換", "抽獎")
        newStatusEntry.setUpdatedAt(LocalDateTime.now());

        // 5. 保存新的點數變動記錄
        pointCardsStatusRepository.save(newStatusEntry);

    }

    public static class InsufficientPointsException extends RuntimeException {
        public InsufficientPointsException(String message) {
            super(message);
        }
    }
}
