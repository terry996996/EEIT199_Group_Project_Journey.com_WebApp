package journey.service.coupons;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.coupons.CouponInstancesBean;
import journey.domain.coupons.CouponTypeBean;
import journey.dto.coupons.CouponDTO;
import journey.repository.coupons.CouponInstancesRepository;

@Service
public class CouponService {

    private final CouponInstancesRepository couponInstancesRepository;

    public CouponService(CouponInstancesRepository couponInstancesRepository) {
        this.couponInstancesRepository = couponInstancesRepository;
    }

    public List<CouponDTO> getCouponsByUserId(Integer userId) {
        List<CouponInstancesBean> couponInstances = couponInstancesRepository.findByUserId(1);
        return couponInstances.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CouponInstancesBean> getUsableCouponsForUser(Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        List<CouponInstancesBean> allCoupons = couponInstancesRepository.findByUserId(1);

        return allCoupons.stream()
                .filter(coupon -> coupon.getUseAt() == null && // Coupon has not been used
                        coupon.getStartTime().isBefore(now) && // Coupon start time is in the past or now
                        (coupon.getExpireTime() == null || coupon.getExpireTime().isAfter(now))) // Coupon has not
                                                                                                 // expired or has no
                                                                                                 // expiry
                .collect(Collectors.toList());
    }

    /**
     * 計算特定折價券應用於給定訂單小計後的折扣金額。
     * 折扣規則（最低消費、折扣類型、折扣值）從關聯的 CouponTypeBean 中獲取。
     * 判斷折扣類型根據 discountPercent 和 discountAmount 是否為非零。
     *
     * @param couponInstanceId 折價券實例的 UUID。
     * @param orderSubtotal    訂單的小計金額。
     * @return 根據折價券規則計算出的折扣金額。如果折價券不適用或不滿足條件，則返回 0。
     * @throws IllegalArgumentException 如果找不到指定的折價券。
     * @throws IllegalStateException    如果折價券實例沒有關聯的 CouponTypeBean。
     */
    public int calculateDiscount(UUID couponInstanceId, int orderSubtotal) {
        // 根據 UUID 查找折價券實例
        CouponInstancesBean couponInstance = couponInstancesRepository.findById(couponInstanceId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found with ID: " + couponInstanceId));

        // 獲取關聯的 CouponTypeBean
        CouponTypeBean couponTypeDefinition = couponInstance.getCouponType();
        if (couponTypeDefinition == null) {
            throw new IllegalStateException(
                    "Coupon instance with ID: " + couponInstanceId + " has no associated coupon type definition.");
        }

        // 從 CouponTypeBean 中獲取最低消費金額 (orderAmountLimit 是 BigDecimal 類型)
        BigDecimal orderAmountLimitBd = couponTypeDefinition.getOrderAmountLimit();
        // 確保 orderAmountLimitBd 不為 null 且 orderSubtotal 低於最低消費金額
        if (orderAmountLimitBd != null && orderSubtotal < orderAmountLimitBd.intValue()) {
            System.out.println("訂單小計 " + orderSubtotal + " 低於最低消費金額 " + orderAmountLimitBd.intValue() + "，不給予折扣。");
            return 0; // 不符合最低消費，不給予折扣
        }

        int discountAmount = 0;
        BigDecimal discountPercent = couponTypeDefinition.getDiscountPercent();
        BigDecimal fixedDiscountAmount = couponTypeDefinition.getDiscountAmount();

        // 使用 compareTo(BigDecimal.ZERO) 來判斷 BigDecimal 是否為 0
        boolean isPercentNonZero = discountPercent != null && discountPercent.compareTo(BigDecimal.ZERO) != 0;
        boolean isFixedNonZero = fixedDiscountAmount != null && fixedDiscountAmount.compareTo(BigDecimal.ZERO) != 0;

        // 根據 discountPercent 和 fixedDiscountAmount 的 0/非0 狀態判斷折扣類型
        if (isPercentNonZero && !isFixedNonZero) {
            // 百分比折扣 (discountPercent 非 0, fixedDiscountAmount 是 0)
            discountAmount = (int) (orderSubtotal * (discountPercent.doubleValue() / 100.0));
            System.out.println("應用百分比折扣: " + discountPercent + "%");
        } else if (isFixedNonZero && !isPercentNonZero) {
            // 固定金額折扣 (fixedDiscountAmount 非 0, discountPercent 是 0)
            discountAmount = fixedDiscountAmount.intValue();
            System.out.println("應用固定金額折扣: " + fixedDiscountAmount.intValue() + " 元");
        } else {
            // 處理折扣類型不明確或未設定的情況：
            // 1. 兩者都是 0 (無折扣)
            // 2. 兩者都不是 0 (配置衝突或無效)
            System.out.println("Warning: Ambiguous or no valid discount configuration for coupon type ID: "
                    + couponTypeDefinition.getCouponTypeId());
            return 0; // 返回 0 折扣
        }

        // 確保折扣金額不會超過訂單總額
        return Math.min(discountAmount, orderSubtotal);
    }

    @Transactional
    public void markCouponAsUsed(UUID couponInstanceId) {
        CouponInstancesBean coupon = couponInstancesRepository.findById(couponInstanceId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found with ID: " + couponInstanceId));
        if (coupon.getUseAt() != null) { // Check if already used
            throw new IllegalStateException("Coupon with ID " + couponInstanceId + " has already been used.");
        }
        coupon.setUseAt(LocalDateTime.now()); // Mark as used
        couponInstancesRepository.save(coupon);
    }

    private CouponDTO convertToDto(CouponInstancesBean couponInstance) {
        CouponDTO dto = new CouponDTO();
        dto.setId(couponInstance.getCouponInstanceId());
        dto.setTitle(couponInstance.getCouponName());
        dto.setCode("N/A"); // 假設 'code' 欄位可以從 'get_from' 或其他方式推導，目前先設置為 "N/A"

        CouponTypeBean couponType = couponInstance.getCouponType();
        if (couponType != null) {
            if (couponType.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
                dto.setType("fixed");
                dto.setValue(couponType.getDiscountAmount());
            } else if (couponType.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
                dto.setType("percentage");
                dto.setValue(couponType.getDiscountPercent());
            } else {
                dto.setType("other");
                dto.setValue(new BigDecimal("0"));
            }
            dto.setMinSpend(couponType.getOrderAmountLimit()); // 低銷金額
            dto.setMaxDiscount(null); // 目前沒設計最大ˋ折扣上限
            dto.setDetail(couponType.getDetail()); // 折價券類型說明文字
        } else {
            dto.setType("other");
            dto.setValue(new BigDecimal("0"));
            dto.setMinSpend(BigDecimal.ZERO);
            dto.setMaxDiscount(null);
            dto.setDetail(null); // 如果沒有 couponType，detail 設為 null (其實這情況不可能發生，保險起見還是寫)
        }

        dto.setExpiryDate(couponInstance.getExpireTime());

        // 映射 issuedTime, startTime, useAt
        dto.setIssuedTime(couponInstance.getIssuedTime());
        dto.setStartTime(couponInstance.getStartTime());
        dto.setUseAt(couponInstance.getUseAt());

        String status;
        if (couponInstance.getUseAt() != null) {
            status = "used";
        } else if (couponInstance.getExpireTime() != null
                && couponInstance.getExpireTime().isBefore(LocalDateTime.now())) {
            status = "expired";
        } else {
            status = "available";
        }
        dto.setStatus(status);

        return dto;
    }
}
