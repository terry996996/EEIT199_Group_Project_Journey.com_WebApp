package journey.service.flights;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.domain.flights.FlightsBean;
import journey.domain.flights.FlightsDetailsBean;
import journey.dto.MultiTripRequest;
import journey.dto.flights.FlightsDTO;
import journey.repository.flights.FlightInfoAdminView;
import journey.repository.flights.FlightsDetailsRepository;
import journey.repository.flights.FlightsRepository;

@Service
public class FlightsSearchService {

    // 自动注入FlightsRepository
    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private FlightsDetailsRepository flightsDetailsRepository;

    // 获取所有航班信息
    public List<FlightsDTO> getAllFlights() {
        List<FlightsBean> flights = flightsRepository.findAll();
        List<FlightsDTO> list = new ArrayList<>();

        for (FlightsBean flight : flights) {
            // 每個航班可能有多筆艙等資料
            Set<FlightsDetailsBean> detailsSet = flight.getFlightsDetails();

            if (detailsSet != null) {
                for (FlightsDetailsBean detail : detailsSet) {
                    FlightsDTO dto = new FlightsDTO();

                    // 航班基本資料
                    dto.setAirline(flight.getAirline().getAirlineName());
                    dto.setFlightNumber(flight.getFlightNumber());
                    dto.setDepartureAirport(flight.getDepartureAirport().getAirportCode());
                    dto.setArrivalAirport(flight.getArrivalAirport().getAirportCode());
                    dto.setDepartureTime(flight.getDepartureTime());
                    dto.setArrivalTime(flight.getArrivalTime());
                    dto.setDuration(flight.getDuration());
                    dto.setDepartureTerminal(flight.getDepartureTerminal());
                    dto.setArrivalTerminal(flight.getArrivalTerminal());
                    dto.setAirline(flight.getAirline().getAirlineName());
                    dto.setImageURL(flight.getAirline().getImageURL());
                    // 艙等細節
                    if (detail.getFlightclass() != null) {
                        dto.setFlightclass(detail.getFlightclass().getClassType());
                    } else {
                        dto.setFlightclass("未知艙等");
                    }
                    dto.setClassPrice(detail.getClassPrice());
                    dto.setClassCapacity(detail.getClassCapacity());
                    dto.setClassAvailable(detail.getClassAvailable());
                    dto.setBaggageAllowance(detail.getBaggageAllowance());
                    dto.setDepartureNameZh(flight.getDepartureAirport().getCity().getNameZh());
                    dto.setArrivalNameZh(flight.getArrivalAirport().getCity().getNameZh());

                    list.add(dto);
                }
            }
        }

        return list;
    }

    // public List<FlightsDTO> getAllFlights() {
    // List<FlightsBean> flights = flightsRepository.findAll();
    // List<FlightsDTO> list = new ArrayList<>();
    // flights.stream().forEach(f -> {
    // FlightsDTO dto = new FlightsDTO();
    // BeanUtils.copyProperties(f, dto, "airline", "departureAirport",
    // "arrivalAirport");
    // dto.setAirline(f.getAirline().getAirlineName());
    // dto.setDepartureAirport(f.getDepartureAirport().getAirportCode());
    // dto.setArrivalAirport(f.getArrivalAirport().getAirportCode());
    // list.add(dto);
    // });
    // return list;
    // }

    // 获取所有航班详细信息
    // public List<FlightsDetailsDTO> getAllFlightsDetails() {
    // List<FlightsDetailsBean> flightsDetails = flightsDetailsRepository.findAll();
    // List<FlightsDetailsDTO> list = new ArrayList<>();
    // flightsDetails.stream().forEach(f -> {
    // FlightsDetailsDTO dto = new FlightsDetailsDTO();
    // BeanUtils.copyProperties(f, dto, "flightclass");
    // dto.setFlightclass(f.getFlightclass().getClassType());
    // });
    // return list;
    // }

    // public List<FlightsDetailsDTO> getAllFlightsDetails() {
    // List<FlightsDetailsBean> flightsDetails = flightsDetailsRepository.findAll();
    // List<FlightsDetailsDTO> list = new ArrayList<>();
    // for (FlightsDetailsBean f : flightsDetails) {
    // FlightsDetailsDTO dto = new FlightsDetailsDTO();
    // BeanUtils.copyProperties(f, dto, "flightclass");

    // if (f.getFlightclass() != null) {
    // dto.setFlightclass(f.getFlightclass().getClassType());
    // } else {
    // dto.setFlightclass("未知艙等");
    // System.out.println("⚠️ 警告：flightsDetailsId " + f.getFlightsDetailsId() + " 的
    // flightclass 為 null");
    // }

    // list.add(dto);
    // }
    // return list;
    // }

    // 後端使用者查詢航班所有資訊
    public List<FlightInfoAdminView> getAdminFlightInfoList() {
        return flightsRepository.findAllFlightInfoForAdmin();
    }

    // 單程、來回查詢
    public List<FlightsDetailsBean> findByCriteria(String departure, String destination,
            LocalDate departDate, String cabinClass, LocalDate returnDate) {
        // 查去程
        List<FlightsDetailsBean> outbound = flightsDetailsRepository.findByCriteria(
                departure, destination, departDate, cabinClass);
        // 如果有 returnDate，查回程
        if (returnDate != null) {
            List<FlightsDetailsBean> inbound = flightsDetailsRepository.findByCriteria(
                    destination, departure, returnDate, cabinClass);
            outbound.addAll(inbound); // ✅ 合併去程與回程結果
        }
        return outbound;
    }

    // 多行程查询
    public List<FlightsDetailsBean> findMultiTripFlights(List<MultiTripRequest.TripLeg> trips, String cabinClass) {
        List<FlightsDetailsBean> results = new ArrayList<>();
        for (MultiTripRequest.TripLeg leg : trips) {
            LocalDate departDate = null;
            if (leg.getDate() != null && !leg.getDate().isEmpty()) {
                departDate = LocalDate.parse(leg.getDate());
            }
            List<FlightsDetailsBean> legResults = flightsDetailsRepository.findByCriteria(
                    leg.getDeparture(), leg.getDestination(), departDate, cabinClass);
            results.addAll(legResults);
        }
        return results;
    }

}
