package journey.service.comments;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.comments.CommentFeedbacksBean;
import journey.domain.comments.CommentImagesBean;
import journey.domain.comments.CommentsBean;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.lodgings.RoomTypesBean;
import journey.domain.users.AllUserBean;
import journey.dto.AuthorInfoDTO;
import journey.dto.BaseImageDTO;
import journey.dto.Comments.CommentImageResponseDTO;
import journey.dto.Comments.CommentRequestDTO;
import journey.dto.Comments.CommentResponseDTO;
import journey.dto.Comments.CommentUpdateDTO;
import journey.dto.Comments.LikeStatusResponseDTO;
import journey.enums.CommentTargetTypeEnum;
import journey.repository.attractions.AttractionTicketRepository;
import journey.repository.comments.CommentFeedbacksRepository;
import journey.repository.comments.CommentsRepository;
import journey.repository.lodgings.LodgingsRepository;
import journey.repository.lodgings.RoomTypesRepository;
import journey.repository.users.AllUserRepository;
import journey.service.UnifiedImageService;

/**
 * 評論服務類別
 * 
 * 功能說明：
 * - 處理評論的創建、查詢、更新、刪除等核心業務邏輯
 * - 支援多種評論目標：住宿、房型、交通票券、景點票券、行程規劃
 * - 支援主評論和回覆評論的層級結構
 * - 支援圖片上傳和管理
 * - 支援按讚功能和統計
 * - 支援軟刪除機制
 * 
 * 權限規則：
 * - USER: 可以發表主評論和回覆評論
 * - VENDOR: 可以回覆評論（針對自己的住宿/票券）
 * - ADMIN: 可以回覆評論和管理所有評論
 * 
 * 交易管理：
 * - 所有方法都使用 @Transactional 確保資料一致性
 * - 圖片上傳失敗會觸發整個交易回滾
 * 
 * @author Fan
 * @version 1.0
 * @since 07/12/2025
 */
@Service
@Transactional
public class CommentService {
    
    /** 評論資料存取層 */
    @Autowired
    private CommentsRepository commentsRepository;
    
    /** 使用者資料存取層 */
    @Autowired
    private AllUserRepository allUserRepository;

    /** 住宿資料存取層 */
    @Autowired
    private LodgingsRepository lodgingsRepository;
    
    /** 房型資料存取層 */
    @Autowired
    private RoomTypesRepository roomTypesRepository;

    /** 評論按讚資料存取層 */
    @Autowired
    private CommentFeedbacksRepository commentFeedbacksRepository;

    /** 統一圖片服務 */
    @Autowired
    private UnifiedImageService unifiedImageService;

    // 其他評論目標的 Repository（目前未啟用）
    // @Autowired
    // private TrafficTicketsRepository trafficTicketsRepository;
    // @Autowired
    // private AttractionTicketRepository attractionTicketsRepository;
    // @Autowired
    // private TripPlanRepository tripPlanRepository;

    /**
     * 創建新評論（純文字，無檔案上傳）
     * 
     * 功能說明：
     * - 創建新的評論記錄
     * - 支援主評論和回覆評論
     * - 支援圖片 URL 儲存（透過 DTO 中的 images 欄位）
     * - 自動設定系統欄位（創建時間、狀態等）
     * 
     * 權限驗證：
     * - 需要驗證使用者存在
     * - 需要驗證評論目標存在
     * - 回覆評論需要驗證父評論存在
     * 
     * 業務規則：
     * - 主評論可以包含評分（1-5分）
     * - 回覆評論不能包含評分
     * - 圖片數量限制為 5 張
     * - 圖片儲存失敗會觸發交易回滾
     * 
     * @param dto 評論創建請求資料
     * @return 創建成功的評論回應資料
     * @throws EntityNotFoundException 當使用者、評論目標或父評論不存在時
     * @throws IllegalArgumentException 當請求資料驗證失敗時
     * @throws RuntimeException 當圖片儲存失敗時
     */
    public CommentResponseDTO createComment(CommentRequestDTO dto) {

        CommentsBean comment = new CommentsBean();

        // 驗證與設定使用者
        AllUserBean user = allUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("使用者不存在"));
        comment.setUser(user);

        // 驗證並設定評論目標（5 擇 1）
        validateSingleCommentTarget(dto);
        applyCommentTarget(dto, comment);

        // 回覆留言（如有）
        if (dto.getParentCommentId() != null) {
            CommentsBean parent = commentsRepository.findWithImagesById(dto.getParentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("回覆目標留言不存在"));
            comment.setParentComment(parent);
        }

        // 評分與內容
        comment.setContent(dto.getContent());
        comment.setRating(dto.getParentCommentId() == null && dto.getRating() != null
                ? dto.getRating().byteValue()
                : null);

        // 系統欄位
        comment.setIsVerified(false);
        comment.setIsActive(true);
        comment.setDeleteStatus(1);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        CommentsBean saved = commentsRepository.save(comment);

