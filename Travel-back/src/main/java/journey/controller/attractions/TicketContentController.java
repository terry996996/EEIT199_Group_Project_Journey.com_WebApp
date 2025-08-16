package journey.controller.attractions;

import journey.domain.attractiontickets.TicketContentsBean;
import journey.dto.attractions.TicketContentDto;
import journey.service.attractions.TicketContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/contents")
public class TicketContentController {

    @Autowired
    private TicketContentService ticketContentService;

    /**
     * 新增票券內文
     */
    @PostMapping
    public TicketContentsBean create(@RequestBody TicketContentDto dto) {
        return ticketContentService.create(dto);
    }

    /**
     * 修改票券內文
     */
    @PutMapping("/{id}")
    public TicketContentsBean update(@PathVariable Integer id,
            @RequestBody TicketContentDto dto) {
        return ticketContentService.update(id, dto);
    }

    /**
     * 刪除票券內文
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return ticketContentService.delete(id);
    }

    /**
     * 查詢單筆內文
     */
    @GetMapping("/{id}")
    public TicketContentsBean findById(@PathVariable Integer id) {
        return ticketContentService.findById(id);
    }

    /**
     * 查詢某票券的所有內文
     */
    @GetMapping("/ticket/{ticketId}")
    public List<TicketContentsBean> findByTicketId(@PathVariable Integer ticketId) {
        return ticketContentService.findByTicketId(ticketId);
    }

    // 搜尋功能已排除，另獨立定義過
}
