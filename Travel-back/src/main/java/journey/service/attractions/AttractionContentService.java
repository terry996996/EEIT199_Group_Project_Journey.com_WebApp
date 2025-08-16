package journey.service.attractions;

import journey.domain.attractiontickets.AttractionContentsBean;
import journey.domain.attractiontickets.AttractionsBean;
import journey.dto.attractions.AttractionContentCreateDto;
import journey.repository.attractions.AttractionContentsRepository;
import journey.repository.attractions.AttractionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AttractionContentService {

    @Autowired
    private AttractionContentsRepository contentsRepository;

    @Autowired
    private AttractionsRepository attractionsRepository;

    /**
     * 根據景點 ID 取得所有內文
     */
    public List<AttractionContentsBean> findByAttractionId(Integer attractionId) {
        return contentsRepository.findByAttraction_Id(attractionId);
    }

   /**
     * 為指定景點新增內文
     */
    public AttractionContentsBean addContent(AttractionContentCreateDto dto) {
        AttractionsBean attraction = attractionsRepository.findById(dto.getAttractionId())
                .orElseThrow(() -> new RuntimeException("找不到指定景點"));

        AttractionContentsBean content = new AttractionContentsBean();
        content.setTitle(dto.getTitle());
        content.setSubtitle(dto.getSubtitle());
        content.setDescription(dto.getDescription());
        content.setAttraction(attraction);

        return contentsRepository.save(content);
    }

    /**
     * 修改內文
     */
    public AttractionContentsBean updateContent(Integer contentId, AttractionContentCreateDto dto) {
        AttractionContentsBean original = contentsRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("找不到內文"));

        original.setTitle(dto.getTitle());
        original.setSubtitle(dto.getSubtitle());
        original.setDescription(dto.getDescription());

        return contentsRepository.save(original);
    }

    /**
     * 刪除指定內文
     */
    public boolean deleteContent(Integer contentId) {
        if (contentsRepository.existsById(contentId)) {
            contentsRepository.deleteById(contentId);
            return true;
        }
        return false;
    }
} 

