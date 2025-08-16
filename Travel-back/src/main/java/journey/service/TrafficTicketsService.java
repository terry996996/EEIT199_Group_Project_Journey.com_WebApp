package journey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.domain.traffictickets.TrafficTicketsBean;
import journey.repository.traffictickets.TrafficTicketsRepository;

@Service
public class TrafficTicketsService {

    @Autowired
    private TrafficTicketsRepository trafficTicketsRepository;

    public List<TrafficTicketsBean> findAllTickets() {
        return trafficTicketsRepository.findAll();
    }

    // 根据名字搜索违章记录
    public List<TrafficTicketsBean> searchByName(String name) {
        // 如果名字为空，则返回所有违章记录
        if (name == null || name.isEmpty()) {
            return trafficTicketsRepository.findAll();
            // 否则，根据名字模糊搜索违章记录
        } else {
            return trafficTicketsRepository.findByNameContainingIgnoreCase(name);
        }
    }

    public List<TrafficTicketsBean> searchTickets(
            String name,
            List<String> selectedTypes,
            List<String> selectedRegions,
            Integer minPrice,
            Integer maxPrice) {
        return trafficTicketsRepository.findByFilters(name, selectedTypes,
                selectedRegions, minPrice, maxPrice);
    }

    public List<TrafficTicketsBean> getAllWithCountry() {
        return trafficTicketsRepository.findAllWithCountry();
    }

}