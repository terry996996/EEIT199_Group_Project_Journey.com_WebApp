package journey.controller.traffictickets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.traffictickets.TrafficTicketsBean;
import journey.service.TrafficTicketsService;

@RestController
@RequestMapping("/traffictickets") // 定义请求路径前缀
public class TrafficTicketsController {

    // 注入TrafficTicketsService
    @Autowired
    private TrafficTicketsService trafficTicketsService;

    @GetMapping("/all") // 定义获取所有交通违章记录的请求路径
    //@CrossOrigin(origins = "http://localhost:5173") // 允许跨域请求
    public List<TrafficTicketsBean> getAllTrafficTickets() {
        return trafficTicketsService.findAllTickets();
    }

    // 使用GetMapping注解，表示该方法处理HTTP GET请求
    @GetMapping("/search")
    // 使用CrossOrigin注解，表示该方法允许跨域请求，允许的源为http://localhost:5173
    //@CrossOrigin(origins = "http://localhost:5173")
    // 定义一个方法，返回一个TrafficTicketsBean类型的列表，参数为name，表示要搜索的票证名称
    public List<TrafficTicketsBean> searchTickets(@RequestParam(required = false) String name) {
        // 调用trafficTicketsService的searchByName方法，传入name参数，返回搜索结果
        return trafficTicketsService.searchByName(name);
    }

    // @GetMapping("/search")
    // @CrossOrigin(origins = "http://localhost:5173")
    // public List<TrafficTicketsBean> searchTrafficTickets(
    // @RequestParam(required = false) String name,
    // @RequestParam(required = false) List<String> selectedTypes,
    // @RequestParam(required = false) List<String> selectedRegions,
    // @RequestParam(required = false) Integer minPrice,
    // @RequestParam(required = false) Integer maxPrice) {
    // return trafficTicketsService.searchTickets(name, selectedTypes,
    // selectedRegions, minPrice, maxPrice);
    // }

}
