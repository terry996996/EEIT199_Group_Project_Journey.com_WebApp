package journey.dto.Comments;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import journey.dto.BaseImageDTO;

/**
 * 更新留言的 DTO（支援同時更新內容和圖片）
 */
public class CommentUpdateDTO {

    private String content;

    @Min(1)
    @Max(5)
    private Byte rating;

    // 新增：支援圖片更新
    private List<BaseImageDTO> images;

    public CommentUpdateDTO() {
    }

    public CommentUpdateDTO(String content, Byte rating) {
        this.content = content;
        this.rating = rating;
    }

    public CommentUpdateDTO(String content, Byte rating, List<BaseImageDTO> images) {
        this.content = content;
        this.rating = rating;
        this.images = images;
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

    public List<BaseImageDTO> getImages() {
        return images;
    }

    public void setImages(List<BaseImageDTO> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "CommentUpdateDTO [content=" + content + ", rating=" + rating + ", images=" + images + "]";
    }
}