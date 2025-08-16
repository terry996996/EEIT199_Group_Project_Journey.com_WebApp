package journey.repository.lodgings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import journey.domain.lodgings.LodgingsTypeBean;
import journey.projection.OptionView;

@Repository
public interface LodgingsTypeRepository extends JpaRepository<LodgingsTypeBean, Integer> {

    // List<OptionView> findByIsActiveTrueOrderByIdAsc();

    // 查詢所有啟用的住宿類型
    @Query("""
            SELECT l.id AS id,
                   l.typeText AS label
            FROM   LodgingsTypeBean l
            WHERE  l.isActive = true
            """)
    List<OptionView> findOptions();

}
