package journey.repository.comments;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.comments.CommentFeedbacksBean;

@Repository
public interface CommentFeedbacksRepository
                extends JpaRepository<CommentFeedbacksBean, CommentFeedbacksBean.CommentFeedbacksId> {

        /**
         * 查詢指定留言的所有按讚記錄
         * 
         * @param commentId 留言 ID
         * @return 按讚記錄列表
         */
        @Query("SELECT cf FROM CommentFeedbacksBean cf WHERE cf.id.commentId = :commentId AND cf.feedback = true")
        List<CommentFeedbacksBean> findLikesByCommentId(@Param("commentId") Integer commentId);

        /**
         * 查詢指定留言的按讚數量
         * 
         * @param commentId 留言 ID
         * @return 按讚數量
         */
        @Query("SELECT COUNT(cf) FROM CommentFeedbacksBean cf WHERE cf.id.commentId = :commentId AND cf.feedback = true")
        int countLikesByCommentId(@Param("commentId") Integer commentId);

        /**
         * 查詢指定用戶對指定留言的按讚記錄
         * 
         * @param commentId 留言 ID
         * @param userId    用戶 ID
         * @return 按讚記錄（如果存在）
         */
        @Query("SELECT cf FROM CommentFeedbacksBean cf WHERE cf.id.commentId = :commentId AND cf.id.userId = :userId")
        Optional<CommentFeedbacksBean> findByCommentIdAndUserId(@Param("commentId") Integer commentId,
                        @Param("userId") Integer userId);

        /**
         * 檢查指定用戶是否已對指定留言按讚
         * 
         * @param commentId 留言 ID
         * @param userId    用戶 ID
         * @return 是否已按讚
         */
        @Query("SELECT CASE WHEN COUNT(cf) > 0 THEN true ELSE false END FROM CommentFeedbacksBean cf WHERE cf.id.commentId = :commentId AND cf.id.userId = :userId AND cf.feedback = true")
        boolean hasUserLikedComment(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

        // ==================== 批次查詢優化 ====================
        
        /**
         * 批次查詢多個留言的按讚數量（避免 N+1 查詢）
         * 
         * @param commentIds 留言ID列表
         * @return 按讚數量統計 [commentId, likeCount]
         */
        @Query("SELECT cf.id.commentId, COUNT(cf) FROM CommentFeedbacksBean cf " +
               "WHERE cf.id.commentId IN :commentIds AND cf.feedback = true " +
               "GROUP BY cf.id.commentId")
        List<Object[]> countLikesByCommentIds(@Param("commentIds") List<Integer> commentIds);

        /**
         * 批次查詢指定用戶對多個留言的按讚狀態（避免 N+1 查詢）
         * 
         * @param commentIds 留言ID列表
         * @param userId     用戶ID
         * @return 按讚狀態 [commentId, hasLiked]
         */
        @Query("SELECT cf.id.commentId, true FROM CommentFeedbacksBean cf " +
               "WHERE cf.id.commentId IN :commentIds AND cf.id.userId = :userId AND cf.feedback = true")
        List<Object[]> findUserLikesByCommentIds(@Param("commentIds") List<Integer> commentIds, 
                                                @Param("userId") Integer userId);

}
