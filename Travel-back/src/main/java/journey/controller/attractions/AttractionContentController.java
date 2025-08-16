package journey.controller.attractions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.attractiontickets.AttractionContentsBean;
import journey.dto.attractions.AttractionContentCreateDto;
import journey.service.attractions.AttractionContentService;

@RestController
@RequestMapping("/api/attraction/contents")
// @CrossOrigin(origins = "http://localhost:5173") // 可根據前端調整
public class AttractionContentController {

    @Autowired
    private AttractionContentService contentService;

    /**
     * 查詢指定景點的所有內文
     */
    @GetMapping("/by-attraction/{attractionId}")
    public List<AttractionContentsBean> getContentsByAttraction(@PathVariable Integer attractionId) {
        return contentService.findByAttractionId(attractionId);
    }

    /**
     * 使用 DTO 新增內文至指定景點
     */
    @PostMapping("/add")
    public AttractionContentsBean addContent(@RequestBody AttractionContentCreateDto dto) {
        return contentService.addContent(dto);
    }

    /**
     * 修改內文（使用相同 DTO）
     */
    @PutMapping("/{contentId}")
    public AttractionContentsBean updateContent(
            @PathVariable Integer contentId,
            @RequestBody AttractionContentCreateDto dto) {
        return contentService.updateContent(contentId, dto);
    }

    /**
     * 刪除內文
     */
    @DeleteMapping("/{contentId}")
    public boolean deleteContent(@PathVariable Integer contentId) {
        return contentService.deleteContent(contentId);
    }
}