        // 儲存圖片（如有）- 使用統一圖片服務
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            try {
                // 轉換為 BaseImageDTO
                List<BaseImageDTO> baseImageDtos = dto.getImages().stream()
                        .filter(imageDTO -> Boolean.TRUE.equals(imageDTO.getIsNew()))
                        .map(imageDTO -> BaseImageDTO.fromCommentImage(imageDTO, saved.getId()))
                        .collect(Collectors.toList());

                if (!baseImageDtos.isEmpty()) {
                    // 驗證圖片數量
                    unifiedImageService.validateImageCount(baseImageDtos, 5, "comment");

                    // 使用統一圖片服務儲存
                    List<BaseImageDTO> savedImages = unifiedImageService.saveImages(baseImageDtos, "comment");
                    System.out.println("✅ 已儲存 " + savedImages.size() + " 張留言圖片");
                }
            } catch (Exception e) {
                System.out.println("❌ 圖片儲存失敗: " + e.getMessage());
                // 拋出異常會觸發交易回滾，留言也會被刪除
                throw new RuntimeException("圖片儲存失敗，留言創建已回滾", e);
            }
        }

        // 回傳 DTO
        CommentsBean full = commentsRepository.findWithImagesById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("找不到剛新增的留言"));

        CommentResponseDTO response = convertToDTO(full);
        return response;
    }

    /**
     * 創建留言並處理檔案上傳（交易處理）
     * 
     * 功能說明：
     * - 創建新評論並同時處理檔案上傳
     * - 支援 multipart/form-data 格式的檔案上傳
     * - 自動處理檔案儲存路徑和命名
     * - 確保檔案上傳和評論創建的原子性
     * 
     * 檔案處理：
     * - 支援多個檔案同時上傳
     * - 自動驗證檔案類型和大小
     * - 使用統一圖片服務進行檔案儲存
     * - 檔案儲存路徑包含住宿ID和評論ID
     * 
     * 錯誤處理：
     * - 檔案上傳失敗會觸發整個交易回滾
     * - 評論創建失敗也會回滾已上傳的檔案
     * 
     * @param dto   留言資料
     * @param files 上傳的檔案列表
     * @return 創建的留言（包含圖片資訊）
     * @throws EntityNotFoundException 當使用者、評論目標或父評論不存在時
     * @throws RuntimeException 當檔案上傳失敗時
     */
    @Transactional
    public CommentResponseDTO createCommentWithFiles(CommentRequestDTO dto, List<MultipartFile> files) {
        System.out.println("🔄 開始創建留言並處理檔案上傳...");

        // 先創建留言（不包含圖片）
        CommentsBean comment = new CommentsBean();

        // 驗證與設定使用者
        AllUserBean user = allUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("使用者不存在"));
        comment.setUser(user);

        // 驗證並設定評論目標（5 擇 1）
        validateSingleCommentTarget(dto);
        applyCommentTarget(dto, comment);

        // 回覆留言（如有）
        if (dto.getParentCommentId() != null) {
            CommentsBean parent = commentsRepository.findWithImagesById(dto.getParentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("回覆目標留言不存在"));
            comment.setParentComment(parent);
        }

        // 評分與內容
        comment.setContent(dto.getContent());
        comment.setRating(dto.getParentCommentId() == null && dto.getRating() != null
                ? dto.getRating().byteValue()
                : null);

        // 系統欄位
        comment.setIsVerified(false);
        comment.setIsActive(true);
        comment.setDeleteStatus(1);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        // 儲存留言
        CommentsBean saved = commentsRepository.save(comment);
        System.out.println("✅ 留言創建成功，ID: " + saved.getId());

        // 處理檔案上傳
        if (files != null && !files.isEmpty()) {
            try {
                System.out.println("📤 開始處理 " + files.size() + " 個檔案...");

                // 取得 lodgingId 用於檔案儲存
                Integer lodgingId = null;
                if (saved.getLodging() != null) {
                    lodgingId = saved.getLodging().getId();
                    System.out.println("   - 從留言取得 lodgingId: " + lodgingId);
                } else if (saved.getRoomType() != null) {
                    lodgingId = saved.getRoomType().getLodging().getId();
                    System.out.println("   - 從房型取得 lodgingId: " + lodgingId);
                }

                if (lodgingId == null) {
                    throw new RuntimeException("無法取得住宿ID，無法儲存圖片");
                }

                // 使用 UnifiedImageService 儲存檔案（傳入 lodgingId）
                List<BaseImageDTO> savedImages = unifiedImageService.saveFiles(files, "comment", saved.getId(),
                        "comment", lodgingId);

                System.out.println("✅ 檔案上傳成功，儲存了 " + savedImages.size() + " 張圖片");

            } catch (Exception e) {
                System.out.println("❌ 檔案上傳失敗: " + e.getMessage());
                // 拋出異常會觸發交易回滾，留言也會被刪除
                throw new RuntimeException("檔案上傳失敗，留言創建已回滾", e);
            }
        }

        // 回傳完整的留言資料
        CommentsBean full = commentsRepository.findWithImagesById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("找不到剛新增的留言"));

        CommentResponseDTO response = convertToDTO(full);
        System.out.println(
                "🎉 留言創建完成，包含 " + (full.getCommentImages() != null ? full.getCommentImages().size() : 0) + " 張圖片");
        return response;
    }

    /**
     * 基本驗證：確保提供了評論目標類型和ID
     * 
     * @param dto 評論請求資料
     * @throws IllegalArgumentException 當 targetType 或 targetId 為空時
     */
    private void validateSingleCommentTarget(CommentRequestDTO dto) {
        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new IllegalArgumentException("請提供 targetType 與對應的 targetId");
        }
    }

    /**
     * 驗證並設定評論目標（5 擇 1）
     * 
     * 功能說明：
     * - 根據 targetType 設定對應的評論目標
     * - 驗證目標實體是否存在
     * - 目前支援：LODGING（住宿）、ROOM_TYPE（房型）
     * - 其他類型（交通票券、景點票券、行程規劃）目前未啟用
     * 
     * @param dto      評論請求資料
     * @param comment  評論實體
     * @throws EntityNotFoundException 當指定的評論目標不存在時
     */
    private void applyCommentTarget(CommentRequestDTO dto, CommentsBean comment) {
        switch (dto.getTargetType()) {
            // 設定評論目標（並驗證是否存在）
            case LODGING -> {
                LodgingsBean lodging = lodgingsRepository.findByIdLightweight(dto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("指定的旅館不存在"));
                comment.setLodging(lodging);
            }
            case ROOM_TYPE -> {
                RoomTypesBean room = roomTypesRepository.findByIdLightweight(dto.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("指定的房型不存在"));
                comment.setRoomType(room);
            }

            // 其他評論目標類型（目前未啟用）
            // case TRAFFIC_TICKET -> {
            // TrafficTicketsBean ticket =
            // trafficTicketsRepository.findById(dto.getTargetId())
            // .orElseThrow(() -> new EntityNotFoundException("指定的交通票券不存在"));
            // comment.setTrafficTicketId(ticket);
            // }
            // case ATTRACTION_TICKET -> {
            // AttractionTicketsBean ticket =
            // attractionTicketsRepository.findById(dto.getTargetId())
            // .orElseThrow(() -> new EntityNotFoundException("指定的景點票券不存在"));
            // comment.setAttractionTicketId(ticket);
            // }
            // case TRIP_PLAN -> {
            // TripPlanBean plan = tripPlanRepository.findById(dto.getTargetId())
            // .orElseThrow(() -> new EntityNotFoundException("指定的行程規劃不存在"));
            // comment.setTripPlanId(plan);
            // }
            default -> throw new IllegalArgumentException("不支援的評論目標類型: " + dto.getTargetType());
        }
    }

    /**
     * 將評論實體轉換為回應 DTO
     * 
     * 功能說明：
     * - 將 CommentsBean 實體轉換為 CommentResponseDTO
     * - 處理關聯資料的載入和轉換
     * - 設定預設的權限和按讚狀態
     * - 優化效能，只載入必要的欄位
     * 
     * 轉換內容：
     * - 基本評論資訊（ID、內容、評分、時間等）
     * - 作者資訊（ID、用戶名、頭像）
     * - 父評論 ID（用於回覆結構）
     * - 評論圖片列表
     * - 權限控制標誌
     * - 按讚狀態和數量
     * 
     * @param comment 評論實體
     * @return 評論回應 DTO
     */
    private CommentResponseDTO convertToDTO(CommentsBean comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setRating(comment.getRating());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setIsVerified(comment.getIsVerified());
        dto.setIsActive(comment.getIsActive());
        dto.setDeleteStatus(comment.getDeleteStatus());

        // 設定作者資訊（只載入必要欄位）
        if (comment.getUser() != null) {
            AuthorInfoDTO authorInfo = new AuthorInfoDTO();
            authorInfo.setId(comment.getUser().getId());
            authorInfo.setUsername(comment.getUser().getUsername());
            authorInfo.setAvatarUrl(null); // 暫時設為 null
            // 設定用戶類型
            if (comment.getUser().getUserType() != null) {
                authorInfo.setUserType(comment.getUser().getUserType().getType());
            }
            dto.setAuthor(authorInfo);
        }

        // 設定父留言 ID（只載入ID，不載入完整物件）
        if (comment.getParentComment() != null) {
            dto.setParentCommentId(comment.getParentComment().getId());
        }

        // 設定留言圖片
        if (comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()) {
            List<CommentImageResponseDTO> imageDTOs = comment.getCommentImages().stream()
                    .map(this::convertImageToDTO)
                    .collect(Collectors.toList());
            dto.setImages(imageDTOs);
        }

        // 設定權限（預設值，實際權限檢查在 Controller 層）
        dto.setCanEdit(true);
        dto.setCanDelete(true);
        dto.setCanReport(true);
        dto.setCanLike(true);

        // 設定按讚資訊（預設值，實際資料在 Controller 層設定）
        dto.setLikedByCurrentUser(false);
        dto.setLikeCount(0);

        return dto;
    }

    /**
     * 轉換留言圖片實體為回應 DTO
     * 
     * 功能說明：
     * - 將 CommentImagesBean 轉換為 CommentImageResponseDTO
     * - 處理圖片的狀態和排序資訊
     * - 設定圖片的刪除狀態
     * 
     * @param image 圖片實體
     * @return 圖片回應 DTO
     */
    private CommentImageResponseDTO convertImageToDTO(CommentImagesBean image) {
        CommentImageResponseDTO dto = new CommentImageResponseDTO();
        dto.setImageId(image.getId()); // 設定圖片主鍵，用於前端拖曳排序
        dto.setCommentImageUrl(image.getImageUrl());
        dto.setSortOrder(image.getSortOrder());
        dto.setIsActive(image.getIsActive());
        dto.setIsDeleted(image.getIsActive() != null && !image.getIsActive());
        dto.setMimeType(image.getMimeType());
        return dto;
    }

    /**
     * 獲取指定評論的所有圖片（包括無效的）- 供管理員使用
     * 
     * 功能說明：
     * - 獲取評論的所有圖片，包括已刪除的
     * - 主要用於管理員查看和管理
     * - 使用統一圖片服務進行查詢
     * 
     * @param commentId 評論 ID
     * @return 所有圖片列表（包括已刪除的）
     */
    public List<CommentImageResponseDTO> getAllImagesByCommentId(Integer commentId) {
        List<BaseImageDTO> allImages = unifiedImageService.getAllImagesByTarget(commentId, "comment");

        return allImages.stream()
                .filter(img -> img != null)
                .map(img -> {
                    CommentImageResponseDTO dto = new CommentImageResponseDTO();
                    dto.setImageId(img.getImageId()); // 設定圖片主鍵
                    dto.setCommentImageUrl(img.getImageUrl());
                    dto.setSortOrder(img.getSortOrder());
                    dto.setIsActive(img.getIsActive());
                    dto.setIsDeleted(img.getIsActive() != null && !img.getIsActive());
                    dto.setMimeType(img.getMimeType());
                    return dto;
                }).toList();
    }

    /**
     * 獲取指定評論的有效圖片
     * 
     * 功能說明：
     * - 只獲取狀態為有效的圖片
     * - 用於前端正常顯示
     * - 過濾掉已刪除的圖片
     * 
     * @param commentId 評論 ID
     * @return 有效圖片列表
     */
    public List<CommentImageResponseDTO> getActiveImagesByCommentId(Integer commentId) {
        List<BaseImageDTO> activeImages = unifiedImageService.getActiveImagesByTarget(commentId, "comment");

        return activeImages.stream()
                .filter(img -> img != null)
                .map(img -> {
                    CommentImageResponseDTO dto = new CommentImageResponseDTO();
                    dto.setImageId(img.getImageId());
                    dto.setCommentImageUrl(img.getImageUrl());
                    dto.setSortOrder(img.getSortOrder());
                    dto.setIsActive(img.getIsActive());
                    dto.setIsDeleted(false); // 有效圖片
                    dto.setMimeType(img.getMimeType());
                    return dto;
                }).toList();
    }

    /**
     * 根據 ID 獲取評論
     * 
     * 功能說明：
     * - 根據評論 ID 獲取完整的評論資訊
     * - 包含圖片、作者、按讚等關聯資料
     * - 如果評論不存在會拋出異常
     * 
     * @param id 評論 ID
     * @return 評論回應資料
     * @throws EntityNotFoundException 當評論不存在時
     */
    public CommentResponseDTO getCommentById(Integer id) {
        CommentsBean comment = commentsRepository.findWithImagesById(id)
                .orElseThrow(() -> new EntityNotFoundException("評論不存在"));
        return convertToDTO(comment);
    }

    /**
     * 更新評論圖片（交易處理）
     * 
     * 功能說明：
     * - 更新評論的圖片列表
     * - 支援新增、刪除、重新排序圖片
     * - 使用統一圖片服務進行處理
     * - 確保圖片更新的原子性
     * 
     * 業務規則：
     * - 圖片數量限制為 5 張
     * - 支援圖片排序
     * - 支援軟刪除圖片
     * 
     * @param commentId 評論 ID
     * @param imageDtos 圖片更新資料
     * @return 更新後的圖片列表
     * @throws IllegalArgumentException 當圖片數量超過限制時
     */
    @Transactional
    public List<BaseImageDTO> updateCommentImages(Integer commentId, List<BaseImageDTO> imageDtos) {
        // 驗證圖片數量
        unifiedImageService.validateImageCount(imageDtos, 5, "comment");

        // 使用統一圖片服務更新
        return unifiedImageService.updateImages(imageDtos, "comment");
    }

    /**
     * 根據目標類型和 ID 獲取評論列表
     * 
     * 功能說明：
     * - 獲取指定目標（住宿、房型等）的所有評論
     * - 支援包含或排除已刪除的評論
     * - 可選擇性載入按讚資訊
     * - 支援分頁查詢（未來擴展）
     * 
     * 查詢邏輯：
     * - 根據 targetType 和 targetId 篩選評論
     * - 可選擇是否包含已刪除的評論
     * - 按創建時間倒序排列
     * - 可選擇性批次載入按讚資訊以提升效能
     * 
     * @param targetType 目標類型（LODGING, ROOM_TYPE 等）
     * @param targetId 目標 ID
     * @param includeDeleted 是否包含已刪除的評論
     * @param includeLikeStatus 是否包含按讚狀態
     * @param userId 當前用戶ID（當includeLikeStatus=true時需要）
     * @return 評論列表
     */
    public List<CommentResponseDTO> getCommentsByTarget(String targetType, Integer targetId, 
            boolean includeDeleted, boolean includeLikeStatus, Integer userId) {
        List<CommentsBean> comments;

        // 根據目標類型查詢評論
        switch (CommentTargetTypeEnum.valueOf(targetType.toUpperCase())) {
            case LODGING -> {
                if (includeDeleted) {
                    comments = commentsRepository.findAllCommentsByLodgingIdOptimized(targetId);
                } else {
                    comments = commentsRepository.findActiveCommentsByLodgingIdOptimized(targetId);
                }
            }
            case ROOM_TYPE -> {
                if (includeDeleted) {
                    comments = commentsRepository.findAllCommentsByRoomTypeIdOptimized(targetId);
                } else {
                    comments = commentsRepository.findActiveCommentsByRoomTypeIdOptimized(targetId);
                }
            }
            // 其他目標類型的處理（目前未啟用）
            default -> throw new IllegalArgumentException("不支援的目標類型: " + targetType);
        }

        // 轉換為 DTO
        List<CommentResponseDTO> commentDTOs = comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 可選擇性批次載入按讚資訊
        if (includeLikeStatus && userId != null) {
            loadLikeInfoBatch(commentDTOs, userId);
        }

        return commentDTOs;
    }

    /**
     * 批次載入評論的按讚資訊
     * 
     * 功能說明：
     * - 批次查詢評論的按讚狀態和數量
     * - 優化效能，減少資料庫查詢次數
     * - 更新 DTO 中的按讚相關欄位
     * 
     * 實作細節：
     * - 收集所有評論 ID
     * - 批次查詢按讚數量
     * - 批次查詢按讚狀態（需要當前用戶 ID）
     * - 更新 DTO 物件
     * 
     * @param comments 評論 DTO 列表
     */
    private void loadLikeInfoBatch(List<CommentResponseDTO> comments) {
        if (comments.isEmpty()) {
            return;
        }

        // 收集評論 ID
        List<Integer> commentIds = comments.stream()
                .map(CommentResponseDTO::getId)
                .collect(Collectors.toList());

        // 批次查詢按讚數量
        Map<Integer, Integer> likeCounts = new HashMap<>();
        for (Integer commentId : commentIds) {
            int count = commentFeedbacksRepository.countLikesByCommentId(commentId);
            likeCounts.put(commentId, count);
        }

        // 更新按讚數量
        comments.forEach(comment -> {
            Integer likeCount = likeCounts.get(comment.getId());
            comment.setLikeCount(likeCount != null ? likeCount : 0);
        });

        // TODO: 批次查詢當前用戶的按讚狀態（需要當前用戶 ID）
        // 目前設為預設值，實際實作需要從 Security Context 獲取當前用戶
    }

    /**
     * 批次載入評論的按讚資訊
     * 
     * 功能說明：
     * - 批次查詢評論的按讚狀態和數量
     * - 優化效能，減少資料庫查詢次數
     * - 更新 DTO 中的按讚相關欄位
     * 
     * 實作細節：
     * - 收集所有評論 ID
     * - 批次查詢按讚數量
     * - 批次查詢按讚狀態（需要當前用戶 ID）
     * - 更新 DTO 物件
     * 
     * @param comments 評論 DTO 列表
     * @param userId 當前用戶ID
     */
    private void loadLikeInfoBatch(List<CommentResponseDTO> comments, Integer userId) {
        if (comments.isEmpty()) {
            return;
        }

        // 收集評論 ID
        List<Integer> commentIds = comments.stream()
                .map(CommentResponseDTO::getId)
                .collect(Collectors.toList());

        // 批次查詢按讚數量
        Map<Integer, Integer> likeCounts = new HashMap<>();
        for (Integer commentId : commentIds) {
            int count = commentFeedbacksRepository.countLikesByCommentId(commentId);
            likeCounts.put(commentId, count);
        }

        // 更新按讚數量
        comments.forEach(comment -> {
            Integer likeCount = likeCounts.get(comment.getId());
            comment.setLikeCount(likeCount != null ? likeCount : 0);
        });

        // 批次查詢用戶按讚狀態
        List<Object[]> userLikes = commentFeedbacksRepository.findUserLikesByCommentIds(commentIds, userId);
        List<Integer> userLikedCommentIds = userLikes.stream()
                .map(row -> (Integer) row[0])
                .collect(Collectors.toList());
        
        // 更新按讚狀態
        comments.forEach(comment -> {
            comment.setLikedByCurrentUser(userLikedCommentIds.contains(comment.getId()));
        });
    }

    /**
     * 更新評論（純文字，無檔案上傳）
     * 
     * 功能說明：
     * - 更新現有評論的內容和評分
     * - 支援圖片 URL 更新（透過 DTO 中的 images 欄位）
     * - 自動更新修改時間戳
     * - 確保更新操作的原子性
     * 
     * 權限驗證：
     * - 需要驗證評論存在
     * - 需要驗證評論狀態有效（未刪除）
     * - TODO: 需要驗證當前用戶是否有權限編輯
     * 
     * 業務規則：
     * - 只能更新內容和評分
     * - 只有主評論可以更新評分
     * - 圖片數量限制為 5 張
     * - 圖片更新失敗會觸發整個交易回滾
     * 
     * @param id 評論 ID
     * @param dto 更新資料
     * @return 更新後的評論
     * @throws EntityNotFoundException 當評論不存在時
     * @throws IllegalArgumentException 當評論已刪除或資料驗證失敗時
     * @throws RuntimeException 當圖片更新失敗時
     */
    @Transactional
    public CommentResponseDTO updateComment(Integer id, CommentUpdateDTO dto) {
        // 查找現有留言（使用輕量查詢）
        CommentsBean existingComment = commentsRepository.findWithImagesById(id)
                .orElseThrow(() -> new EntityNotFoundException("留言不存在"));

        // 驗證權限（這裡簡化處理，實際應該從 Security Context 獲取當前用戶）
        // TODO: 實作權限驗證邏輯
        // if (!existingComment.getUser().getId().equals(currentUserId)) {
        // throw new AccessDeniedException("無權限編輯此留言");
        // }

        // 驗證留言是否有效
        if (existingComment.getDeleteStatus() != 1 || !existingComment.getIsActive()) {
            throw new IllegalArgumentException("無法編輯已刪除的留言");
        }

        // 更新內容
        if (dto.getContent() != null && !dto.getContent().trim().isEmpty()) {
            existingComment.setContent(dto.getContent().trim());
        }

        // 更新評分（僅主留言可以更新評分）
        if (existingComment.getParentComment() == null && dto.getRating() != null) {
            existingComment.setRating(dto.getRating().byteValue());
        }

        // 更新時間戳
        existingComment.setUpdatedAt(LocalDateTime.now());

        // 儲存留言更新
        CommentsBean savedComment = commentsRepository.save(existingComment);

        // 處理圖片更新（如果有的話）
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            try {
                System.out.println("🖼️ 開始更新留言圖片，共 " + dto.getImages().size() + " 張");
                
                // 驗證圖片數量
                unifiedImageService.validateImageCount(dto.getImages(), 5, "comment");
                
                // 使用 UnifiedImageService 更新圖片
                List<BaseImageDTO> updatedImages = unifiedImageService.updateImages(dto.getImages(), "comment");
                
                System.out.println("✅ 圖片更新成功，共更新 " + updatedImages.size() + " 張圖片");
                
            } catch (Exception e) {
                System.err.println("❌ 圖片更新失敗: " + e.getMessage());
                // 拋出異常會觸發交易回滾，留言更新也會被回滾
                throw new RuntimeException("圖片更新失敗，留言更新已回滾", e);
            }
        }

        // 重新載入包含圖片的完整資料
        CommentsBean fullComment = commentsRepository.findWithImagesById(savedComment.getId())
                .orElseThrow(() -> new EntityNotFoundException("找不到更新後的留言"));

        return convertToDTO(fullComment);
    }

    /**
     * 更新評論（支援檔案上傳）
     * 
     * 功能說明：
     * - 更新評論內容和評分
     * - 支援新檔案上傳
     * - 自動處理檔案儲存路徑和命名
     * - 確保更新操作的原子性
     * 
     * 檔案處理：
     * - 支援多個檔案同時上傳
     * - 自動驗證檔案類型和大小
     * - 檔案儲存路徑包含住宿ID和評論ID
     * - 不處理現有圖片更新（由前端通過專用 API 管理）
     * 
     * 業務規則：
     * - 圖片數量限制為 5 張
     * - 檔案大小限制為 500KB
     * - 只支援圖片檔案類型
     * - 檔案上傳失敗會觸發整個交易回滾
     * 
     * @param id 評論 ID
     * @param dto 更新資料
     * @param files 新上傳的檔案列表
     * @return 更新後的評論
     * @throws EntityNotFoundException 當評論不存在時
     * @throws IllegalArgumentException 當評論已刪除或檔案驗證失敗時
     * @throws RuntimeException 當檔案上傳失敗時
     */
    @Transactional
    public CommentResponseDTO updateCommentWithFiles(Integer id, CommentUpdateDTO dto, List<MultipartFile> files) {
        System.out.println("🔄 開始更新留言並處理檔案上傳...");

        // 查找現有留言（使用輕量查詢）
        CommentsBean existingComment = commentsRepository.findWithImagesById(id)
                .orElseThrow(() -> new EntityNotFoundException("留言不存在"));

        // 驗證權限（這裡簡化處理，實際應該從 Security Context 獲取當前用戶）
        // TODO: 實作權限驗證邏輯
        // if (!existingComment.getUser().getId().equals(currentUserId)) {
        // throw new AccessDeniedException("無權限編輯此留言");
        // }

        // 驗證留言是否有效
        if (existingComment.getDeleteStatus() != 1 || !existingComment.getIsActive()) {
            throw new IllegalArgumentException("無法編輯已刪除的留言");
        }

        // 更新內容
        if (dto.getContent() != null && !dto.getContent().trim().isEmpty()) {
            existingComment.setContent(dto.getContent().trim());
        }

        // 更新評分（僅主留言可以更新評分）
        if (existingComment.getParentComment() == null && dto.getRating() != null) {
            existingComment.setRating(dto.getRating().byteValue());
        }

        // 更新時間戳
        existingComment.setUpdatedAt(LocalDateTime.now());

        // 儲存留言更新
        CommentsBean savedComment = commentsRepository.save(existingComment);

        // 處理檔案上傳（如果有的話）
        if (files != null && !files.isEmpty()) {
            try {
                System.out.println("📤 開始處理 " + files.size() + " 個新檔案...");

                // 驗證檔案
                if (files.size() > 5) {
                    throw new IllegalArgumentException("圖片數量不能超過5張");
                }
                
                for (MultipartFile file : files) {
                    if (file.isEmpty()) {
                        throw new IllegalArgumentException("檔案不能為空");
                    }
                    if (file.getSize() > 500 * 1024) { // 500KB
                        throw new IllegalArgumentException("檔案大小不能超過500KB");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        throw new IllegalArgumentException("只能上傳圖片檔案");
                    }
                }

                // 取得 lodgingId 用於檔案儲存
                Integer lodgingId = null;
                if (savedComment.getLodging() != null) {
                    lodgingId = savedComment.getLodging().getId();
                    System.out.println("   - 從留言取得 lodgingId: " + lodgingId);
                } else if (savedComment.getRoomType() != null) {
                    lodgingId = savedComment.getRoomType().getLodging().getId();
                    System.out.println("   - 從房型取得 lodgingId: " + lodgingId);
                }

                if (lodgingId == null) {
                    throw new RuntimeException("無法取得住宿ID，無法儲存圖片");
                }

                // 使用 UnifiedImageService 儲存檔案（傳入 lodgingId）
                List<BaseImageDTO> savedImages = unifiedImageService.saveFiles(files, "comment", savedComment.getId(),
                        "comment", lodgingId);

                System.out.println("✅ 新檔案上傳成功，儲存了 " + savedImages.size() + " 張圖片");

            } catch (Exception e) {
                System.err.println("❌ 檔案上傳失敗: " + e.getMessage());
                // 拋出異常會觸發交易回滾，留言更新也會被回滾
                throw new RuntimeException("檔案上傳失敗，留言更新已回滾", e);
            }
        }

        // 注意：不處理現有圖片更新，因為前端已經通過 /api/comments/{id}/images 同步了圖片狀態
        // 這裡只處理新上傳的檔案
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            System.out.println("⚠️ 檢測到現有圖片資料，但跳過處理（前端已同步）");
            System.out.println("   - 圖片數量: " + dto.getImages().size());
            System.out.println("   - 建議：前端應只發送新檔案，現有圖片狀態通過 /api/comments/{id}/images 管理");
        }

        // 重新載入包含圖片的完整資料
        CommentsBean fullComment = commentsRepository.findWithImagesById(savedComment.getId())
                .orElseThrow(() -> new EntityNotFoundException("找不到更新後的留言"));

        return convertToDTO(fullComment);
    }

    /**
     * 獲取指定評論的所有回覆
     * 
     * 功能說明：
     * - 獲取指定父評論的所有回覆
     * - 支援包含或排除已刪除的回覆
     * - 按創建時間升序排列（先回覆的在前）
     * - 自動載入回覆的圖片和作者資訊
     * 
     * 業務規則：
     * - 預設包含已刪除的回覆，避免刪除主評論時回覆一起消失
     * - 回覆按時間順序排列，便於閱讀
     * - 支援軟刪除的回覆查詢
     * 
     * @param parentCommentId 父評論 ID
     * @param includeDeleted 是否包含已刪除的回覆
     * @return 回覆列表
     */
    public List<CommentResponseDTO> getRepliesByParentCommentId(Integer parentCommentId, boolean includeDeleted) {
        List<CommentsBean> replies;

        if (includeDeleted) {
            replies = commentsRepository.findAllRepliesByParentCommentId(parentCommentId);
        } else {
            replies = commentsRepository.findActiveRepliesByParentCommentId(parentCommentId);
        }

        return replies.stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * 刪除評論（軟刪除）
     * 
     * 功能說明：
     * - 將評論標記為已刪除狀態
     * - 保留評論資料，但設定為不可見
     * - 自動設定刪除時間戳
     * - 支援權限驗證（TODO）
     * 
     * 軟刪除邏輯：
     * - 設定 deleteStatus = 0（用戶刪除）
     * - 設定 isActive = false
     * - 設定 deletedTime = 當前時間
     * - 保留所有關聯資料（圖片、按讚等）
     * 
     * 業務規則：
     * - 只能刪除自己的評論（TODO：實作權限驗證）
     * - 只能刪除正常狀態的評論
     * - 刪除後評論不可編輯或回覆
     * - 刪除後按讚功能停用
     * 
     * @param id 評論 ID
     * @return 被刪除的評論資訊
     * @throws EntityNotFoundException 當評論不存在時
     * @throws IllegalArgumentException 當評論已刪除時
     */
    public CommentResponseDTO deleteComment(Integer id) {
        // 查找現有留言（使用輕量查詢）
        CommentsBean existingComment = commentsRepository.findWithImagesById(id)
                .orElseThrow(() -> new EntityNotFoundException("留言不存在"));

        // 驗證權限（這裡簡化處理，實際應該從 Security Context 獲取當前用戶）
        // TODO: 實作權限驗證邏輯
        // if (!existingComment.getUser().getId().equals(currentUserId)) {
        // throw new AccessDeniedException("無權限刪除此留言");
        // }

        // 檢查是否已經被刪除
        if (existingComment.getDeleteStatus() != 1 || !existingComment.getIsActive()) {
            throw new IllegalArgumentException("留言已經被刪除");
        }

        // 軟刪除：設定狀態為已刪除
        existingComment.setDeleteStatus(0);
        existingComment.setIsActive(false);
        existingComment.setDeletedTime(LocalDateTime.now());
        existingComment.setUpdatedAt(LocalDateTime.now());

        // 儲存更新
        CommentsBean savedComment = commentsRepository.save(existingComment);

        System.out.println("留言已軟刪除 - ID: " + id);

        // 回傳被刪除的留言資訊
        return convertToDTO(savedComment);
    }

    // ==================== 按讚功能相關方法 ====================

    /**
     * 切換按讚狀態（按讚/取消按讚）
     * 
     * 功能說明：
     * - 根據當前按讚狀態進行切換
     * - 如果已按讚則取消按讚，如果未按讚則按讚
     * - 自動更新按讚數量統計
     * - 返回最新的按讚狀態和數量
     * 
     * 業務規則：
     * - 需要驗證評論和用戶存在
     * - 管理員不能按讚（在 Controller 層驗證）
     * - 不能對自己的評論按讚（可選功能）
     * - 按讚狀態變更會即時反映
     * 
     * @param commentId 評論 ID
     * @param userId 用戶 ID
     * @return 按讚狀態回應（包含是否按讚、按讚數量、訊息）
     * @throws EntityNotFoundException 當評論或用戶不存在時
     */
    public LikeStatusResponseDTO toggleLike(Integer commentId, Integer userId) {
        // 驗證留言是否存在（使用輕量查詢）
        CommentsBean comment = commentsRepository.findWithImagesById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("留言不存在"));

        // 驗證用戶是否存在
        AllUserBean user = allUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用戶不存在"));

        // 檢查是否已按讚
        boolean hasLiked = hasUserLiked(commentId, userId);

        if (hasLiked) {
            // 取消按讚
            return unlikeComment(commentId, userId);
        } else {
            // 按讚
            return likeComment(commentId, userId);
        }
    }

    /**
     * 按讚評論
     * 
     * 功能說明：
     * - 為指定評論建立按讚記錄
     * - 使用複合主鍵（commentId + userId）確保唯一性
     * - 自動更新按讚數量統計
     * 
     * 業務規則：
     * - 同一用戶對同一評論只能按讚一次
     * - 按讚記錄包含評論和用戶的關聯
     * - 按讚成功後返回更新後的按讚數量
     * 
     * @param commentId 評論 ID
     * @param userId 用戶 ID
     * @return 按讚狀態回應
     * @throws EntityNotFoundException 當評論或用戶不存在時
     * @throws IllegalArgumentException 當已經按讚過時
     */
    private LikeStatusResponseDTO likeComment(Integer commentId, Integer userId) {
        // 檢查是否已經按讚
        if (hasUserLiked(commentId, userId)) {
            throw new IllegalArgumentException("您已經按讚過此留言");
        }

        // 獲取留言和用戶（使用輕量查詢）
        CommentsBean comment = commentsRepository.findWithImagesById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("留言不存在"));
        AllUserBean user = allUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用戶不存在"));

        // 建立按讚記錄
        CommentFeedbacksBean.CommentFeedbacksId feedbackId = new CommentFeedbacksBean.CommentFeedbacksId();
        feedbackId.setCommentId(commentId);
        feedbackId.setUserId(userId);

        CommentFeedbacksBean feedback = new CommentFeedbacksBean();
        feedback.setId(feedbackId);
        feedback.setComment(comment);
        feedback.setUser(user);
        feedback.setFeedback(true);

        // 儲存按讚記錄
        commentFeedbacksRepository.save(feedback);

        // 獲取更新後的按讚數量
        int likeCount = getLikeCount(commentId);

        System.out.println("用戶 " + userId + " 對留言 " + commentId + " 按讚成功");

        return new LikeStatusResponseDTO(true, likeCount, "按讚成功");
    }

    /**
     * 取消按讚評論
     * 
     * 功能說明：
     * - 刪除指定評論的按讚記錄
     * - 使用複合主鍵定位要刪除的記錄
     * - 自動更新按讚數量統計
     * 
     * 業務規則：
     * - 只能取消已經按讚過的評論
     * - 取消按讚後按讚數量會減少
     * - 取消按讚成功後返回更新後的按讚數量
     * 
     * @param commentId 評論 ID
     * @param userId 用戶 ID
     * @return 按讚狀態回應
     * @throws EntityNotFoundException 當評論或用戶不存在時
     * @throws IllegalArgumentException 當尚未按讚時
     */
    private LikeStatusResponseDTO unlikeComment(Integer commentId, Integer userId) {
        // 檢查是否已經按讚
        if (!hasUserLiked(commentId, userId)) {
            throw new IllegalArgumentException("您尚未按讚此留言");
        }

        // 刪除按讚記錄
        CommentFeedbacksBean.CommentFeedbacksId feedbackId = new CommentFeedbacksBean.CommentFeedbacksId();
        feedbackId.setCommentId(commentId);
        feedbackId.setUserId(userId);

        commentFeedbacksRepository.deleteById(feedbackId);

        // 獲取更新後的按讚數量
        int likeCount = getLikeCount(commentId);

        System.out.println("用戶 " + userId + " 對留言 " + commentId + " 取消按讚成功");

        return new LikeStatusResponseDTO(false, likeCount, "取消按讚成功");
    }

    /**
     * 檢查用戶是否已對指定評論按讚
     * 
     * 功能說明：
     * - 查詢指定用戶對指定評論的按讚記錄
     * - 用於判斷當前按讚狀態
     * - 支援前端顯示按讚按鈕狀態
     * 
     * @param commentId 評論 ID
     * @param userId 用戶 ID
     * @return 是否已按讚
     */
    public boolean hasUserLiked(Integer commentId, Integer userId) {
        return commentFeedbacksRepository.findByCommentIdAndUserId(commentId, userId).isPresent();
    }

    /**
     * 獲取評論的按讚數量
     * 
     * 功能說明：
     * - 統計指定評論的總按讚數量
     * - 用於顯示評論的受歡迎程度
     * - 支援排序和篩選功能
     * 
     * @param commentId 評論 ID
     * @return 按讚數量
     */
    public int getLikeCount(Integer commentId) {
        return commentFeedbacksRepository.countLikesByCommentId(commentId);
    }

    /**
     * 獲取評論的按讚狀態和數量
     * 
     * 功能說明：
     * - 一次性獲取按讚狀態和數量
     * - 減少資料庫查詢次數
     * - 用於前端初始化按讚相關 UI
     * 
     * @param commentId 評論 ID
     * @param userId 用戶 ID
     * @return 按讚狀態回應
     */
    public LikeStatusResponseDTO getLikeStatus(Integer commentId, Integer userId) {
        boolean hasLiked = hasUserLiked(commentId, userId);
        int likeCount = getLikeCount(commentId);
        String message = hasLiked ? "已按讚" : "未按讚";
        
        return new LikeStatusResponseDTO(hasLiked, likeCount, message);
    }

    /**
     * 批量獲取留言按讚狀態
     * 
     * 功能說明：
     * - 一次性查詢多個留言的按讚狀態和數量
     * - 解決 N+1 查詢問題，大幅提升效能
     * - 返回結構化的按讚狀態資料
     * 
     * 效能優化：
     * - 使用單次查詢獲取所有按讚數量
     * - 使用單次查詢獲取用戶按讚狀態
     * - 減少資料庫查詢次數從 N 次到 2 次
     * 
     * @param commentIds 留言ID列表
     * @param userId 當前用戶ID
     * @return 按讚狀態映射，key為留言ID，value為按讚狀態物件
     */
    public Map<String, Object> getBatchLikeStatus(List<Integer> commentIds, Integer userId) {
        Map<String, Object> result = new HashMap<>();
        
        // 批量查詢按讚數量
        List<Object[]> likeCounts = commentFeedbacksRepository.countLikesByCommentIds(commentIds);
        Map<Integer, Long> likeCountMap = new HashMap<>();
        for (Object[] row : likeCounts) {
            Integer commentId = (Integer) row[0];
            Long count = (Long) row[1];
            likeCountMap.put(commentId, count);
        }
        
        // 批量查詢用戶按讚狀態
        List<Object[]> userLikes = commentFeedbacksRepository.findUserLikesByCommentIds(commentIds, userId);
        List<Integer> userLikedCommentIds = userLikes.stream()
                .map(row -> (Integer) row[0])
                .collect(Collectors.toList());
        
        // 組裝結果
        for (Integer commentId : commentIds) {
            Map<String, Object> likeStatus = new HashMap<>();
            likeStatus.put("likedByCurrentUser", userLikedCommentIds.contains(commentId));
            likeStatus.put("likeCount", likeCountMap.getOrDefault(commentId, 0L).intValue());
            
            result.put(commentId.toString(), likeStatus);
        }
        
        return result;
    }
}
