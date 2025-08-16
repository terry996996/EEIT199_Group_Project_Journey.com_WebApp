package journey.service.lodgings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.repository.lodgings.UnifiedRatingRepository;

import java.util.Optional;

@Service
public class UnifiedRatingService {

    @Autowired
    private UnifiedRatingRepository unifiedRatingRepository;

    /**
     * 統一的評分計算邏輯
     * 近1個月平均分，只計算主留言
     * 
     * @param lodgingId 住宿ID
     * @return 評分統計 [平均分, 評論數]，如果沒有評分則返回 [0.0, 0L]
     */
    public RatingStats calculateUnifiedRating(Integer lodgingId) {
        Object result = unifiedRatingRepository.calculateUnifiedRating(lodgingId);

        if (result == null) {
            return new RatingStats(0.0, 0L);
        }

        Object[] stats = (Object[]) result;
        Double avgRating = stats[0] != null ? ((Number) stats[0]).doubleValue() : 0.0;
        Long reviewCount = stats[1] != null ? ((Number) stats[1]).longValue() : 0L;

        return new RatingStats(avgRating, reviewCount);
    }

    /**
     * 檢查是否為熱門住宿
     * 
     * @param lodgingId 住宿ID
     * @return 是否符合熱門住宿標準
     */
    public boolean isHotLodging(Integer lodgingId) {
        Optional<Boolean> result = unifiedRatingRepository.isHotLodging(lodgingId);
        return result.orElse(false);
    }

    /**
     * 評分統計資料類
     */
    public static class RatingStats {
        private final Double avgRating;
        private final Long reviewCount;

        public RatingStats(Double avgRating, Long reviewCount) {
            this.avgRating = avgRating;
            this.reviewCount = reviewCount;
        }

        public Double getAvgRating() {
            return avgRating;
        }

        public Long getReviewCount() {
            return reviewCount;
        }

        /**
         * 檢查是否符合熱門住宿標準
         */
        public boolean meetsHotLodgingCriteria() {
            return avgRating >= 4.0 && reviewCount >= 5L;
        }
    }
}