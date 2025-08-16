package journey.repository.lodgings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnifiedRatingRepository extends JpaRepository<journey.domain.lodgings.LodgingsBean, Integer> {

    /**
     * 統一的評分計算邏輯（用於搜尋功能和住宿詳情）
     * 近1個月平均分 > 4，只計算主留言
     * 注意：這只是計算評分，不進行過濾
     */
    @Query("SELECT AVG(CAST(c.rating AS FLOAT)), COUNT(c.id) " +
            "FROM journey.domain.comments.CommentsBean c " +
            "WHERE c.lodging.id = :lodgingId " +
            "AND c.rating IS NOT NULL " +
            "AND c.isActive = true " +
            "AND c.deleteStatus = 1 " +
            "AND c.parentComment IS NULL " +
            "AND c.createdAt >= DATEADD(DAY, -30, CURRENT_DATE)")
    Object calculateUnifiedRating(@Param("lodgingId") Integer lodgingId);

    /**
     * 檢查住宿是否符合熱門住宿標準（用於首頁熱門旅宿）
     * 近1個月平均分 > 4 且評論數 >= 5
     */
    @Query("SELECT CASE WHEN AVG(CAST(c.rating AS FLOAT)) >= 4 AND COUNT(c.id) >= 5 THEN true ELSE false END " +
            "FROM journey.domain.comments.CommentsBean c " +
            "WHERE c.lodging.id = :lodgingId " +
            "AND c.rating IS NOT NULL " +
            "AND c.isActive = true " +
            "AND c.deleteStatus = 1 " +
            "AND c.parentComment IS NULL " +
            "AND c.createdAt >= DATEADD(DAY, -30, CURRENT_DATE)")
    Optional<Boolean> isHotLodging(@Param("lodgingId") Integer lodgingId);

    /**
     * 獲取熱門住宿列表（用於首頁熱門旅宿）
     * 只返回符合標準的住宿
     */
    @Query("SELECT l.id, l.lodgingName, AVG(CAST(c.rating AS FLOAT)) as avgRating, COUNT(c.id) as reviewCount " +
            "FROM journey.domain.lodgings.LodgingsBean l " +
            "JOIN journey.domain.comments.CommentsBean c ON c.lodging.id = l.id " +
            "WHERE l.isActive = true " +
            "AND l.deleteStatus = 1 " +
            "AND c.rating IS NOT NULL " +
            "AND c.isActive = true " +
            "AND c.deleteStatus = 1 " +
            "AND c.parentComment IS NULL " +
            "AND c.createdAt >= DATEADD(DAY, -30, CURRENT_DATE) " +
            "GROUP BY l.id, l.lodgingName " +
            "HAVING AVG(CAST(c.rating AS FLOAT)) >= 4 AND COUNT(c.id) >= 5 " +
            "ORDER BY avgRating DESC, reviewCount DESC")
    List<Object[]> findHotLodgings();

    /**
     * 獲取所有住宿的統一評分（用於搜尋功能）
     * 返回所有住宿的評分，不管是否符合標準
     */
    @Query("SELECT l.id, AVG(CAST(c.rating AS FLOAT)) as avgRating, COUNT(c.id) as reviewCount " +
            "FROM journey.domain.lodgings.LodgingsBean l " +
            "LEFT JOIN journey.domain.comments.CommentsBean c ON c.lodging.id = l.id " +
            "AND c.rating IS NOT NULL " +
            "AND c.isActive = true " +
            "AND c.deleteStatus = 1 " +
            "AND c.parentComment IS NULL " +
            "AND c.createdAt >= DATEADD(DAY, -30, CURRENT_DATE) " +
            "WHERE l.isActive = true AND l.deleteStatus = 1 " +
            "GROUP BY l.id")
    List<Object[]> getAllLodgingsUnifiedRating();
}