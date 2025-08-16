package journey.controller.lodgings;

/**
 * 商家住宿管理控制器
 * 
 * @author Max
 * @since 2025-07-08
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import journey.dto.lodgings.LodgingRequestDTO;
import journey.dto.lodgings.LodgingResponseDTO;
import journey.dto.lodgings.VendorLodgingDetailDTO;
import journey.dto.lodgings.VendorLodgingListDTO;
import journey.service.lodgings.VendorLodgingService;

@RestController
@RequestMapping("/api/vendor/lodging")

public class VendorLodgingController {

    @Autowired
    private VendorLodgingService vendorLodgingService;

    /* ---------- 我的旅宿列表（支援狀態篩選） ---------- */
    @GetMapping
    public ResponseEntity<List<VendorLodgingListDTO>> getVendorLodgings(
            @RequestParam(required = false) Integer vendorId,
            @RequestParam(required = false, defaultValue = "active") String status) {
        try {
            List<VendorLodgingListDTO> response = vendorLodgingService.getVendorLodgings(vendorId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 獲取單筆旅宿詳細資料 ---------- */
    @GetMapping("/{id}")
    public ResponseEntity<VendorLodgingDetailDTO> getLodgingDetail(@PathVariable Integer id) {
        try {
            VendorLodgingDetailDTO response = vendorLodgingService.getLodgingDetail(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 新增住宿 ---------- */
    @PostMapping
    public ResponseEntity<LodgingResponseDTO> createLodging(@RequestBody LodgingRequestDTO request,
                                                           @RequestParam(value = "vendorId", required = false) Integer vendorId) {
        try {
            if (vendorId == null) vendorId = 2002; // 預設 Sheraton
            LodgingResponseDTO response = vendorLodgingService.createLodging(request, vendorId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 處理驗證錯誤
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // 處理其他錯誤
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Max 2024-07-14 專用：批次匯入住宿資料（支援 Excel/多筆 DTO）
     * @author Max
     * @since 2024-07-14
     * @param dtos 住宿請求 DTO 清單
     * @param vendorId 商家ID
     * @return 每筆匯入結果訊息
     */
    @PostMapping("/batch")
    public ResponseEntity<List<String>> batchCreateLodgings(
            @RequestBody List<LodgingRequestDTO> dtos,
            @RequestParam Integer vendorId) {
        List<String> results = vendorLodgingService.createLodgings(dtos, vendorId);
        return ResponseEntity.ok(results);
    }

    /* ---------- 更新旅宿資料 ---------- */
    @PutMapping("/{id}")
    public ResponseEntity<LodgingResponseDTO> updateLodging(@PathVariable Integer id,
            @RequestBody LodgingRequestDTO request) {
        try {
            LodgingResponseDTO response = vendorLodgingService.updateLodging(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 軟刪除旅宿 ---------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLodging(@PathVariable Integer id) {
        try {
            vendorLodgingService.softDeleteLodging(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            // 旅宿不存在
            return ResponseEntity.status(404)
                    .body("{\"error\": \"Lodging not found\"}");
        } catch (SecurityException e) {
            // 權限不足
            return ResponseEntity.status(403)
                    .body("{\"error\": \"Access denied\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 軟刪還原旅宿 ---------- */
    @PostMapping("/{id}/restore")
    public ResponseEntity<?> restoreLodging(@PathVariable Integer id,
            @RequestParam(required = false) Integer vendorId) {
        try {
            vendorLodgingService.restoreLodging(id, vendorId);
            return ResponseEntity.ok().body("{\"message\": \"旅宿已還原\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 停業旅宿 ---------- */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateLodging(@PathVariable Integer id,
            @RequestParam(required = false) Integer vendorId) {
        try {
            vendorLodgingService.deactivateLodging(id, vendorId);
            return ResponseEntity.ok().body("{\"message\": \"旅宿已停業\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /* ---------- 重新上架旅宿 ---------- */
    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateLodging(@PathVariable Integer id,
            @RequestParam(required = false) Integer vendorId) {
        try {
            vendorLodgingService.activateLodging(id, vendorId);
            return ResponseEntity.ok().body("{\"message\": \"旅宿已重新上架\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}