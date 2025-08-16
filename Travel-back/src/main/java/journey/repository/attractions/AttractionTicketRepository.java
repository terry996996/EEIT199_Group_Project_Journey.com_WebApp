package journey.repository.attractions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttractionTicketRepository extends JpaRepository<AttractionTicketsBean, Integer> {

    // 依城市查詢票券
    List<AttractionTicketsBean> findByCity(CityBean city);

    // 依國家查詢票券
    List<AttractionTicketsBean> findByCountryId(Integer countryId);

    // 模糊查詢名稱（不區分大小寫）
    // 根据关键字模糊查询景点门票信息，不区分大小写
    List<AttractionTicketsBean> findByNameContainingIgnoreCase(String keyword);

    // 查詢啟用狀態的票券
    List<AttractionTicketsBean> findByStateTrue();

    
    // 模糊查詢票券名稱（不分大小寫）並依瀏覽量由高到低排序
    List<AttractionTicketsBean> findByNameContainingIgnoreCaseOrderByViewsDesc(String name);
    
}
