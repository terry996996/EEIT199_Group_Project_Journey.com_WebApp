/**
 * 商家 Excel 批次上傳 API
 * @author Max
 * @since 2025-07-10
 */
package journey.controller.lodgings;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import journey.service.lodgings.VendorExcelUploadService;

@RestController
@RequestMapping("/api/vendor")
public class VendorExcelUploadController {
    @Autowired
    private VendorExcelUploadService vendorExcelUploadService;

    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file,
                                         @RequestParam("type") String type,
                                         @RequestParam("vendorId") Integer vendorId) {
        try {
            var result = vendorExcelUploadService.importLodgingsExcel(file, type, vendorId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("上傳失敗: " + e.getMessage());
        }
    }
} 