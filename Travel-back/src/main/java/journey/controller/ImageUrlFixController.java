package journey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.service.ImageUrlFixService;

/**
 * 圖片URL修復控制器
 * 提供修復資料庫中圖片URL格式的API端點
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2025-07-09
 */
@RestController
@RequestMapping("/api/admin/image-fix")
public class ImageUrlFixController {

    @Autowired
    private ImageUrlFixService imageUrlFixService;

    /**
     * 修復所有留言圖片的URL
     * 
     * @return 修復結果
     */
    @PostMapping("/comments/all")
    public ResponseEntity<String> fixAllCommentImageUrls() {
        try {
            imageUrlFixService.fixAllCommentImageUrls();
            return ResponseEntity.ok("✅ 所有留言圖片URL修復完成");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ 修復失敗: " + e.getMessage());
        }
    }

    /**
     * 修復指定留言的圖片URL
     * 
     * @param commentId 留言ID
     * @return 修復結果
     */
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<String> fixCommentImageUrls(@PathVariable Integer commentId) {
        try {
            imageUrlFixService.fixCommentImageUrls(commentId);
            return ResponseEntity.ok("✅ 留言 " + commentId + " 的圖片URL修復完成");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ 修復失敗: " + e.getMessage());
        }
    }
} 