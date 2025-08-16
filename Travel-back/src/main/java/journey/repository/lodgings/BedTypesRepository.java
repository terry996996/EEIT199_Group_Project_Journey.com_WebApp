package journey.repository.lodgings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import journey.domain.lodgings.BedTypesBean;
import journey.projection.OptionView;

public interface BedTypesRepository extends JpaRepository<BedTypesBean, Integer> {

    // List<OptionView> findAllByOrderByIdAsc();

    // 查詢所有床的種類
    @Query("""
            SELECT b.id AS id,
                   b.name AS label
            FROM   BedTypesBean b
            ORDER BY b.id ASC
            """)
    List<OptionView> findOptions();

}
