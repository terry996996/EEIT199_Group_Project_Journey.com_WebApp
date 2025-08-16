package journey.repository.attractions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import journey.domain.attractiontickets.AttractionImagesBean;
import journey.domain.attractiontickets.AttractionsBean;

import java.util.List;

@Repository
public interface AttractionImagesRepository extends JpaRepository<AttractionImagesBean, Integer> {

    // 自訂方法：透過 attractionId 查詢圖片列表
    List<AttractionImagesBean> findByAttraction_Id(Integer attractionId);

    // 整個景點物件查詢
    List<AttractionImagesBean> findByAttraction(AttractionsBean attraction);
}
