package journey.service.attractions;

import journey.domain.attractiontickets.AttractionImagesBean;
import journey.domain.attractiontickets.AttractionsBean;
import journey.dto.attractions.AttractionImageCreateDto;
import journey.repository.attractions.AttractionImagesRepository;
import journey.repository.attractions.AttractionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttractionImageService {

    @Autowired
    private AttractionImagesRepository imagesRepository;

    @Autowired
    private AttractionsRepository attractionsRepository;

    /**
     * 根據景點 ID 查詢所有圖片
     */
    public List<AttractionImagesBean> findByAttractionId(Integer attractionId) {
        return imagesRepository.findByAttraction_Id(attractionId);
    }

    /**
     * 為指定景點新增圖片
     */
    public AttractionImagesBean addImage(AttractionImageCreateDto dto) {
        AttractionsBean attraction = attractionsRepository.findById(dto.getAttractionId())
                .orElseThrow(() -> new RuntimeException("找不到對應景點"));

        AttractionImagesBean image = new AttractionImagesBean();
        image.setImageUrl(dto.getImageUrl());
        image.setImageType(dto.getImageType());
        image.setAltText(dto.getAltText());
        image.setSortOrder(dto.getSortOrder());
        image.setAttraction(attraction);

        return imagesRepository.save(image);
    }

    /**
     * 修改單筆圖片
     */
    public AttractionImagesBean updateImage(Integer imageId, AttractionImageCreateDto dto) {
        AttractionImagesBean original = imagesRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("找不到圖片 ID：" + imageId));

        if (dto.getAttractionId() != null) {
            AttractionsBean attraction = attractionsRepository.findById(dto.getAttractionId())
                    .orElseThrow(() -> new RuntimeException("找不到景點 ID：" + dto.getAttractionId()));
            original.setAttraction(attraction);
        }

        original.setImageUrl(dto.getImageUrl());
        original.setImageType(dto.getImageType());
        original.setAltText(dto.getAltText());
        original.setSortOrder(dto.getSortOrder());

        return imagesRepository.save(original);
    }

    /**
     * 刪除圖片
     */
    public boolean deleteImage(Integer imageId) {
        if (imagesRepository.existsById(imageId)) {
            imagesRepository.deleteById(imageId);
            return true;
        }
        return false;
    }
}
