package journey.dto.Comments;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import journey.enums.CommentTargetTypeEnum;

/**
 * 留言請求DTO
 * 
 * 功能說明：
 * - 用於建立新留言的請求資料傳輸物件
 * - 支援主留言和回覆留言
 * - 支援多種評論目標類型
 * - 支援圖片上傳
 * - 支援評分系統（僅主留言）
 * 
 * 驗證規則：
 * - content: 必填，不能為空，長度1-1000字
 * - rating: 1-5分，僅主留言需要
 * - targetType & targetId: 必填，指定評論目標
 * - userId: 必填，指定用戶
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2024
 */
public class CommentRequestDTO {

    /** 使用者ID */
    @NotNull(message = "用戶ID不能為空")
    private Integer userId;

    /** 留言內容 - 必填，不能為空，長度1-1000字 */
    @NotBlank(message = "留言內容不能為空")
    @Size(min = 1, max = 1000, message = "留言內容長度必須在1-1000字之間")
    private String content;

    /** 評分 - 1-5分，僅主留言填，回覆為null */
    @Min(value = 1, message = "評分不能低於1分")
    @Max(value = 5, message = "評分不能高於5分")
    private Byte rating;

    /** 回覆對象留言ID（主留言=null） */
    private Integer parentCommentId;

    /** 留言目標類型（LODGING, ROOM_TYPE, TRAFFIC_TICKET, ATTRACTION_TICKET, TRIP_PLAN） */
    @NotNull(message = "目標類型不能為空")
    private CommentTargetTypeEnum targetType;

    /** 留言目標ID */
    @NotNull(message = "目標ID不能為空")
    private Integer targetId;

    /** 留言圖片列表（前端傳陣列） */
    private List<CommentImageRequestDTO> images;

    public CommentRequestDTO() {

    }

    public CommentRequestDTO(Integer userId, @NotBlank String content, @Min(1) @Max(5) Byte rating,
            Integer parentCommentId, CommentTargetTypeEnum targetType, Integer targetId,
            List<CommentImageRequestDTO> images) {
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.parentCommentId = parentCommentId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.images = images;
    }

    @Override
    public String toString() {
        return "CommentRequestDTO [userId=" + userId + ", content=" + content + ", rating=" + rating
                + ", parentCommentId=" + parentCommentId + ", targetType=" + targetType + ", targetId=" + targetId
                + ", images=" + images + "]";
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public CommentTargetTypeEnum getTargetType() {
        return targetType;
    }

    public void setTargetType(CommentTargetTypeEnum targetType) {
        this.targetType = targetType;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public List<CommentImageRequestDTO> getImages() {
        return images;
    }

    public void setImages(List<CommentImageRequestDTO> images) {
        this.images = images;
    }
}
