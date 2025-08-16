package journey.service.attractions;

import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.attractiontickets.TicketContentsBean;
import journey.dto.attractions.TicketContentDto;
import journey.repository.attractions.AttractionTicketRepository;
import journey.repository.attractions.TicketContentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketContentService {

    @Autowired
    private TicketContentsRepository ticketContentsRepository;

    @Autowired
    private AttractionTicketRepository attractionTicketRepository;

    /**
     * 新增內文（指定票券）
     */
    public TicketContentsBean create(TicketContentDto dto) {
        AttractionTicketsBean ticket = attractionTicketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("找不到票券 ID: " + dto.getTicketId()));

        TicketContentsBean content = new TicketContentsBean();
        content.setTicket(ticket);
        content.setTitle(dto.getTitle());
        content.setSubtitle(dto.getSubtitle());
        content.setDescription(dto.getDescription()); // HTML 內文

        return ticketContentsRepository.save(content);
    }

    /**
     * 修改內文
     */
    public TicketContentsBean update(Integer id, TicketContentDto dto) {
        TicketContentsBean content = ticketContentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到內文 ID: " + id));

        content.setTitle(dto.getTitle());
        content.setSubtitle(dto.getSubtitle());
        content.setDescription(dto.getDescription());

        return ticketContentsRepository.save(content);
    }

    /**
     * 刪除內文
     */
    public boolean delete(Integer id) {
        if (ticketContentsRepository.existsById(id)) {
            ticketContentsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 查詢單筆內文
     */
    public TicketContentsBean findById(Integer id) {
        return ticketContentsRepository.findById(id).orElse(null);
    }

    /**
     * 查詢某票券的所有內文
     */
    public List<TicketContentsBean> findByTicketId(Integer ticketId) {
        return ticketContentsRepository.findByTicket_IdOrderByContentIdAsc(ticketId);
    }

}
