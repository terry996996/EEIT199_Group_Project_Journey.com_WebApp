package journey.repository.locations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import journey.domain.locations.CountryBean;
import journey.projection.OptionView;

@Repository
public interface CountryRepository extends JpaRepository<CountryBean, Integer> {

    @Query("SELECT c.id as id, " +
            "CASE WHEN c.nameZh IS NOT NULL AND c.nameZh != '' THEN c.nameZh ELSE c.name END as label " +
            "FROM CountryBean c ORDER BY " +
            "CASE WHEN c.nameZh IS NOT NULL AND c.nameZh != '' THEN c.nameZh ELSE c.name END")
    List<OptionView> findOptions();

    Optional<CountryBean> findByName(String name);
}
