package journey.controller.attractions;

import journey.domain.attractiontickets.TicketTypesBean;
import journey.dto.attractions.TicketTypeDto;
import journey.service.attractions.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-types")
public class TicketTypeController {

    @Autowired
    private TicketTypeService ticketTypeService;

    /**
     * 新增票種
     */
    @PostMapping
    public TicketTypesBean create(@RequestBody TicketTypeDto dto) {
        return ticketTypeService.create(dto);
    }

    /**
     * 修改票種
     */
    @PutMapping("/{id}")
    public TicketTypesBean update(@PathVariable Integer id, @RequestBody TicketTypeDto dto) {
        return ticketTypeService.update(id, dto);
    }

    /**
     * 刪除票種
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return ticketTypeService.delete(id);
    }

    /**
     * 查詢單一票種
     */
    @GetMapping("/{id}")
    public TicketTypesBean getById(@PathVariable Integer id) {
        return ticketTypeService.findById(id);
    }

    /**
     * 🔍 查詢某套票下所有票種
     */
    @GetMapping("/package/{packageId}")
    public List<TicketTypesBean> getByPackageId(@PathVariable Integer packageId) {
        return ticketTypeService.findByPackageId(packageId);
    }
}
