package journey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import journey.projection.OptionView;
import journey.repository.attractions.AttractionTagsRepository;
import journey.repository.attractions.AttractionTypesRepository;
import journey.repository.comments.CommentReasonsRepository;
import journey.repository.lodgings.BedTypesRepository;
import journey.repository.lodgings.FacilitiesRepository;
import journey.repository.lodgings.LodgingsTypeRepository;
import journey.repository.locations.CityRepository;
import journey.repository.locations.CountryRepository;

/**
 * Spring Data Projection
 * 前端選單使用。
 * 
 * @author FAN
 * @since 250704
 */
@Service
@EnableCaching
public class OptionService {

    @Autowired
    private CommentReasonsRepository commentReasons;
    @Autowired
    private LodgingsTypeRepository lodgingsType;
    @Autowired
    private FacilitiesRepository facilities;
    @Autowired
    private BedTypesRepository bedTypes;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;
    // -----------------景點--------------------
    @Autowired
    private AttractionTagsRepository attractionTagsRepository;
    @Autowired 
    private AttractionTypesRepository attractionTypesRepository;
    // ---------------------------------------
    public OptionService(CommentReasonsRepository commentReasons,
            LodgingsTypeRepository lodgingsType,
            FacilitiesRepository facilities,
            BedTypesRepository bedTypes,
            CityRepository cityRepository,
            CountryRepository countryRepository) {
        this.commentReasons = commentReasons;
        this.lodgingsType = lodgingsType;
        this.facilities = facilities;
        this.bedTypes = bedTypes;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Cacheable("commentReasons")
    public List<OptionView> commentReasons() {
        return commentReasons.findOptions();
    }

    @Cacheable("lodgingTypes")
    public List<OptionView> lodgingsType() {
        return lodgingsType.findOptions();
    }

    @Cacheable("roomFacilities")
    public List<OptionView> facilities() {
        return facilities.findOptions();
    }

    @Cacheable("bedTypes")
    public List<OptionView> bedTypes() {
        return bedTypes.findOptions();
    }

    @Cacheable("cities")
    public List<OptionView> cities() {
        return cityRepository.findOptions();
    }

    @Cacheable("countries")
    public List<OptionView> countries() {
        return countryRepository.findOptions();
    }


    //景點標籤
    @Cacheable("attractionTags") // 可選，加快效能
    public List<OptionView> attractionTags() {
    return attractionTagsRepository.findOptions();
    }
    // 🏷 景點類型
    @Cacheable("attractionTypes")
    public List<OptionView> attractionTypes() {
        return attractionTypesRepository.findOptions();
    }

}
