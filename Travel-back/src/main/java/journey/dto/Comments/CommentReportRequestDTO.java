package journey.dto.Comments;

import java.util.List;

public record CommentReportRequestDTO(
        Integer commentId,
        List<Integer> reasonIds // 前端傳陣列，後端只取第一個
) {
}