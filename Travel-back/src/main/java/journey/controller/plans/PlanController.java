package journey.controller.plans;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.plans.TripPlanBean;
import journey.domain.users.UserBean;
import journey.dto.plans.PlanDTO;
import journey.service.plans.TripPlanService;
import journey.service.user.UserService;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private UserService userService;

    @Autowired
    private TripPlanService tripPlanService;

    @GetMapping("/getData/{userId}")
    public ResponseEntity<?> getPlans(@PathVariable Integer userId) {
        if (userId != null) {
            UserBean user = userService.findUser(userId);
            if (user != null) {
                return ResponseEntity.ok().body(tripPlanService.findAllPlans(user));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addPlan(@PathVariable Integer userId, @RequestBody PlanDTO dto) {
        if (userId != null && dto != null) {
            System.out.println(dto.getTitle());
            System.out.println(dto.getStartDate());
            System.out.println(dto.getEndDate());
            UserBean user = userService.findUser(userId);
            if (user != null) {
                TripPlanBean bean = tripPlanService.newPlan(user, dto);
                if (bean != null) {
                    PlanDTO response = new PlanDTO();
                    BeanUtils.copyProperties(bean, response, "createdBy");
                    dto.setCreatedBy(bean.getCreatedBy().getId());
                    return ResponseEntity.ok().body(response);
                }
            }
        }
        return ResponseEntity.badRequest().body("error when adding new plan!");
    }

}
