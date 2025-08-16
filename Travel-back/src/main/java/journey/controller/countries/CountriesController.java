package journey.controller.countries;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.locations.CountryBean;
import journey.service.CountrySearchService;

@RestController
@RequestMapping("/countries")
public class CountriesController {

    @Autowired
    private CountrySearchService countrySearchService;

    @GetMapping("/all")
    public List<CountryBean> getAllCountries() {
        return countrySearchService.searchAllCountries(); // 會自動轉成 JSON 回傳
    }
}
