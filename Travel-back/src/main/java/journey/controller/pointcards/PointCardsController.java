package journey.controller.pointcards;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.service.pointcards.PointCardsService;
import journey.service.pointcards.PointCardsService.InsufficientPointsException;

@RestController
@RequestMapping("/api/points")
public class PointCardsController {

    private final PointCardsService pointCardsService;

    @Autowired
    public PointCardsController(PointCardsService pointCardsService) {
        this.pointCardsService = pointCardsService;
    }

    /**
     * 獲取用戶的總點數。
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Integer>> getUserTotalPoints() {
        Integer totalPoints = pointCardsService.getUserTotalPoints(1);
        return ResponseEntity.ok(Map.of("totalPoints", totalPoints));
    }

    /**
     * 為用戶新增點數。
     */
    @PostMapping("/add")
    public ResponseEntity<String> addPointsToUser(@RequestBody PointAdditionRequest request) {
        try {
            pointCardsService.addPoints(1, request.getPointsToAdd(), request.getSource(), request.getPaymentId());
            return ResponseEntity.ok("點數新增成功。");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 為用戶扣除點數，用於前端商品兌換或抽獎後更新。
     */
    @PostMapping("/deduct")
    public ResponseEntity<String> deductPointsFromUser(@RequestBody PointDeductionRequest request) {
        Integer fixedUserId = 1;

        try {
            pointCardsService.deductPoints(fixedUserId, request.getPointsToDeduct(), request.getReason(),
                    request.getItemId());
            return ResponseEntity.ok("點數扣除成功。");
        } catch (InsufficientPointsException e) {
            // 點數不足的自定義異常處理
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("點數不足：" + e.getMessage());
        } catch (RuntimeException e) {
            // 其他運行時錯誤
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("點數扣除失敗：" + e.getMessage());
        }
    }

    static class PointAdditionRequest {

        private Integer pointsToAdd;
        private String source;
        private Integer paymentId;

        public Integer getPointsToAdd() {
            return pointsToAdd;
        }

        public void setPointsToAdd(Integer pointsToAdd) {
            this.pointsToAdd = pointsToAdd;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Integer getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Integer paymentId) {
            this.paymentId = paymentId;
        }
    }

    // 點數扣除請求體類別
    static class PointDeductionRequest {

        private Integer pointsToDeduct; // 要扣除的點數數量
        private String reason; // 扣除原因，ex: "商品兌換", "抽獎"
        private String itemId; // 兌換的商品ID或抽獎相關ID

        public Integer getPointsToDeduct() {
            return pointsToDeduct;
        }

        public void setPointsToDeduct(Integer pointsToDeduct) {
            this.pointsToDeduct = pointsToDeduct;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }
    }
}