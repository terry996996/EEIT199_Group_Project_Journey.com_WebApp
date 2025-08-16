package journey.service.locations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.locations.CountryBean;
import journey.repository.locations.CountryRepository;

/**
 * country related services
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/03/2025
 */
@Service
@Transactional
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    /**
     * find country by id
     * 
     * @return country entity if succeeds
     */
    public CountryBean findCountry(Integer id) {
        if (id != null) {
            Optional<CountryBean> opt = countryRepository.findById(id);
            if (opt.isPresent() && opt != null) {
                return opt.get();
            }
        }
        return null;
    }

    /**
     * find country by name
     * 
     * @return country entity if succeeds
     */
    public CountryBean findCountry(String name) {
        if (name != null) {
            Optional<CountryBean> opt = countryRepository.findByName(name);
            if (opt.isPresent() && opt != null) {
                return opt.get();
            }
        }
        return null;
    }

    public List<CountryBean> findAll() {
        return countryRepository.findAll();
    }
}
