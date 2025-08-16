package journey.controller.locations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.locations.CountryBean;
import journey.dto.locations.CountryDTO;
import journey.service.locations.CountryService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    CountryService countryService;

    @GetMapping("/getCountries")
    public ResponseEntity<?> getCountries() {
        List<CountryBean> beans = countryService.findAll();
        List<CountryDTO> list = new ArrayList<>();
        for (CountryBean bean : beans) {
            CountryDTO dto = new CountryDTO();
            BeanUtils.copyProperties(bean, dto);
            list.add(dto);
        }
        // sorted by name
        return ResponseEntity
                .ok()
                .body(list.stream()
                        .sorted(Comparator.comparing(CountryDTO::getName))
                        .toList());
    }

}
