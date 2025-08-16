package journey.repository.lodgings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import journey.domain.lodgings.FacilitiesBean;
import journey.projection.OptionView;

public interface FacilitiesRepository extends JpaRepository<FacilitiesBean, Integer> {

    // 查詢所有啟用的房型設施
    @Query("""
            SELECT f.id AS id,
                   f.facilityName AS label
            FROM   FacilitiesBean f
            WHERE  f.isActive = true
            ORDER BY f.id ASC
            """)
    List<OptionView> findOptions();

}
