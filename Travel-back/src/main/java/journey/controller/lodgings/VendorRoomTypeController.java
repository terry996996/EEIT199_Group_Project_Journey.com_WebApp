package journey.controller.lodgings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.dto.lodgings.RoomTypeCreateRequestDTO;
import journey.dto.lodgings.RoomTypeCreateResponseDTO;
import journey.dto.lodgings.RoomTypeDetailDTO;
import journey.dto.lodgings.RoomTypeListDTO;
import journey.dto.lodgings.RoomTypeUpdateRequestDTO;
import journey.service.lodgings.VendorRoomTypeService;

@RestController
@RequestMapping("/api/vendor/lodgings")
public class VendorRoomTypeController {

    @Autowired
    private VendorRoomTypeService vendorRoomTypeService;

    /* ---------- 查詢飯店全部房型簡歷 ---------- */
    @GetMapping("/{lodgingId}/room-types")
    public ResponseEntity<List<RoomTypeListDTO>> getRoomTypes(@PathVariable Integer lodgingId) {
        try {
            List<RoomTypeListDTO> response = vendorRoomTypeService.getRoomTypes(lodgingId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 查詢單筆房型詳細 ---------- */
    @GetMapping("/{lodgingId}/room-types/{roomTypeId}")
    public ResponseEntity<RoomTypeDetailDTO> getRoomTypeDetail(
            @PathVariable Integer lodgingId,
            @PathVariable Integer roomTypeId) {
        try {
            RoomTypeDetailDTO response = vendorRoomTypeService.getRoomTypeDetail(lodgingId, roomTypeId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 新增房型 ---------- */
    @PostMapping("/{lodgingId}/room-types")
    public ResponseEntity<RoomTypeCreateResponseDTO> createRoomType(
            @PathVariable Integer lodgingId,
            @RequestBody RoomTypeCreateRequestDTO request) {
        try {
            request.setLodgingId(lodgingId);
            RoomTypeCreateResponseDTO response = vendorRoomTypeService.createRoomType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // 處理驗證錯誤
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // 處理其他錯誤
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 更新房型資料 ---------- */
    @PutMapping("/{lodgingId}/room-types/{roomTypeId}")
    public ResponseEntity<RoomTypeDetailDTO> updateRoomType(
            @PathVariable Integer lodgingId,
            @PathVariable Integer roomTypeId,
            @RequestBody RoomTypeUpdateRequestDTO request) {
        try {
            RoomTypeDetailDTO response = vendorRoomTypeService.updateRoomType(lodgingId, roomTypeId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 刪除房型資料 ---------- */
    @DeleteMapping("/{lodgingId}/room-types/{roomTypeId}")
    public ResponseEntity<?> deleteRoomType(
            @PathVariable Integer lodgingId,
            @PathVariable Integer roomTypeId) {
        try {
            vendorRoomTypeService.softDeleteRoomType(lodgingId, roomTypeId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            // 房型不存在
            return ResponseEntity.status(404)
                    .body("{\"error\": \"Room type not found\"}");
        } catch (SecurityException e) {
            // 權限不足
            return ResponseEntity.status(403)
                    .body("{\"error\": \"Access denied\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
