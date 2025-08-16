package journey.controller.flights;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import journey.domain.flights.FlightsDetailsBean;
import journey.dto.MultiTripRequest;
import journey.dto.flights.FlightCreateRequestDTO;
import journey.dto.flights.FlightDetailsRequestDTO;
import journey.dto.flights.FlightsDTO;

import journey.repository.flights.FlightInfoAdminView;
import journey.service.flights.FlightsSearchService;
import journey.service.flights.FlightsService;

@RestController
@RequestMapping("/flights")
// @CrossOrigin(origins = "http://localhost:5173") // 允许跨域请求
public class FlightsController {

    @Autowired
    private FlightsSearchService flightsSearchService;
    @Autowired
    private FlightsService flightsService;

    // 获取所有航班信息
    @GetMapping("/all")
    public List<FlightsDTO> getAllFlights() {
        return flightsSearchService.getAllFlights();
    }

    // 获取所有航班详细信息
    // @GetMapping("/details")
    // public List<FlightsDetailsDTO> getAllFlightDetails() {
    // return flightsSearchService.getAllFlightsDetails();
    // }

    // 搜索航班信息
    @GetMapping("/search")
    public List<FlightsDTO> getFlightDetailsByCriteria(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate returnDate,
            @RequestParam(required = false) String cabinClass) {
        List<FlightsDetailsBean> results = flightsSearchService.findByCriteria(departure, destination, departDate,
                cabinClass, returnDate);
        List<FlightsDTO> dtos = new ArrayList<>();
        for (FlightsDetailsBean detail : results) {
            FlightsDTO dto = new FlightsDTO();
            dto.setFlightNumber(detail.getFlight().getFlightNumber());
            dto.setAirline(detail.getFlight().getAirline().getAirlineName());
            dto.setImageURL(detail.getFlight().getAirline().getImageURL());
            dto.setDepartureAirport(detail.getFlight().getDepartureAirport().getAirportCode());
            dto.setArrivalAirport(detail.getFlight().getArrivalAirport().getAirportCode());
            dto.setDepartureNameZh(detail.getFlight().getDepartureAirport().getCity().getNameZh());
            dto.setArrivalNameZh(detail.getFlight().getArrivalAirport().getCity().getNameZh());
            dto.setDepartureTime(detail.getFlight().getDepartureTime());
            dto.setArrivalTime(detail.getFlight().getArrivalTime());
            dto.setDuration(detail.getFlight().getDuration());
            dto.setDepartureTerminal(detail.getFlight().getDepartureTerminal());
            dto.setArrivalTerminal(detail.getFlight().getArrivalTerminal());
            dto.setFlightclass(detail.getFlightclass() != null ? detail.getFlightclass().getClassType() : "未知艙等");
            dto.setClassPrice(detail.getClassPrice());
            dto.setClassCapacity(detail.getClassCapacity());
            dto.setClassAvailable(detail.getClassAvailable());
            dto.setBaggageAllowance(detail.getBaggageAllowance());
            dtos.add(dto);
        }

        return dtos;
    }

    // @GetMapping("/search")
    // public List<FlightsDetailsBean> getFlightDetailsByCriteria(
    // @RequestParam(required = false) String departure,
    // @RequestParam(required = false) String destination,
    // @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
    // LocalDate departDate,
    // @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
    // LocalDate returnDate,
    // @RequestParam(required = false) String cabinClass) {
    // System.out.println("搜尋參數 => " + departure + ", " + destination + ", " +
    // departDate);

    // return flightsSearchService.findByCriteria(departure, destination,
    // departDate, cabinClass, returnDate);
    // }

    // 多行程查询
    // @PostMapping("/search/multi")
    // public List<FlightsDetailsBean> getMultiTripFlights(@RequestBody
    // MultiTripRequest request) {
    // return flightsSearchService.findMultiTripFlights(request.getTrips(),
    // request.getCabinClass());
    // }

    // 多行程查询
    @PostMapping("/search/multi")
    public List<FlightsDTO> getMultiTripFlights(@RequestBody MultiTripRequest request) {
        List<FlightsDetailsBean> results = flightsSearchService.findMultiTripFlights(
                request.getTrips(), request.getCabinClass());
        List<FlightsDTO> dtos = new ArrayList<>();
        for (FlightsDetailsBean detail : results) {
            FlightsDTO dto = new FlightsDTO();
            dto.setFlightNumber(detail.getFlight().getFlightNumber());
            dto.setAirline(detail.getFlight().getAirline().getAirlineName());
            dto.setImageURL(detail.getFlight().getAirline().getImageURL());
            dto.setDepartureAirport(detail.getFlight().getDepartureAirport().getAirportCode());
            dto.setArrivalAirport(detail.getFlight().getArrivalAirport().getAirportCode());
            dto.setDepartureNameZh(detail.getFlight().getDepartureAirport().getCity().getNameZh());
            dto.setArrivalNameZh(detail.getFlight().getArrivalAirport().getCity().getNameZh());
            dto.setDepartureTime(detail.getFlight().getDepartureTime());
            dto.setArrivalTime(detail.getFlight().getArrivalTime());
            dto.setDuration(detail.getFlight().getDuration());
            dto.setDepartureTerminal(detail.getFlight().getDepartureTerminal());
            dto.setArrivalTerminal(detail.getFlight().getArrivalTerminal());
            dto.setFlightclass(detail.getFlightclass() != null ? detail.getFlightclass().getClassType() : "未知艙等");
            dto.setClassPrice(detail.getClassPrice());
            dto.setClassCapacity(detail.getClassCapacity());
            dto.setClassAvailable(detail.getClassAvailable());
            dto.setBaggageAllowance(detail.getBaggageAllowance());
            dtos.add(dto);
        }

        return dtos;
    }

    // 新增航班
    @PostMapping("/create")
    public ResponseEntity<String> createFlight(@RequestBody @Valid FlightCreateRequestDTO dto) {

        try {
            flightsService.createFlight(dto);
            return ResponseEntity.ok("航班新增成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("航班新增失敗: " + e.getMessage());
        }
    }

    // 依據航班ID新增艙等資訊
    @PostMapping("/create/details")
    public ResponseEntity<String> createFlightDetails(@RequestBody FlightDetailsRequestDTO dto) {
        try {
            flightsService.createFlightDetails(dto);
            return ResponseEntity.ok("航班艙等資訊新增成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("新增失敗: " + e.getMessage());
        }
    }

    // 刪除航班
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable("id") Integer id) {
        try {
            flightsService.deleteFlightById(id);
            return ResponseEntity.ok("航班刪除成功，ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("航班刪除失敗: " + e.getMessage());
        }
    }

    // 後端使用者查詢航班所有資訊
    @GetMapping("/allinformation")
    public List<FlightInfoAdminView> getAdminFlightInfo() {
        return flightsSearchService.getAdminFlightInfoList();
    }

}
