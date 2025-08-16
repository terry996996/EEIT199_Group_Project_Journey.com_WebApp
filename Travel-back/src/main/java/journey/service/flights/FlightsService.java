package journey.service.flights;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.domain.flights.AirlinesBean;
import journey.domain.flights.AirportsBean;
import journey.domain.flights.FlightClassesBean;
import journey.domain.flights.FlightsBean;
import journey.domain.flights.FlightsDetailsBean;
import journey.dto.flights.FlightCreateRequestDTO;
import journey.dto.flights.FlightDetailsRequestDTO;
import journey.repository.flights.AirlinesRepository;
import journey.repository.flights.AirportsRepository;
import journey.repository.flights.FlightClassesRepository;
import journey.repository.flights.FlightsDetailsRepository;
import journey.repository.flights.FlightsRepository;

@Service
public class FlightsService {
    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private AirlinesRepository airlinesRepository;
    @Autowired
    private AirportsRepository airportsRepository;
    @Autowired
    private FlightClassesRepository flightClassesRepository;
    @Autowired
    private FlightsDetailsRepository flightsDetailsRepository;

    // 创建新航班
    public void createFlight(FlightCreateRequestDTO dto) {
        FlightsBean flight = new FlightsBean();
        AirportsBean departureAirport = airportsRepository.findById(dto.getDepartureAirportCode())
                .orElseThrow(() -> new RuntimeException("找不到出發機場"));
        AirportsBean arrivalAirport = airportsRepository.findById(dto.getArrivalAirportCode())
                .orElseThrow(() -> new RuntimeException("找不到抵達機場"));
        AirlinesBean airline = airlinesRepository.findById(dto.getAirlineCode())
                .orElseThrow(() -> new RuntimeException("找不到航空公司"));
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setDepartureTerminal(dto.getDepartureTerminal());
        flight.setArrivalTerminal(dto.getArrivalTerminal());
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setAirline(airline);
        flight.setCreatedAt(LocalDateTime.now());
        // 計算分鐘數
        int duration = (int) java.time.Duration.between(dto.getDepartureTime(), dto.getArrivalTime())
                .toMinutes();
        flight.setDuration(duration);
        flightsRepository.save(flight);
    }

    // 依據航班id創建細節資訊
    public void createFlightDetails(FlightDetailsRequestDTO dto) {
        FlightsBean flight = flightsRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new RuntimeException("找不到航班 ID：" + dto.getFlightId()));

        FlightClassesBean flightClass = flightClassesRepository.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("找不到艙等 ID：" + dto.getClassId()));

        FlightsDetailsBean details = new FlightsDetailsBean();
        details.setFlight(flight);
        details.setFlightclass(flightClass);
        details.setClassPrice(dto.getClassPrice());
        details.setClassCapacity(dto.getClassCapacity());
        details.setClassAvailable(dto.getClassCapacity()); // 初始等於總座位
        details.setBaggageAllowance(dto.getBaggageAllowance());
        flightsDetailsRepository.save(details);
    }

    // 刪除航班
    public void deleteFlightById(Integer flightId) {
        if (!flightsRepository.existsById(flightId)) {
            throw new RuntimeException("找不到對應的航班 ID：" + flightId);
        }
        flightsRepository.deleteById(flightId);
    }

    public FlightsBean findFlightById(Integer id) {
        if (id != null) {
            Optional<FlightsBean> opt = flightsRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }
}
