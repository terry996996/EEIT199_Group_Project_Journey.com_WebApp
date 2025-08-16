package journey.controller.attractions;

import journey.domain.attractiontickets.TicketPackagesBean;
import journey.domain.attractiontickets.TicketTypesBean;
import journey.dto.attractions.TicketPackageDto;
import journey.dto.attractions.TicketTypeDto;
import journey.service.attractions.TicketPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/packages")
public class TicketPackageController {

    @Autowired
    private TicketPackageService ticketPackageService;

    /**
     * 新增套票及票種
     */
    @PostMapping("/full")
    public TicketPackagesBean createFullPackage(@RequestBody TicketPackageFullRequest request) {
        return ticketPackageService.createWithTypes(request.getPackageDto(), request.getTypeDtos());
    }

    /**
     * 修改套票資訊（不含票種）
     */
    @PutMapping("/{id}")
    public TicketPackagesBean updatePackage(@PathVariable Integer id, @RequestBody TicketPackageDto dto) {
        return ticketPackageService.update(id, dto);
    }

    /**
     * 刪除套票（含子票種）
     */
    @DeleteMapping("/{id}")
    public boolean deletePackage(@PathVariable Integer id) {
        return ticketPackageService.delete(id);
    }

    /**
     * 查詢某票券下所有套票
     */
    @GetMapping("/ticket/{ticketId}")
    public List<TicketPackagesBean> getPackagesByTicketId(@PathVariable Integer ticketId) {
        return ticketPackageService.findByTicketId(ticketId);
    }

    /**
     * 查詢某套票下所有票種
     */
    @GetMapping("/{packageId}/types")
    public List<TicketTypesBean> getTypesByPackageId(@PathVariable Integer packageId) {
        return ticketPackageService.findTypesByPackageId(packageId);
    }

    /**
     * 輔助封裝類：用於新增套票與子票種（POST 請求 JSON 格式）
     */
    public static class TicketPackageFullRequest {
        private TicketPackageDto packageDto;
        private List<TicketTypeDto> typeDtos;

        public TicketPackageDto getPackageDto() {
            return packageDto;
        }

        public void setPackageDto(TicketPackageDto packageDto) {
            this.packageDto = packageDto;
        }

        public List<TicketTypeDto> getTypeDtos() {
            return typeDtos;
        }

        public void setTypeDtos(List<TicketTypeDto> typeDtos) {
            this.typeDtos = typeDtos;
        }
    }
}
