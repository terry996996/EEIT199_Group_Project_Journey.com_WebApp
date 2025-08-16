package journey.repository.comments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.comments.CommentImagesBean;

@Repository
public interface CommentImagesRepository extends JpaRepository<CommentImagesBean, Integer> {

    /**
     * 查詢指定 comment 的所有有效圖片
     * 
     * @param commentId comment ID
     * @return 有效的圖片列表
     */
    @Query("SELECT ci FROM CommentImagesBean ci WHERE ci.comment.id = :commentId ORDER BY ci.sortOrder ASC")
    List<CommentImagesBean> findActiveImagesByCommentId(@Param("commentId") Integer commentId);

    /**
     * 查詢指定 comment 的所有圖片（包括無效的）
     * 
     * @param commentId comment ID
     * @return 所有圖片列表
     */
    @Query(value = "SELECT * FROM comment_images WHERE comment_id = :commentId ORDER BY sort_order ASC", nativeQuery = true)
    List<CommentImagesBean> findAllImagesByCommentId(@Param("commentId") Integer commentId);

}
