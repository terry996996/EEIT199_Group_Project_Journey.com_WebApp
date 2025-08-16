package journey.service.plans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.plans.TripActivityBean;
import journey.domain.plans.TripDayBean;
import journey.dto.plans.ActivityDTO;
import journey.enums.ActivityTypeEnum;
import journey.repository.trip.TripActivitiyRepository;
import journey.service.attractions.AttractionTicketsService;
import journey.service.attractions.AttractionsService;
import journey.service.flights.FlightsService;
import journey.service.lodgings.LodgingsService;

@Service
@Transactional
public class TripActivityService {

    @Autowired
    private TripActivitiyRepository tripActivitiyRepository;

    @Autowired
    private FlightsService flightsService;

    @Autowired
    private LodgingsService lodgingsService;

    @Autowired
    private AttractionTicketsService attractionTicketsService;

    @Autowired
    private AttractionsService attractionsService;

    @Autowired
    private SelfDefinedSpotService selfDefinedSpotService;

    public TripActivityBean newActivity(ActivityDTO dto) {
        if (dto != null) {
            TripActivityBean bean = new TripActivityBean();
            if (bean != null) {
                dto2bean(dto, bean);
                return tripActivitiyRepository.save(bean);
            }
        }
        return null;
    }

    public TripActivityBean findActivityById(Integer id) {
        if (id != null) {
            Optional<TripActivityBean> opt = tripActivitiyRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    public List<ActivityDTO> findAllPlans(TripDayBean day) {
        if (day != null) {
            List<TripActivityBean> activities = tripActivitiyRepository.findByDayId(day);
            if (activities != null && !activities.isEmpty()) {
                return activities.stream().map(this::bean2Dto).toList();
            }
        }

        return new ArrayList<>();
    }

    public TripActivityBean updatePlan(ActivityDTO dto) {
        if (dto != null) {
            TripActivityBean bean = findActivityById(dto.getId());
            if (bean != null) {
                dto2bean(dto, bean);
                return tripActivitiyRepository.save(bean);
            }
        }
        return null;
    }

    private void dto2bean(ActivityDTO dto, TripActivityBean bean) {
        if (dto != null && bean != null) {
            String[] ignored = {
                    "id", "locationType", "flight", "roomType", "traffic", "attractionTicket", "attraction",
                    "selfDefined", "itemId"
            };
            BeanUtils.copyProperties(dto, bean, ignored);
            String type = dto.getLocationType();
            bean.setLocationType(ActivityTypeEnum.valueOf(type));
            if (type == "flight") {
                bean.setFlight(flightsService.findFlightById(dto.getItemId()));
            } else if (type == "roomType") {
                bean.setRoomType(lodgingsService.findRoomTypeById(dto.getItemId()));
            } else if (type == "traffic") {
                System.out.println("nothing here");
            } else if (type == "attractionTicket") {
                bean.setAttractionTicket(attractionTicketsService.findById(dto.getItemId()));
            } else if (type == "attraction") {
                bean.setAttraction(attractionsService.findAttractionById(dto.getItemId()));
            } else if (type == "selfDefined") {
                bean.setSelfDefined(selfDefinedSpotService.findSpotById(dto.getItemId()));
            }

        }
    }

    private ActivityDTO bean2Dto(TripActivityBean bean) {
        if (bean != null) {
            ActivityDTO dto = new ActivityDTO();
            String[] ignored = {
                    "id", "locationType", "flight", "roomType", "traffic", "attractionTicket", "attraction",
                    "selfDefined", "dayId"
            };
            BeanUtils.copyProperties(bean, dto, ignored);
            String type = bean.getLocationType().name();
            dto.setLocationType(type);
            Integer itemId = null;
            if (type == "flight") {
                itemId = bean.getFlight().getFlightId();
            } else if (type == "roomType") {
                itemId = bean.getRoomType().getId();
            } else if (type == "traffic") {
                itemId = null;
            } else if (type == "attractionTicket") {
                itemId = bean.getAttractionTicket().getId();
            } else if (type == "attraction") {
                itemId = bean.getAttraction().getId();
            } else if (type == "selfDefined") {
                itemId = bean.getSelfDefined().getId();
            }
            dto.setItemId(itemId);
            return dto;
        }
        return null;
    }

}
