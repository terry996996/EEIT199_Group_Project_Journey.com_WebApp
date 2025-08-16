// package journey.controller.lodgings;

// import journey.dto.lodgings.LodgingRequestDTO;
// import journey.service.lodgings.LodgingBatchService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// /**
//  * Spring Batch Lodging 批次匯入 API
//  * 
//  * 此控制器提供住宿資料批次匯入的 REST API 端點：
//  * 1. POST /api/vendor/lodging/batch - 批次匯入住宿資料
//  * 2. 支援多筆住宿資料同時處理
//  * 3. 提供批次處理狀態回饋
//  * 
//  * 主要功能：
//  * - 接收前端傳來的住宿資料清單
//  * - 啟動 Spring Batch 批次處理
//  * - 回傳批次處理結果
//  * - 錯誤處理和異常回饋
//  * 
//  * 使用方式：
//  * POST /api/vendor/lodging/batch
//  * Content-Type: application/json
//  * {
//  *   "lodgingList": [...],
//  *   "vendorId": 1
//  * }
//  * 
//  * @author Max
//  * @since 2025-07-14
//  */
// @RestController
// @RequestMapping("/api/vendor/lodging")
// public class LodgingBatchController {

//     @Autowired
//     private LodgingBatchService lodgingBatchService;

//     /**
//      * 批次匯入住宿資料
//      * 
//      * 此端點接收住宿資料清單和廠商ID，啟動 Spring Batch 批次處理：
//      * 1. 驗證輸入資料
//      * 2. 啟動批次處理 Job
//      * 3. 回傳處理結果
//      * 
//      * @param request 包含住宿資料清單和廠商ID的請求物件
//      * @return 批次處理結果
//      */
//     @PostMapping("/batch")
//     public ResponseEntity<BatchImportResponse> batchImportLodgings(@RequestBody BatchImportRequest request) {
//         try {
//             // 驗證輸入資料
//             if (request.getLodgingList() == null || request.getLodgingList().isEmpty()) {
//                 return ResponseEntity.badRequest()
//                     .body(new BatchImportResponse(false, "住宿資料清單不能為空", null));
//             }

//             if (request.getVendorId() == null) {
//                 return ResponseEntity.badRequest()
//                     .body(new BatchImportResponse(false, "廠商ID不能為空", null));
//             }

//             // 啟動批次處理
//             lodgingBatchService.runBatch(request.getLodgingList(), request.getVendorId());

//             return ResponseEntity.ok(new BatchImportResponse(
//                 true, 
//                 "批次匯入已啟動，共 " + request.getLodgingList().size() + " 筆資料", 
//                 request.getLodgingList().size()
//             ));

//         } catch (Exception e) {
//             // 記錄錯誤日誌
//             System.err.println("批次匯入住宿資料失敗：" + e.getMessage());
//             e.printStackTrace();

//             return ResponseEntity.internalServerError()
//                 .body(new BatchImportResponse(false, "批次匯入失敗：" + e.getMessage(), null));
//         }
//     }

//     /**
//      * 批次匯入請求物件
//      * 包含住宿資料清單和廠商ID
//      */
//     public static class BatchImportRequest {
//         private List<LodgingRequestDTO> lodgingList;
//         private Integer vendorId;

//         // 建構子
//         public BatchImportRequest() {}

//         public BatchImportRequest(List<LodgingRequestDTO> lodgingList, Integer vendorId) {
//             this.lodgingList = lodgingList;
//             this.vendorId = vendorId;
//         }

//         // Getter 和 Setter
//         public List<LodgingRequestDTO> getLodgingList() {
//             return lodgingList;
//         }

//         public void setLodgingList(List<LodgingRequestDTO> lodgingList) {
//             this.lodgingList = lodgingList;
//         }

//         public Integer getVendorId() {
//             return vendorId;
//         }

//         public void setVendorId(Integer vendorId) {
//             this.vendorId = vendorId;
//         }
//     }

//     /**
//      * 批次匯入回應物件
//      * 包含處理結果、訊息和處理數量
//      */
//     public static class BatchImportResponse {
//         private boolean success;
//         private String message;
//         private Integer processedCount;

//         // 建構子
//         public BatchImportResponse() {}

//         public BatchImportResponse(boolean success, String message, Integer processedCount) {
//             this.success = success;
//             this.message = message;
//             this.processedCount = processedCount;
//         }

//         // Getter 和 Setter
//         public boolean isSuccess() {
//             return success;
//         }

//         public void setSuccess(boolean success) {
//             this.success = success;
//         }

//         public String getMessage() {
//             return message;
//         }

//         public void setMessage(String message) {
//             this.message = message;
//         }

//         public Integer getProcessedCount() {
//             return processedCount;
//         }

//         public void setProcessedCount(Integer processedCount) {
//             this.processedCount = processedCount;
//         }
//     }
// } 