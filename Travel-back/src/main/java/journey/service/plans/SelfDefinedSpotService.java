package journey.service.plans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.plans.SelfDefinedSpotBean;
import journey.domain.users.UserBean;
import journey.dto.plans.SelfDTO;
import journey.repository.trip.SelfDefinedSpotRepository;
import journey.service.locations.CityService;
import journey.service.locations.CountryService;
import journey.service.user.UserService;

@Service
@Transactional
public class SelfDefinedSpotService {

    @Autowired
    private SelfDefinedSpotRepository selfDefinedSpotRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    public SelfDefinedSpotBean newSpot(SelfDTO planDTO) {
        if (planDTO != null) {
            SelfDefinedSpotBean bean = new SelfDefinedSpotBean();
            if (bean != null) {
                dto2bean(planDTO, bean, "id", "createdBy", "country", "city");
                return selfDefinedSpotRepository.save(bean);
            }
        }
        return null;
    }

    public SelfDefinedSpotBean findSpotById(Integer id) {
        if (id != null) {
            Optional<SelfDefinedSpotBean> opt = selfDefinedSpotRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    public List<SelfDTO> findAllSpots(UserBean user) {
        if (user != null) {
            List<SelfDefinedSpotBean> plans = selfDefinedSpotRepository.findByCreatedBy(user);
            if (plans != null && !plans.isEmpty()) {
                return plans.stream().map(this::bean2Dto).toList();
            }
        }
        return new ArrayList<>();
    }

    public SelfDefinedSpotBean updatePlan(SelfDTO dto) {
        if (dto != null) {
            SelfDefinedSpotBean bean = findSpotById(dto.getId());
            if (bean != null) {
                dto2bean(dto, bean, "id", "createdBy", "country", "city");
                return selfDefinedSpotRepository.save(bean);
            }
        }
        return null;
    }

    private void dto2bean(SelfDTO dto, SelfDefinedSpotBean bean, String... ignored) {
        if (dto != null && bean != null) {
            BeanUtils.copyProperties(dto, bean, ignored);
            bean.setCreatedBy(userService.findUser(dto.getCreatedBy()));
            bean.setCoutry(countryService.findCountry(dto.getCoutry()));
            bean.setCity(cityService.findCity(dto.getCity()));
        }
    }

    private SelfDTO bean2Dto(SelfDefinedSpotBean bean) {
        if (bean != null) {
            SelfDTO dto = new SelfDTO();
            BeanUtils.copyProperties(bean, dto, "createdBy", "coutry", "city");
            dto.setCreatedBy(bean.getCreatedBy().getId());
            dto.setCoutry(bean.getCoutry().getId());
            dto.setCity(bean.getCity().getId());
            return dto;
        }
        return null;
    }

}
