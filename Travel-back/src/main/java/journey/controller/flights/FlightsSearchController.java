// package journey.controller.flights;

// import java.time.LocalDate;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import journey.domain.flights.FlightsBean;
// import journey.domain.flights.FlightsDetailsBean;
// import journey.dto.MultiTripRequest;
// import journey.service.FlightsService;

// @RestController
// @RequestMapping("/flights")
// // @CrossOrigin(origins = "http://localhost:5173") // 允许跨域请求
// public class FlightsSearchController {

// @Autowired
// private FlightsService flightsService;

// // 获取所有航班信息
// @GetMapping("/all")
// public List<FlightsBean> getAllFlights() {
// return flightsService.getAllFlights();
// }

// // 获取所有航班详细信息
// @GetMapping("/details")
// public List<FlightsDetailsBean> getAllFlightDetails() {
// return flightsService.getAllFlightsDetails();
// }

// // 搜索航班信息
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

// return flightsService.findByCriteria(departure, destination, departDate,
// cabinClass, returnDate);
// }

// // 多行程查询
// @PostMapping("/search/multi")
// public List<FlightsDetailsBean> getMultiTripFlights(@RequestBody
// MultiTripRequest request) {
// return flightsService.findMultiTripFlights(request.getTrips(),
// request.getCabinClass());
// }

// }
