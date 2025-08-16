package journey.service.locations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.locations.CityBean;
import journey.repository.locations.CityRepository;

@Service
@Transactional
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public CityBean findCity(Integer id) {
        if (id != null) {
            Optional<CityBean> opt = cityRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }
}
