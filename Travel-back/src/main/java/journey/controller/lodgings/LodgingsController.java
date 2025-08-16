package journey.controller.lodgings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.dto.lodgings.LodgingHotSearchResponseDTO;
import journey.dto.lodgings.LodgingSearchRequestDTO;
import journey.dto.lodgings.SearchResponseDTO;
import journey.dto.lodgings.LodgingRoomTypeDetailDTO;
import journey.service.lodgings.LodgingsService;

@RestController
@RequestMapping("/api/lodgings")
public class LodgingsController {

    @Autowired
    private LodgingsService lodgingService;

    /** 首頁熱門旅宿（近 1 個月平均分 > 4，取前 6 筆） */
    @GetMapping("/hot")
    public List<LodgingHotSearchResponseDTO> getHotLodgings() {
        return lodgingService.getHotLodgings(6);
    }

    /* ---------- 搜尋 ---------- */
    @PostMapping("/search")
    public ResponseEntity<SearchResponseDTO> search(@RequestBody LodgingSearchRequestDTO request) {
        return ResponseEntity.ok(lodgingService.searchLodgingsWithFilterOptions(request));
    }

    /**
     * 獲取旅館和房型的詳細資訊
     * 包含旅館基本資訊、房型詳細資訊、圖片、床型配置、設施配置、可用性等。
     * 
     * @param lodgingId  旅館ID
     * @param roomTypeId 房型ID
     * @return LodgingRoomTypeDetailDTO 旅館房型詳細資訊
     */
    @GetMapping("/{lodgingId}/roomType/{roomTypeId}")
    public ResponseEntity<LodgingRoomTypeDetailDTO> getLodgingRoomTypeDetail(
            @PathVariable Integer lodgingId,
            @PathVariable Integer roomTypeId) {
        try {
            LodgingRoomTypeDetailDTO response = lodgingService.getLodgingRoomTypeDetail(lodgingId, roomTypeId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
