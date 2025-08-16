package journey.dto.Comments;

/**
 * 檢舉更新 DTO
 * 用於管理員處理檢舉時傳送更新資料
 */
public record CommentReportUpdateDTO(
                String status, // RESOLVED 或 REJECTED
                String note // 管理員備註
) {
}