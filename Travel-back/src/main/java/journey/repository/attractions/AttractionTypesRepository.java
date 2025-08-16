package journey.repository.attractions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import journey.domain.attractiontickets.AttractionTypesBean;
import journey.projection.OptionView;
import org.springframework.data.jpa.repository.Query; // @Query 標註
import java.util.List; // List 回傳型別
import java.util.Optional;

@Repository
public interface AttractionTypesRepository extends JpaRepository<AttractionTypesBean, Integer> {

@Query("SELECT t.id AS id, t.type AS label FROM AttractionTypesBean t")
List<OptionView> findOptions();
}
