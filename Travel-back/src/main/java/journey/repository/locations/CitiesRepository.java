package journey.repository.locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import journey.domain.locations.CityBean;
import journey.projection.OptionView;

@Repository
public interface CitiesRepository extends JpaRepository<CityBean, Integer> {

    @Query("SELECT c.id as value, c.nameZh as label FROM CityBean c ORDER BY c.id")
    List<OptionView> findOptions();
}
