package journey.service.plans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.plans.TripDayBean;
import journey.domain.plans.TripPlanBean;
import journey.dto.plans.DayDTO;
import journey.repository.trip.TripDayRepository;

@Service
@Transactional
public class TripDayService {

    @Autowired
    private TripDayRepository tripDayRepository;

    @Autowired
    private TripPlanService tripPlanService;

    public TripDayBean newDay(DayDTO dto) {
        if (dto != null) {
            TripDayBean bean = new TripDayBean();
            if (bean != null) {
                dto2bean(dto, bean, "id", "tripId");
                return tripDayRepository.save(bean);
            }
        }
        return null;
    }

    public TripDayBean findByDayId(Integer id) {
        if (id != null) {
            Optional<TripDayBean> opt = tripDayRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    public List<DayDTO> findAllDays(TripPlanBean plan) {
        if (plan != null) {
            List<TripDayBean> days = tripDayRepository.findByTripId(plan);
            if (days != null && !days.isEmpty()) {
                return days.stream().map(this::bean2Dto).toList();
            }
        }
        return new ArrayList<>();
    }

    private void dto2bean(DayDTO dto, TripDayBean bean, String... ignored) {
        if (dto != null && bean != null) {
            BeanUtils.copyProperties(dto, bean, ignored);
            bean.setTripId(tripPlanService.findTripById(dto.getTripId()));
        }
    }

    private DayDTO bean2Dto(TripDayBean bean) {
        if (bean != null) {
            DayDTO dto = new DayDTO();
            BeanUtils.copyProperties(bean, dto, "tripId");
            dto.setTripId(bean.getTripId().getId());
            return dto;
        }
        return null;
    }

}
