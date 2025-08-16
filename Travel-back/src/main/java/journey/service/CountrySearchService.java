package journey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.domain.locations.CountryBean;
import journey.repository.countries.CountriesRepository;

@Service
public class CountrySearchService {
    @Autowired
    private CountriesRepository countriesRepository;

    // 查询所有国家信息
    public List<CountryBean> searchAllCountries() {
        // 从countriesRepository中查询所有国家信息
        return countriesRepository.findAll();
    }
}
