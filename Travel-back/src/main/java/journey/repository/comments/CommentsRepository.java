package journey.repository.comments;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.comments.CommentsBean;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsBean, Integer> {

    @Query("SELECT c FROM CommentsBean c " +
           "LEFT JOIN FETCH c.commentImages " +
           "LEFT JOIN FETCH c.parentComment " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.id = :id")
    Optional<CommentsBean> findWithImagesById(@Param("id") Integer id);

    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.lodging LEFT JOIN FETCH c.roomType WHERE c.id = :id")
    Optional<CommentsBean> findWithLodgingById(@Param("id") Integer id);

    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages WHERE c.id = :id")
    Optional<CommentsBean> findByIdWithImages(@Param("id") Integer id);

    // ==================== 條件查詢方法 ====================
    
    /**
     * 根據目標類型和ID查詢留言（條件載入關聯）
     * 只載入對應 targetType 的關聯，避免不必要的 JOIN
     */
    @Query("SELECT c FROM CommentsBean c " +
           "LEFT JOIN FETCH c.commentImages " +
           "LEFT JOIN FETCH c.parentComment " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.lodging.id = :targetId " +
           "AND c.deleteStatus = 1 AND c.isActive = true " +
           "ORDER BY c.createdAt DESC")
    List<CommentsBean> findActiveCommentsByLodgingIdOptimized(@Param("targetId") Integer targetId);

    @Query("SELECT c FROM CommentsBean c " +
           "LEFT JOIN FETCH c.commentImages " +
           "LEFT JOIN FETCH c.parentComment " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.lodging.id = :targetId " +
           "ORDER BY c.createdAt DESC")
    List<CommentsBean> findAllCommentsByLodgingIdOptimized(@Param("targetId") Integer targetId);

    @Query("SELECT c FROM CommentsBean c " +
           "LEFT JOIN FETCH c.commentImages " +
           "LEFT JOIN FETCH c.parentComment " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.roomType.id = :targetId " +
           "AND c.deleteStatus = 1 AND c.isActive = true " +
           "ORDER BY c.createdAt DESC")
    List<CommentsBean> findActiveCommentsByRoomTypeIdOptimized(@Param("targetId") Integer targetId);

    @Query("SELECT c FROM CommentsBean c " +
           "LEFT JOIN FETCH c.commentImages " +
           "LEFT JOIN FETCH c.parentComment " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.roomType.id = :targetId " +
           "ORDER BY c.createdAt DESC")
    List<CommentsBean> findAllCommentsByRoomTypeIdOptimized(@Param("targetId") Integer targetId);

    // ==================== 原有方法（保留向後相容） ====================

    /**
     * 查詢指定旅館的所有有效留言
     */
    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages LEFT JOIN FETCH c.parentComment WHERE c.lodging.id = :lodgingId AND c.deleteStatus = 1 AND c.isActive = true ORDER BY c.createdAt DESC")
    List<CommentsBean> findActiveCommentsByLodgingId(@Param("lodgingId") Integer lodgingId);

    /**
     * 查詢指定旅館的所有留言（包括已刪除的）
     */
    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages LEFT JOIN FETCH c.parentComment WHERE c.lodging.id = :lodgingId ORDER BY c.createdAt DESC")
    List<CommentsBean> findAllCommentsByLodgingId(@Param("lodgingId") Integer lodgingId);

    /**
     * 查詢指定房型的所有有效留言
     */
    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages LEFT JOIN FETCH c.parentComment WHERE c.roomType.id = :roomTypeId AND c.deleteStatus = 1 AND c.isActive = true ORDER BY c.createdAt DESC")
    List<CommentsBean> findActiveCommentsByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    /**
     * 查詢指定房型的所有留言（包括已刪除的）
     */
    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages LEFT JOIN FETCH c.parentComment WHERE c.roomType.id = :roomTypeId ORDER BY c.createdAt DESC")
    List<CommentsBean> findAllCommentsByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    /**
     * 查詢指定留言的所有回覆
     */
    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages LEFT JOIN FETCH c.parentComment WHERE c.parentComment.id = :parentCommentId AND c.deleteStatus = 1 AND c.isActive = true ORDER BY c.createdAt ASC")
    List<CommentsBean> findActiveRepliesByParentCommentId(@Param("parentCommentId") Integer parentCommentId);

    /**
     * 查詢指定留言的所有回覆（包括已刪除的）
     */
    @Query("SELECT c FROM CommentsBean c LEFT JOIN FETCH c.commentImages LEFT JOIN FETCH c.parentComment WHERE c.parentComment.id = :parentCommentId ORDER BY c.createdAt ASC")
    List<CommentsBean> findAllRepliesByParentCommentId(@Param("parentCommentId") Integer parentCommentId);

}
