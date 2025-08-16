package journey.controller.users;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.users.PartnerBean;
import journey.domain.users.UserBean;
import journey.dto.user.DeleteTimeDTO;
import journey.dto.user.NewPartnerDTO;
import journey.dto.user.ProfileDTO;
import journey.service.user.PartnerService;
import journey.service.user.UserService;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PartnerService partnerService;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPartners(@PathVariable Integer userId) {
        if (userId != null) {
            UserBean user = userService.findUser(userId);
            if (user != null) {
                return ResponseEntity.ok().body(partnerService.findAllPartnersByUser(user));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addPartner(@PathVariable Integer userId, @RequestBody ProfileDTO profile) {
        if (userId != null && profile != null) {
            UserBean user = userService.findUser(userId);
            if (user != null) {
                PartnerBean bean = partnerService.newPartner(user, profile);
                if (bean != null) {
                    return ResponseEntity.ok().body(new NewPartnerDTO(bean.getId(), bean.getLastModified()));
                }
            }
        }
        return ResponseEntity.badRequest().body("error when adding new Partner!");
    }

    @PutMapping("/update/{partnerId}")
    public ResponseEntity<?> updatePartner(
            @PathVariable Integer partnerId,
            @RequestBody ProfileDTO profile) {
        if (partnerId != null && profile != null) {
            PartnerBean partner = partnerService.updateProfile(partnerId, profile);
            if (partner != null) {

                return ResponseEntity.ok().body(partner.getLastModified().format(FORMATTER));
            }
        }
        return ResponseEntity.badRequest().body("error when updataing");
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<?> deletePartner(@PathVariable Integer partnerId) {
        if (partnerId != null) {
            PartnerBean bean = partnerService.removePartner(partnerId);
            if (bean != null) {
                return ResponseEntity.ok()
                        .body(new DeleteTimeDTO(
                                bean.getDeleteTime(),
                                bean.getLastModified()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("partner not found");
    }

    @PutMapping("recover/{partnerId}")
    public ResponseEntity<?> recoverPartner(@PathVariable Integer partnerId) {
        if (partnerId != null) {
            PartnerBean bean = partnerService.recoverPartner(partnerId);
            if (bean != null) {
                return ResponseEntity.ok().body(bean.getLastModified().format(FORMATTER));
            }
        }
        return ResponseEntity.badRequest().body("cannot undo the deletion");
    }

}
