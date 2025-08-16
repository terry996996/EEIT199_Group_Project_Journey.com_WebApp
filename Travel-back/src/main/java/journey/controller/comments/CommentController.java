package journey.controller.comments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import journey.domain.users.AllUserBean;
import journey.dto.BaseImageDTO;
import journey.dto.Comments.CommentImageResponseDTO;
import journey.dto.Comments.CommentRequestDTO;
import journey.dto.Comments.CommentResponseDTO;
import journey.dto.Comments.CommentUpdateDTO;
import journey.dto.Comments.LikeRequestDTO;
import journey.dto.Comments.LikeStatusResponseDTO;
import journey.enums.UserTypeEnum;
import journey.repository.users.AllUserRepository;
import journey.service.comments.CommentService;

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
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AllUserRepository allUserRepository;

    /**
     * 新增留言（支援檔案上傳）- multipart/form-data 格式
     * 
     * 權限規則：
     * - USER: 新增主留言（評論）/ 回覆留言（解釋/處理）
     * - VENDOR: 回覆留言
     * - ADMIN: 回覆留言
     * 
     * @param commentJson 留言資料（JSON 字串）
     * @param files       上傳的圖片檔案（可選）
     * @return 新增的留言
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentResponseDTO> createCommentWithFiles(
            @RequestPart("comment") String commentJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {
            // 解析 JSON 字串為 CommentRequestDTO
            ObjectMapper objectMapper = new ObjectMapper();
            CommentRequestDTO finalDto = objectMapper.readValue(commentJson, CommentRequestDTO.class);

            System.out.println("📝 收到留言創建請求 (multipart/form-data):");
            System.out.println("   - 內容：" + finalDto.getContent());
            System.out.println("   - 用戶ID：" + finalDto.getUserId());
            System.out.println("   - 目標類型：" + finalDto.getTargetType());
            System.out.println("   - 目標ID：" + finalDto.getTargetId());
            System.out.println("   - 圖片數量：" + (files != null ? files.size() : 0));

            // 驗證權限
            validateUserPermissions(finalDto);

            CommentResponseDTO createdComment;

            // 如果有檔案，使用支援檔案上傳的方法
            if (files != null && !files.isEmpty()) {
                System.out.println("🖼️ 處理 " + files.size() + " 個圖片檔案");

                // 驗證檔案
                validateFiles(files);

                // 使用支援檔案上傳的方法
                createdComment = commentService.createCommentWithFiles(finalDto, files);
            } else {
                // 沒有檔案，使用原有方法
                createdComment = commentService.createComment(finalDto);
            }

            System.out.println("✅ 留言創建成功，ID: " + createdComment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);

        } catch (Exception e) {
            System.out.println("❌ 留言創建失敗: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 新增留言（純 JSON 格式）- application/json 格式
     * 
     * 權限規則：
     * - USER: 新增主留言（評論）/ 回覆留言（解釋/處理）
     * - VENDOR: 回覆留言
     * - ADMIN: 回覆留言
     * 
     * @param dto 留言資料
     * @return 新增的留言
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponseDTO> createComment(
            @RequestBody CommentRequestDTO dto) {

        try {
            System.out.println("📝 收到留言創建請求 (application/json):");
            System.out.println("   - 內容：" + dto.getContent());
            System.out.println("   - 用戶ID：" + dto.getUserId());
            System.out.println("   - 目標類型：" + dto.getTargetType());
            System.out.println("   - 目標ID：" + dto.getTargetId());

            // 驗證權限
            validateUserPermissions(dto);

            // 使用原有方法（無檔案）
            CommentResponseDTO createdComment = commentService.createComment(dto);

            System.out.println("✅ 留言創建成功，ID: " + createdComment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);

        } catch (Exception e) {
            System.out.println("❌ 留言創建失敗: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 驗證用戶權限
     */
    private void validateUserPermissions(CommentRequestDTO dto) {
        boolean isReply = dto.getParentCommentId() != null;

        AllUserBean currentUser = allUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("使用者不存在"));

        UserTypeEnum role = currentUser.getUserType().getType();

        // 主留言只能由 USER 發表
        if (!isReply && role != UserTypeEnum.USER) {
            throw new AccessDeniedException("只有一般使用者可以發表主留言");
        }

        // 回覆留言則需為 USER / ADMIN / VENDOR
        if (isReply && !(role == UserTypeEnum.USER || role == UserTypeEnum.ADMIN || role == UserTypeEnum.VENDOR)) {
            throw new AccessDeniedException("目前身份無法回覆留言");
        }
    }

    /**
     * 驗證檔案
     */
    private void validateFiles(List<MultipartFile> files) {
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("第 " + (i + 1) + " 個檔案為空");
            }
            // 評論圖片限制為 500KB
            if (file.getSize() > 500 * 1024) {
                throw new IllegalArgumentException("第 " + (i + 1) + " 個檔案大小超過限制 (500KB)");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("第 " + (i + 1) + " 個檔案類型不支援");
            }
        }
    }

    /**
     * 根據 ID 獲取留言
     * 
     * @param id 留言 ID
     * @return 留言資訊
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Integer id) {
        try {
            CommentResponseDTO comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 根據目標類型和 ID 獲取留言列表
     * 
     * 權限規則：
     * - USER: 新增主留言（評論）/ 回覆留言（解釋/處理）
     * - VENDOR: 回覆留言
     * - ADMIN: 回覆留言
     * 
     * @param targetType        目標類型 (LODGING, ROOM_TYPE, etc.)
     * @param targetId          目標 ID
     * @param includeDeleted    是否包含已刪除的留言（預設為 true）
     * @param includeLikeStatus 是否包含按讚狀態（預設為 false）
     * @param userId            當前用戶ID（當includeLikeStatus=true時需要）
     * @return 留言列表
     */
    @GetMapping("/target/{targetType}/{targetId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTarget(
            @PathVariable String targetType,
            @PathVariable Integer targetId,
            @RequestParam(defaultValue = "true") boolean includeDeleted,
            @RequestParam(defaultValue = "false") boolean includeLikeStatus,
            @RequestParam(required = false) Integer userId) {
        try {
            System.out.println("📝 查詢留言列表:");
            System.out.println("   - 目標類型: " + targetType);
            System.out.println("   - 目標ID: " + targetId);
            System.out.println("   - 包含已刪除: " + includeDeleted);
            System.out.println("   - 包含按讚狀態: " + includeLikeStatus);
            System.out.println("   - 用戶ID: " + userId);

            List<CommentResponseDTO> comments = commentService.getCommentsByTarget(targetType, targetId,
                    includeDeleted, includeLikeStatus, userId);

            System.out.println("✅ 查詢成功，共 " + comments.size() + " 筆留言");
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            System.err.println("❌ 查詢留言列表失敗: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 更新留言
     * 
     * 權限規則：
     * - 只能編輯自己的留言
     * - 只能編輯正常留言（未被刪除或檢舉）
     * 
     * @param id  留言 ID
     * @param dto 更新資料
     * @return 更新後的留言
     */
    /**
     * 更新留言（JSON格式）
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Integer id,
            @RequestBody CommentUpdateDTO dto) {
        try {
            System.out.println("📝 收到留言更新請求 (JSON) - ID: " + id);
            System.out.println("   - 內容: " + (dto.getContent() != null
                    ? dto.getContent().substring(0, Math.min(50, dto.getContent().length())) + "..."
                    : "null"));
            System.out.println("   - 評分: " + dto.getRating());
            System.out.println("   - 圖片數量: " + (dto.getImages() != null ? dto.getImages().size() : 0));

            CommentResponseDTO updatedComment = commentService.updateComment(id, dto);

            System.out.println("✅ 留言更新成功 - ID: " + id);
            return ResponseEntity.ok(updatedComment);

        } catch (EntityNotFoundException e) {
            System.err.println("❌ 留言更新失敗 - 留言不存在: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            System.err.println("❌ 留言更新失敗 - 參數錯誤: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("❌ 留言更新失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 更新留言（支援檔案上傳）- multipart/form-data 格式
     * 
     * @param id          留言 ID
     * @param commentJson 留言資料（JSON 字串）
     * @param files       上傳的圖片檔案（可選）
     * @return 更新後的留言
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentResponseDTO> updateCommentWithFiles(
            @PathVariable Integer id,
            @RequestPart("comment") String commentJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            System.out.println("📝 收到留言更新請求 (FormData) - ID: " + id);
            System.out.println("   - 檔案數量: " + (files != null ? files.size() : 0));

            // 解析 JSON 字串為 CommentUpdateDTO
            ObjectMapper objectMapper = new ObjectMapper();
            CommentUpdateDTO dto = objectMapper.readValue(commentJson, CommentUpdateDTO.class);

            System.out.println("   - 內容: " + (dto.getContent() != null
                    ? dto.getContent().substring(0, Math.min(50, dto.getContent().length())) + "..."
                    : "null"));
            System.out.println("   - 評分: " + dto.getRating());

            // 使用支援檔案上傳的更新方法
            CommentResponseDTO updatedComment = commentService.updateCommentWithFiles(id, dto, files);

            System.out.println("✅ 留言更新成功 (FormData) - ID: " + id);
            return ResponseEntity.ok(updatedComment);

        } catch (EntityNotFoundException e) {
            System.err.println("❌ 留言更新失敗 - 留言不存在: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            System.err.println("❌ 留言更新失敗 - 參數錯誤: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("❌ 留言更新失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 刪除留言（軟刪除）
     * 
     * 權限規則：
     * - 只能刪除自己的留言
     * - 只能刪除正常留言（未被刪除或檢舉）
     * 
     * @param id 留言 ID
     * @return 被刪除的留言資訊
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> deleteComment(@PathVariable Integer id) {
        try {
            CommentResponseDTO deletedComment = commentService.deleteComment(id);
            return ResponseEntity.ok(deletedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 獲取指定留言的有效圖片
     * 
     * @param commentId 留言 ID
     * @return 圖片列表
     */
    @GetMapping("/{commentId}/images")
    public ResponseEntity<List<CommentImageResponseDTO>> getActiveImagesByCommentId(@PathVariable Integer commentId) {
        try {
            List<CommentImageResponseDTO> images = commentService.getActiveImagesByCommentId(commentId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 獲取指定留言的所有圖片（包括無效的）- 供管理員使用
     * 
     * @param commentId 留言 ID
     * @return 所有圖片列表
     */
    @GetMapping("/{commentId}/images/all")
    public ResponseEntity<List<CommentImageResponseDTO>> getAllImagesByCommentId(@PathVariable Integer commentId) {
        try {
            List<CommentImageResponseDTO> images = commentService.getAllImagesByCommentId(commentId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 更新留言圖片（排序、刪除、新增）
     * 
     * @param commentId 留言 ID
     * @param request   包含圖片列表的請求
     * @return 更新後的圖片列表
     */
    @PutMapping("/{commentId}/images")
    public ResponseEntity<List<CommentImageResponseDTO>> updateCommentImages(
            @PathVariable Integer commentId,
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> imagesData = (List<Map<String, Object>>) request.get("images");

            if (imagesData == null) {
                return ResponseEntity.badRequest().build();
            }

            // 轉換為 BaseImageDTO
            List<BaseImageDTO> imageDtos = imagesData.stream()
                    .map(imgData -> {
                        BaseImageDTO dto = new BaseImageDTO();

                        // 處理 imageId（可能是字串或數字）
                        Object imageIdObj = imgData.get("imageId");
                        if (imageIdObj != null) {
                            if (imageIdObj instanceof String) {
                                String imageIdStr = (String) imageIdObj;
                                // 如果是 "existing-X" 格式，提取數字部分
                                if (imageIdStr.startsWith("existing-")) {
                                    try {
                                        String numPart = imageIdStr.substring("existing-".length());
                                        dto.setImageId(Integer.parseInt(numPart));
                                    } catch (NumberFormatException e) {
                                        // 如果無法解析，設置為 null（表示新增）
                                        dto.setImageId(null);
                                    }
                                } else {
                                    // 嘗試直接轉換為數字
                                    try {
                                        dto.setImageId(Integer.parseInt(imageIdStr));
                                    } catch (NumberFormatException e) {
                                        dto.setImageId(null);
                                    }
                                }
                            } else if (imageIdObj instanceof Number) {
                                dto.setImageId(((Number) imageIdObj).intValue());
                            }
                        }

                        // 設置圖片類型
                        dto.setImageType("comment");
                        dto.setTargetId(commentId);
                        dto.setTargetType("comment");

                        // 設置其他欄位（如果存在）
                        if (imgData.get("imageUrl") != null) {
                            dto.setImageUrl((String) imgData.get("imageUrl"));
                        }
                        if (imgData.get("mimeType") != null) {
                            dto.setMimeType((String) imgData.get("mimeType"));
                        }
                        if (imgData.get("sortOrder") != null) {
                            Object sortOrderObj = imgData.get("sortOrder");
                            if (sortOrderObj instanceof Number) {
                                dto.setSortOrder(((Number) sortOrderObj).intValue());
                            }
                        }
                        if (imgData.get("isActive") != null) {
                            dto.setIsActive((Boolean) imgData.get("isActive"));
                        }

                        return dto;
                    })
                    .collect(Collectors.toList());

            // 使用 UnifiedImageService 更新圖片
            List<BaseImageDTO> updatedImages = commentService.updateCommentImages(commentId, imageDtos);

            // 轉換為回應 DTO
            List<CommentImageResponseDTO> responseImages = updatedImages.stream()
                    .map(img -> {
                        CommentImageResponseDTO dto = new CommentImageResponseDTO();
                        dto.setImageId(img.getImageId());
                        dto.setCommentImageUrl(img.getImageUrl());
                        dto.setSortOrder(img.getSortOrder());
                        dto.setIsActive(img.getIsActive());
                        dto.setIsDeleted(img.getIsActive() != null && !img.getIsActive());
                        dto.setMimeType(img.getMimeType());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseImages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 獲取指定留言的所有回覆
     * 
     * @param parentCommentId 父留言 ID
     * @param includeDeleted  是否包含已刪除的回覆（預設為 true）
     * @return 回覆列表
     */
    @GetMapping("/{parentCommentId}/replies")
    public ResponseEntity<List<CommentResponseDTO>> getRepliesByParentCommentId(
            @PathVariable Integer parentCommentId,
            @RequestParam(defaultValue = "true") boolean includeDeleted) {
        try {
            List<CommentResponseDTO> replies = commentService.getRepliesByParentCommentId(parentCommentId,
                    includeDeleted);
            return ResponseEntity.ok(replies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== 按讚功能 API 端點 ====================

    /**
     * 切換按讚狀態（按讚/取消按讚）
     * 
     * @param commentId 留言 ID
     * @param request   按讚請求
     * @return 按讚狀態回應
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<LikeStatusResponseDTO> toggleLike(@PathVariable Integer commentId,
            @RequestBody LikeRequestDTO request) {
        try {
            System.out.println("收到按讚請求 - 留言ID: " + commentId + ", 用戶ID: " + request.getUserId());

            // 驗證用戶 ID
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(new LikeStatusResponseDTO(false, 0, "用戶 ID 不能為空"));
            }

            // 檢查用戶權限（管理員不能按讚）
            AllUserBean user = allUserRepository.findById(request.getUserId())
                    .orElse(null);
            if (user != null && "ADMIN".equals(user.getUserType().getType().toString())) {
                return ResponseEntity.badRequest()
                        .body(new LikeStatusResponseDTO(false, 0, "管理員不能按讚留言"));
            }

            LikeStatusResponseDTO response = commentService.toggleLike(commentId, request.getUserId());
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            System.err.println("按讚失敗 - 實體不存在: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            System.err.println("按讚失敗 - 參數錯誤: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new LikeStatusResponseDTO(false, 0, e.getMessage()));
        } catch (Exception e) {
            System.err.println("按讚失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LikeStatusResponseDTO(false, 0, "系統錯誤，請稍後再試"));
        }
    }

    /**
     * 獲取留言的按讚狀態
     * 
     * @param commentId 留言 ID
     * @param userId    用戶 ID（可選）
     * @return 按讚狀態回應
     */
    @GetMapping("/{commentId}/like-status")
    public ResponseEntity<LikeStatusResponseDTO> getLikeStatus(@PathVariable Integer commentId,
            @RequestParam(required = false) Integer userId) {
        try {
            System.out.println("查詢按讚狀態 - 留言ID: " + commentId + ", 用戶ID: " + userId);

            LikeStatusResponseDTO response = commentService.getLikeStatus(commentId, userId);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            System.err.println("查詢按讚狀態失敗 - 留言不存在: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("查詢按讚狀態失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LikeStatusResponseDTO(false, 0, "系統錯誤，請稍後再試"));
        }
    }

    /**
     * 檢查用戶是否已按讚指定留言
     * 
     * @param commentId 留言 ID
     * @param userId    用戶 ID
     * @return 是否已按讚
     */
    @GetMapping("/{commentId}/liked-by/{userId}")
    public ResponseEntity<Map<String, Object>> checkUserLiked(@PathVariable Integer commentId,
            @PathVariable Integer userId) {
        try {
            System.out.println("檢查用戶按讚狀態 - 留言ID: " + commentId + ", 用戶ID: " + userId);

            boolean hasLiked = commentService.hasUserLiked(commentId, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("liked", hasLiked);
            response.put("message", hasLiked ? "用戶已按讚" : "用戶未按讚");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("檢查用戶按讚狀態失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("liked", false);
            errorResponse.put("message", "系統錯誤，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 獲取留言的按讚數量
     * 
     * @param commentId 留言 ID
     * @return 按讚數量
     */
    @GetMapping("/{commentId}/like-count")
    public ResponseEntity<Map<String, Object>> getLikeCount(@PathVariable Integer commentId) {
        try {
            System.out.println("查詢按讚數量 - 留言ID: " + commentId);

            int likeCount = commentService.getLikeCount(commentId);
            Map<String, Object> response = new HashMap<>();
            response.put("likeCount", likeCount);
            response.put("message", "查詢成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("查詢按讚數量失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("likeCount", 0);
            errorResponse.put("message", "系統錯誤，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ==================== 批量按讚狀態 API ====================

    /**
     * 批量獲取留言按讚狀態
     * 
     * @param request 包含留言ID列表和用戶ID的請求
     * @return 批量按讚狀態回應
     */
    @PostMapping("/batch-like-status")
    public ResponseEntity<Map<String, Object>> getBatchLikeStatus(
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> commentIds = (List<Integer>) request.get("commentIds");
            Integer userId = (Integer) request.get("userId");

            System.out.println("批量查詢按讚狀態 - 留言IDs: " + commentIds + ", 用戶ID: " + userId);

            if (commentIds == null || commentIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "留言ID列表不能為空"));
            }

            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "用戶ID不能為空"));
            }

            Map<String, Object> result = commentService.getBatchLikeStatus(commentIds, userId);
            return ResponseEntity.ok(Map.of("success", true, "data", result));

        } catch (Exception e) {
            System.err.println("批量查詢按讚狀態失敗 - 系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "系統錯誤，請稍後再試"));
        }
    }

    // ========== 錯誤處理 ==========

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse("FORBIDDEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(ConstraintViolationException ex) {
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", "資料驗證失敗");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedOperation(UnsupportedOperationException ex) {
        ErrorResponse error = new ErrorResponse("NOT_IMPLEMENTED", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(error);
    }

    // 錯誤回應 DTO
    public static class ErrorResponse {
        private String code;
        private String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}