package journey.controller.attractions;

import journey.domain.attractiontickets.TicketImagesBean;
import journey.dto.attractions.TicketImageDto;
import journey.service.attractions.TicketImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/images")
public class TicketImageController {

    @Autowired
    private TicketImageService ticketImageService;

    /**
     * 新增票券圖片（單筆）
     */
    @PostMapping
    public TicketImagesBean createImage(@RequestBody TicketImageDto dto) {
        return ticketImageService.create(dto);
    }

    /**
     * 修改票券圖片
     */
    @PutMapping("/{id}")
    public TicketImagesBean updateImage(@PathVariable Integer id,
            @RequestBody TicketImageDto dto) {
        return ticketImageService.update(id, dto);
    }

    /**
     * 刪除圖片
     */
    @DeleteMapping("/{id}")
    public boolean deleteImage(@PathVariable Integer id) {
        return ticketImageService.delete(id);
    }

    /**
     * 查詢單筆圖片
     */
    @GetMapping("/{id}")
    public TicketImagesBean getImage(@PathVariable Integer id) {
        return ticketImageService.findById(id);
    }

    /**
     * 查詢指定票券下所有圖片
     */
    @GetMapping("/ticket/{ticketId}")
    public List<TicketImagesBean> getImagesByTicketId(@PathVariable Integer ticketId) {
        return ticketImageService.findByTicketId(ticketId);
    }
}
