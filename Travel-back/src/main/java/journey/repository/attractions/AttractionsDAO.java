package journey.repository.attractions;

import journey.domain.attractiontickets.AttractionsBean;
import journey.dto.attractions.AttractionSearchDto;

//記得加JSON的專案
import java.util.List;

public interface AttractionsDAO {

    List<AttractionsBean> find(AttractionSearchDto dto);

    long count(AttractionSearchDto dto);
}