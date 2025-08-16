package journey.repository.attractions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import journey.domain.attractiontickets.AttractionsBean;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;

import java.util.List;

@Repository
public interface AttractionsRepository extends JpaRepository<AttractionsBean, Integer> {

    // 自訂查詢：依照城市查找
    List<AttractionsBean> findByCity(CityBean city);

    // 自訂查詢：依照名稱模糊搜尋
    List<AttractionsBean> findByNameContainingIgnoreCase(String name);

    // 自訂查詢：依照國家查找
    List<AttractionsBean> findByCountryId(CountryBean country);

   
}
