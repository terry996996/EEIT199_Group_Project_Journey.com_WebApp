package journey.service.lodgings;

import org.springframework.web.multipart.MultipartFile;

public interface VendorExcelUploadService {
    Object importLodgingsExcel(MultipartFile file, String type, Integer vendorId) throws Exception;
} 