package journey.service.attractions;

import journey.domain.attractiontickets.AttractionContentsBean;
import journey.domain.attractiontickets.AttractionImagesBean;
import journey.domain.attractiontickets.AttractionsBean;
import journey.repository.attractions.AttractionContentsRepository;

import journey.repository.attractions.AttractionImagesRepository;
import journey.repository.attractions.AttractionsDAO;
import journey.repository.attractions.AttractionsRepository;
import journey.repository.attractions.AttractionTagsRepository;
import journey.repository.attractions.AttractionTypesRepository;
import journey.repository.locations.CityRepository;
import journey.repository.locations.CountryRepository;

import journey.domain.attractiontickets.AttractionTagsBean;
import journey.domain.attractiontickets.AttractionTypesBean;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;
import journey.dto.attractions.AttractionContentCreateDto;
import journey.dto.attractions.AttractionCreateDto;
import journey.dto.attractions.AttractionFullCreateDto;
import journey.dto.attractions.AttractionImageCreateDto;
import journey.dto.attractions.AttractionSearchDto;
import journey.dto.attractions.AttractionUpdateDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDateTime;

@Service
public class AttractionsService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AttractionsRepository attractionsRepository;

    @Autowired
    private AttractionsDAO attractionsDAO;

    @Autowired
    private AttractionImagesRepository imagesRepository;

    @Autowired
    private AttractionContentsRepository contentsRepository;

    @Autowired
    private AttractionTagsRepository tagRepository;

    @Autowired
    private AttractionTypesRepository typeRepository;

    public AttractionsBean createFull(AttractionFullCreateDto dto) {
        // 先查找國家與城市實體
        CountryBean country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("找不到指定國家"));
        CityBean city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new RuntimeException("找不到指定城市"));

        // 建立 AttractionsBean 主體
        AttractionsBean attraction = new AttractionsBean();
        attraction.setName(dto.getName());
        attraction.setDescription(dto.getDescription());
        attraction.setAddress(dto.getAddress());
        attraction.setImageUrl(dto.getImageUrl());
        attraction.setImageType(dto.getImageType());
        attraction.setCreatedAt(dto.getCreatedAt());
        attraction.setCreatedBy(dto.getCreatedBy());
        attraction.setCountry(country);
        attraction.setCity(city);

        // 多對多 - 標籤與類型
        if (dto.getTagIds() != null) {
            Set<AttractionTagsBean> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            attraction.setTags(tags);
        }

        if (dto.getTypeIds() != null) {
            Set<AttractionTypesBean> types = new HashSet<>(typeRepository.findAllById(dto.getTypeIds()));
            attraction.setTypes(types);
        }

        // 儲存主體
        AttractionsBean saved = attractionsRepository.save(attraction);

        // 儲存圖片
        if (dto.getImages() != null) {
            for (AttractionImageCreateDto imgDto : dto.getImages()) {
                AttractionImagesBean image = new AttractionImagesBean();
                image.setImageUrl(imgDto.getImageUrl());
                image.setImageType(imgDto.getImageType());
                image.setAltText(imgDto.getAltText());
                image.setSortOrder(imgDto.getSortOrder());
                image.setAttraction(saved);
                imagesRepository.save(image);
            }
        }

        // 儲存內文
        if (dto.getContents() != null) {
            for (AttractionContentCreateDto contentDto : dto.getContents()) {
                AttractionContentsBean content = new AttractionContentsBean();
                content.setTitle(contentDto.getTitle());
                content.setSubtitle(contentDto.getSubtitle());
                content.setDescription(contentDto.getDescription());
                content.setAttraction(saved);
                contentsRepository.save(content);
            }
        }

        return saved;
    }

    /**
     * 查詢景點：支援模糊查詢、分類、標籤、分頁、排序等條件
     */
    public List<AttractionsBean> search(AttractionSearchDto dto) {
        return attractionsDAO.find(dto);
    }

    /**
     * 新增一筆景點資料（含 DTO 與多對多）
     */
    public AttractionsBean create(AttractionCreateDto dto) {
        AttractionsBean attraction = new AttractionsBean();
        attraction.setName(dto.getName());
        attraction.setDescription(dto.getDescription());
        attraction.setAddress(dto.getAddress());
        attraction.setImageUrl(dto.getImageUrl());
        attraction.setImageType(dto.getImageType());
        attraction.setCreatedAt(Optional.ofNullable(dto.getCreatedAt()).orElse(LocalDateTime.now()));
        attraction.setCreatedBy(dto.getCreatedBy());

        // 設定關聯的國家與城市
        CountryBean country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("找不到指定的國家"));
        CityBean city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new RuntimeException("找不到指定的城市"));

        attraction.setCountry(country);
        attraction.setCity(city);

        // 多對多：標籤
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            Set<AttractionTagsBean> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            attraction.setTags(tags);
        }

        // 多對多：類型
        if (dto.getTypeIds() != null && !dto.getTypeIds().isEmpty()) {
            Set<AttractionTypesBean> types = new HashSet<>(typeRepository.findAllById(dto.getTypeIds()));
            attraction.setTypes(types);
        }

        return attractionsRepository.save(attraction);
    }

    /**
     * 修改指定 ID 的景點主體資料（不含子表）
     */
    public AttractionsBean update(Integer id, AttractionUpdateDto dto) {
        AttractionsBean attraction = attractionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定景點"));

        attraction.setName(dto.getName());
        attraction.setDescription(dto.getDescription());
        attraction.setAddress(dto.getAddress());
        attraction.setUpdatedAt(dto.getUpdatedAt());
        attraction.setUpdatedBy(dto.getUpdatedBy());

        // 多對一關聯
        CountryBean country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("找不到國家"));
        CityBean city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new RuntimeException("找不到城市"));
        attraction.setCountry(country);
        attraction.setCity(city);

        // 多對多關聯
        if (dto.getTagIds() != null) {
            Set<AttractionTagsBean> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            attraction.setTags(tags);
        }

        if (dto.getTypeIds() != null) {
            Set<AttractionTypesBean> types = new HashSet<>(typeRepository.findAllById(dto.getTypeIds()));
            attraction.setTypes(types);
        }

        return attractionsRepository.save(attraction);
    }

    /**
     * 刪除指定 ID 的景點資料（主表）
     */
    public boolean delete(Integer id) {
        if (attractionsRepository.existsById(id)) {
            attractionsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 搜尋全部景點
    public List<AttractionsBean> findAllWithDetails() {
        List<AttractionsBean> attractions = attractionsRepository.findAll();

        // 強制載入子關聯資料（避免 lazy loading 在 JSON 序列化時錯誤）
        for (AttractionsBean attraction : attractions) {
            // 初始化子關聯集合
            attraction.getImages().size();
            attraction.getContents().size();
            attraction.getTags().size();
            attraction.getTypes().size();

            if (attraction.getCountry() != null) {
                attraction.getCountry().getName(); // or any field
            }
            if (attraction.getCity() != null) {
                attraction.getCity().getName();
            }
        }

        return attractions;
    }

    public AttractionsBean findAttractionById(Integer id) {
        if (id != null) {
            Optional<AttractionsBean> opt = attractionsRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }
}
