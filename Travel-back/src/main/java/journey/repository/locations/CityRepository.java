package journey.repository.locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import journey.domain.locations.CityBean;
import journey.projection.OptionView;

@Repository
public interface CityRepository extends JpaRepository<CityBean, Integer> {

    @Query("SELECT c.id as id, " +
            "CASE WHEN c.nameZh IS NOT NULL AND c.nameZh != '' THEN c.nameZh ELSE c.name END as label " +
            "FROM CityBean c ORDER BY " +
            "CASE WHEN c.nameZh IS NOT NULL AND c.nameZh != '' THEN c.nameZh ELSE c.name END")
    List<OptionView> findOptions();

}
