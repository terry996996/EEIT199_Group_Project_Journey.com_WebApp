package journey.repository.attractions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import journey.domain.attractiontickets.AttractionContentsBean;
import journey.domain.attractiontickets.AttractionsBean;

import java.util.List;

@Repository
public interface AttractionContentsRepository extends JpaRepository<AttractionContentsBean, Integer> {
  /**
   * 根據景點主體物件查詢其底下所有內文
   * 相當於：SELECT * FROM attraction_contents WHERE attraction_id = ?
   * 用來搭配 @ManyToOne(fetch = FetchType.LAZY) 關聯的 Attraction Bean
   */
  List<AttractionContentsBean> findByAttraction(AttractionsBean attraction);

  /**
   * 根據景點主鍵 ID 查詢其所有內文（用 ID 而不是整個物件）
   * 相當於：SELECT * FROM attraction_contents WHERE attraction_id = ?
   * 效果與 findByAttraction(...) 一樣，只是傳入參數不同（只傳 ID）
   */

  List<AttractionContentsBean> findByAttraction_Id(Integer attractionId);
}