package journey.repository.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.comments.CommentReportsBean;
import journey.enums.CommentReportStatusEnum;

public interface CommentReportsRepository extends JpaRepository<CommentReportsBean, Integer> {
    /** 不看狀態：同一使用者是否已檢舉過同一留言 */
    boolean existsByCommentIdAndCreatedById(Integer commentId, Integer createdById);

    /** 看狀態：同一使用者對同一留言、同一狀態（例：PENDING）不可重複檢舉 */
    boolean existsByCommentIdAndCreatedByIdAndStatus(
            Integer commentId,
            Integer createdById,
            CommentReportStatusEnum status);
    
    /** 根據狀態查詢檢舉，按建立時間降序排列 */
    Page<CommentReportsBean> findByStatusOrderByCreatedAtDesc(CommentReportStatusEnum status, Pageable pageable);
    
    /** 查詢所有檢舉，按建立時間降序排列 */
    Page<CommentReportsBean> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
