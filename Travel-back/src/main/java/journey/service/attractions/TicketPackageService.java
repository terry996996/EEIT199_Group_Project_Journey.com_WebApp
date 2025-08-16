package journey.service.attractions;

import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.attractiontickets.TicketPackagesBean;
import journey.domain.attractiontickets.TicketTypesBean;
import journey.dto.attractions.TicketPackageDto;
import journey.dto.attractions.TicketTypeDto;
import journey.repository.attractions.AttractionTicketRepository;
import journey.repository.attractions.TicketPackagesRepository;
import journey.repository.attractions.TicketTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketPackageService {

    @Autowired
    private TicketPackagesRepository ticketPackagesRepository;

    @Autowired
    private TicketTypesRepository ticketTypesRepository;

    @Autowired
    private AttractionTicketRepository attractionTicketRepository;

    /**
     * 新增套票及其子票種
     */
    public TicketPackagesBean createWithTypes(TicketPackageDto dto, List<TicketTypeDto> typeDtos) {
        AttractionTicketsBean ticket = attractionTicketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("找不到票券 ID: " + dto.getTicketId()));

        TicketPackagesBean pkg = new TicketPackagesBean();
        pkg.setTicket(ticket);
        pkg.setName(dto.getName());
        pkg.setDescription(dto.getDescription());
        pkg.setImageUrl(dto.getImageUrl());
        pkg.setImageType(dto.getImageType());
        pkg.setStartTime(dto.getStartTime());
        pkg.setEndTime(dto.getEndTime());
        pkg.setCreatedBy(dto.getCreatedBy());
        pkg.setCreatedAt(Optional.ofNullable(dto.getCreatedAt())
                .orElseGet(() -> new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()));
        pkg.setState(Optional.ofNullable(dto.getState()).orElse(true));

        TicketPackagesBean savedPkg = ticketPackagesRepository.save(pkg);

        // 新增子票種（使用單一 date 欄位）
        if (typeDtos != null) {
            for (TicketTypeDto typeDto : typeDtos) {
                TicketTypesBean type = new TicketTypesBean();
                type.setTicketPackage(savedPkg);
                type.setTicketName(typeDto.getTicketName());
                type.setPrice(typeDto.getPrice());
                type.setQuantity(typeDto.getQuantity());
                type.setDate(typeDto.getDate()); // 改為使用 date 欄位
                ticketTypesRepository.save(type);
            }
        }

        return savedPkg;
    }

    /**
     * 修改套票基本資訊（不含子票種）
     */
    public TicketPackagesBean update(Integer id, TicketPackageDto dto) {
        TicketPackagesBean pkg = ticketPackagesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到套票 ID: " + id));

        pkg.setName(dto.getName());
        pkg.setDescription(dto.getDescription());
        pkg.setImageUrl(dto.getImageUrl());
        pkg.setImageType(dto.getImageType());
        pkg.setStartTime(dto.getStartTime());
        pkg.setEndTime(dto.getEndTime());
        pkg.setUpdatedBy(dto.getUpdatedBy());
        pkg.setUpdatedAt(dto.getUpdatedAt());
        pkg.setState(dto.getState());

        return ticketPackagesRepository.save(pkg);
    }

    /**
     * 刪除套票（連帶票種）
     */
    public boolean delete(Integer id) {
        if (ticketPackagesRepository.existsById(id)) {
            ticketPackagesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 查詢某票券下所有套票
     */
    public List<TicketPackagesBean> findByTicketId(Integer ticketId) {
        return ticketPackagesRepository.findByTicket_Id(ticketId);
    }

    /**
     * 查詢某套票下的所有票種
     */
    public List<TicketTypesBean> findTypesByPackageId(Integer packageId) {
        return ticketTypesRepository.findByTicketPackage_Id(packageId);
    }
}
