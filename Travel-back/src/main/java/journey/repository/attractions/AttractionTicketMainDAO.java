package journey.repository.attractions;

import journey.dto.attractions.AttractionSearchDto;
import journey.dto.attractions.AttractionTicketMainDto;

import java.util.List;
import journey.domain.attractiontickets.AttractionTicketsBean;

public interface AttractionTicketMainDAO {

    // 現有方法
    List<AttractionTicketsBean> find(AttractionSearchDto dto);

    long count(AttractionSearchDto dto);

    // 加在這裡！
    List<AttractionTicketMainDto> searchMainTickets(AttractionSearchDto dto);

}
