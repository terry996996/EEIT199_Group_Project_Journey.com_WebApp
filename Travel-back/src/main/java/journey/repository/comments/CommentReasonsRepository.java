package journey.repository.comments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import journey.domain.comments.CommentReasonsBean;
import journey.projection.OptionView;

public interface CommentReasonsRepository extends JpaRepository<CommentReasonsBean, Integer> {

        // 查詢所有啟用的檢舉類型
        @Query("""
                        SELECT c.id   AS id,
                               c.reasonText AS label
                        FROM   CommentReasonsBean c
                        WHERE  c.isActive = true
                        ORDER BY c.id ASC
                        """)
        List<OptionView> findOptions();
}
