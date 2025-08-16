package journey.service.plans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.plans.TripPlanBean;
import journey.domain.users.UserBean;
import journey.dto.plans.PlanDTO;
import journey.repository.trip.TripPlanRepository;
import journey.service.user.UserService;

@Service
@Transactional
public class TripPlanService {

    @Autowired
    private TripPlanRepository tripPlanRepository;

    public TripPlanBean newPlan(UserBean user, PlanDTO planDTO) {
        if (planDTO != null) {
            TripPlanBean bean = new TripPlanBean();
            if (bean != null) {
                dto2bean(planDTO, bean, "id", "createdBy", "lastModified", "deleteStatus");
                bean.setCreatedBy(user);
                return tripPlanRepository.save(bean);
            }
        }
        return null;
    }

    public TripPlanBean findTripById(Integer id) {
        if (id != null) {
            Optional<TripPlanBean> opt = tripPlanRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    public List<PlanDTO> findAllPlans(UserBean user) {
        if (user != null) {
            List<TripPlanBean> plans = tripPlanRepository.findByCreatedBy(user);
            if (plans != null && !plans.isEmpty()) {
                return plans.stream().map(this::bean2Dto).toList();
            }
        }

        return new ArrayList<>();
    }

    public TripPlanBean updatePlan(PlanDTO dto) {
        if (dto != null) {
            TripPlanBean bean = findTripById(dto.getId());
            if (bean != null) {
                dto2bean(dto, bean, "id", "createdBy", "lastModified");
                return tripPlanRepository.save(bean);
            }
        }
        return null;
    }

    private void dto2bean(PlanDTO dto, TripPlanBean bean, String... ignored) {
        if (dto != null && bean != null) {
            BeanUtils.copyProperties(dto, bean, ignored);
        }
    }

    private PlanDTO bean2Dto(TripPlanBean bean) {
        if (bean != null) {
            PlanDTO dto = new PlanDTO();
            BeanUtils.copyProperties(bean, dto, "createdBy");
            dto.setCreatedBy(bean.getCreatedBy().getId());
            return dto;
        }
        return null;
    }

}
