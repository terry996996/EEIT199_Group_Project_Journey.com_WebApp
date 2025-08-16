package journey.repository.attractions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import journey.domain.attractiontickets.AttractionTagsBean;
import journey.projection.OptionView;
import org.springframework.data.jpa.repository.Query; // @Query 標註
import java.util.List; // List 回傳型別

import java.util.Optional;

@Repository
public interface AttractionTagsRepository extends JpaRepository<AttractionTagsBean, Integer> {

    // 自訂查詢：根據標籤名稱尋找（用於檢查是否存在）
    Optional<AttractionTagsBean> findByTagName(String tagName);

    // 快速讀取標籤種類
    @Query("SELECT t.id AS id, t.tagName AS label FROM AttractionTagsBean t")
    List<OptionView> findOptions(); // 正確：沒有 {}
}
