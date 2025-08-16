// package journey.service.lodgings;

// import journey.dto.lodgings.LodgingRequestDTO;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobExecution;
// import org.springframework.batch.core.JobParameter;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// /**
//  * Spring Batch 批次啟動服務
//  * 
//  * 此服務負責啟動 Spring Batch Job，主要功能：
//  * 1. 接收前端傳來的住宿資料清單
//  * 2. 設定 Job 執行參數（如時間戳記、廠商ID等）
//  * 3. 啟動批次處理 Job
//  * 
//  * 注意事項：
//  * - JobParameters 只能包含基本型別（String、Long、Date、Double）
//  * - 複雜物件（如 lodgingList）需要透過其他方式傳遞
//  * - 建議使用 Spring Context 或 JobExecutionContext 傳遞大量資料
//  * 
//  * @author Max
//  * @since 2025-07-14
//  */
// @Service
// public class LodgingBatchService {

//     /**
//      * Spring Batch Job 啟動器
//      * 負責啟動和管理批次作業
//      */
//     @Autowired
//     private JobLauncher jobLauncher;
    
//     /**
//      * 住宿資料批次處理 Job
//      * 在 LodgingBatchConfig 中定義
//      */
//     @Autowired
//     private Job lodgingJob;

//     /**
//      * 執行住宿資料批次匯入
//      * 
//      * @param lodgingList 要匯入的住宿資料清單
//      * @param vendorId 廠商ID
//      * @throws Exception 批次執行異常
//      */
//     public void runBatch(List<LodgingRequestDTO> lodgingList, Integer vendorId) throws Exception {
//         // 建立 Job 執行參數
//         Map<String, JobParameter<?>> params = new HashMap<>();
//         params.put("time", new JobParameter<>(System.currentTimeMillis(), Long.class));
//         params.put("vendorId", new JobParameter<>(vendorId.longValue(), Long.class));
        
//         // 注意：lodgingList 無法直接放入 JobParameters
//         // 建議做法：
//         // 1. 將資料暫存到資料庫或 Redis
//         // 2. 在 ItemReader 中讀取暫存資料
//         // 3. 或使用 Spring Context 傳遞資料
        
//         JobParameters jobParameters = new JobParameters(params);
        
//         // 啟動批次處理 Job
//         JobExecution jobExecution = jobLauncher.run(lodgingJob, jobParameters);
        
//         // 可選擇記錄執行結果
//         System.out.println("批次處理完成，Job ID: " + jobExecution.getJobId());
//     }
// }