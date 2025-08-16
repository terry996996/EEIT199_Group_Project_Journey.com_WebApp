// package journey.batch;

// import journey.dto.lodgings.LodgingRequestDTO;
// import journey.service.lodgings.VendorLodgingService;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobExecution;
// import org.springframework.batch.core.JobExecutionListener;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.item.ItemProcessor;
// import org.springframework.batch.item.ItemReader;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.batch.item.support.ListItemReader;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;

// import java.util.List;

// /**
//  * Spring Batch 批次匯入 Lodging 設定
//  * 
//  * 此配置類別定義了住宿資料批次處理的完整流程：
//  * 1. Job：定義整個批次作業
//  * 2. Step：定義批次處理步驟
//  * 3. ItemReader：讀取資料來源
//  * 4. ItemProcessor：處理和轉換資料
//  * 5. ItemWriter：寫入資料到目標位置
//  * 
//  * 主要功能：
//  * - 批次匯入住宿資料
//  * - 支援多筆資料同時處理
//  * - 提供錯誤處理和監控機制
//  * - 可擴展為更複雜的批次處理流程
//  * 
//  * @author Max
//  * @since 2025-07-14
//  */
// @Configuration
// public class LodgingBatchConfig {

//     @Autowired
//     private VendorLodgingService vendorLodgingService;

//     /**
//      * 定義住宿資料批次處理 Job
//      * 
//      * @param lodgingStep 住宿資料處理步驟
//      * @param jobRepository Job 儲存庫
//      * @return 配置好的 Job
//      */
//     @Bean
//     public Job lodgingJob(Step lodgingStep, JobRepository jobRepository) {
//         return new JobBuilder("lodgingJob", jobRepository)
//                 .listener(lodgingJobListener())
//                 .start(lodgingStep)
//                 .build();
//     }

//     /**
//      * 定義住宿資料處理步驟
//      * 
//      * @param lodgingReader 資料讀取器
//      * @param lodgingProcessor 資料處理器
//      * @param lodgingWriter 資料寫入器
//      * @param jobRepository Job 儲存庫
//      * @param transactionManager 交易管理器
//      * @return 配置好的 Step
//      */
//     @Bean
//     public Step lodgingStep(ItemReader<LodgingRequestDTO> lodgingReader,
//                            ItemProcessor<LodgingRequestDTO, LodgingRequestDTO> lodgingProcessor,
//                            ItemWriter<LodgingRequestDTO> lodgingWriter,
//                            JobRepository jobRepository,
//                            PlatformTransactionManager transactionManager) {
//         return new StepBuilder("lodgingStep", jobRepository)
//                 .<LodgingRequestDTO, LodgingRequestDTO>chunk(10, transactionManager)
//                 .reader(lodgingReader)
//                 .processor(lodgingProcessor)
//                 .writer(lodgingWriter)
//                 .build();
//     }

//     /**
//      * 住宿資料讀取器
//      * 從暫存資料或資料來源讀取住宿資料
//      * 
//      * @return 配置好的 ItemReader
//      */
//     @Bean
//     public ItemReader<LodgingRequestDTO> lodgingReader() {
//         // 這裡可以從資料庫、檔案、或其他來源讀取資料
//         // 目前使用空的 List，實際使用時需要注入資料來源
//         List<LodgingRequestDTO> lodgingList = List.of();
//         return new ListItemReader<>(lodgingList);
//     }

//     /**
//      * 住宿資料處理器
//      * 對讀取的資料進行驗證、轉換、或額外處理
//      * 
//      * @return 配置好的 ItemProcessor
//      */
//     @Bean
//     public ItemProcessor<LodgingRequestDTO, LodgingRequestDTO> lodgingProcessor() {
//         return lodging -> {
//             // 在這裡可以加入資料驗證邏輯
//             if (lodging == null) {
//                 return null; // 跳過 null 資料
//             }
            
//             // 範例：驗證必要欄位
//             if (lodging.getLodgingName() == null || lodging.getLodgingName().trim().isEmpty()) {
//                 System.out.println("跳過無效的住宿資料：名稱為空");
//                 return null;
//             }
            
//             // 範例：資料轉換或清理
//             lodging.setLodgingName(lodging.getLodgingName().trim());
            
//             return lodging;
//         };
//     }

//     /**
//      * 住宿資料寫入器
//      * 將處理後的資料寫入到目標位置（如資料庫）
//      * 
//      * @return 配置好的 ItemWriter
//      */
//     @Bean
//     public ItemWriter<LodgingRequestDTO> lodgingWriter() {
//         return items -> {
//             for (LodgingRequestDTO lodging : items) {
//                 try {
//                     // 呼叫 Service 方法建立住宿資料
//                     vendorLodgingService.createLodging(lodging, 1); // vendorId 需要從 Job 參數取得
//                     System.out.println("成功處理住宿資料：" + lodging.getLodgingName());
//                 } catch (Exception e) {
//                     System.err.println("處理住宿資料失敗：" + lodging.getLodgingName() + ", 錯誤：" + e.getMessage());
//                     throw e; // 重新拋出異常，讓 Spring Batch 處理
//                 }
//             }
//         };
//     }

//     /**
//      * Job 執行監聽器
//      * 用於監控 Job 執行狀態和記錄日誌
//      * 
//      * @return 配置好的 JobExecutionListener
//      */
//     @Bean
//     public JobExecutionListener lodgingJobListener() {
//         return new JobExecutionListener() {
//             @Override
//             public void beforeJob(JobExecution jobExecution) {
//                 System.out.println("開始執行住宿資料批次匯入，Job ID: " + jobExecution.getJobId());
//             }

//             @Override
//             public void afterJob(JobExecution jobExecution) {
//                 if (jobExecution.getStatus().isUnsuccessful()) {
//                     System.err.println("住宿資料批次匯入失敗，Job ID: " + jobExecution.getJobId());
//                 } else {
//                     System.out.println("住宿資料批次匯入完成，Job ID: " + jobExecution.getJobId());
//                 }
//             }
//         };
//     }
// } 