package journey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.projection.OptionView;
import journey.repository.lodgings.BedTypesRepository;
import journey.repository.lodgings.LodgingsTypeRepository;
import journey.repository.lodgings.RoomFacilitiesRepository;
import journey.service.OptionService;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/comment-reasons")
    public List<OptionView> reasons() {
        return optionService.commentReasons();
    }

    @GetMapping("/lodgings-types")
    public List<OptionView> lodgingsType() {
        return optionService.lodgingsType();
    }

    @GetMapping("/facilities")
    public List<OptionView> facilities() {
        return optionService.facilities();
    }

    @GetMapping("/bed-types")
    public List<OptionView> bedTypes() {
        return optionService.bedTypes();
    }

    @GetMapping("/cities")
    public List<OptionView> cities() {
        return optionService.cities();
    }

    @GetMapping("/countries")
    public List<OptionView> countries() {
        return optionService.countries();
    }
    // -------------------------景點標籤與類別-------------------------
    @GetMapping("/attraction-tags")
    public List<OptionView> attractionTags() {
    return optionService.attractionTags();
}
    //景點類型
    @GetMapping("/attraction-types")
    public List<OptionView> attractionTypes() {
    return optionService.attractionTypes();
    }
    // -------------------------------------------------------------
}
