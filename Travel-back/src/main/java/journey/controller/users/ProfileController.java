package journey.controller.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.users.UserBean;
import journey.dto.user.ProfileDTO;
import journey.service.user.AllUserService;
import journey.service.user.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private AllUserService allUserService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Integer id) {
        if (id != null) {
            UserBean bean = userService.findUser(id);
            return ResponseEntity.ok().body(bean2DTO(bean, id));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateProfile(@PathVariable Integer id, @RequestBody ProfileDTO dto) {
        if (id != null && dto != null) {
            UserBean bean = userService.updateProfile(id, dto);
            if (bean != null) {
                return ResponseEntity.ok().body(bean2DTO(bean, id));
            }
        }
        return ResponseEntity.badRequest().body("Error Updating User Profile");
    }

    private ProfileDTO bean2DTO(UserBean bean, Integer id) {
        if (bean != null) {
            ProfileDTO profile = new ProfileDTO();
            BeanUtils.copyProperties(bean, profile, "gender", "nationality", "residence");
            profile.setEmail(userService.findUserEmailById(id));
            profile.setGender(bean.getGender().getCode());
            profile.setNationality(bean.getNationality().getId());
            profile.setResidence(bean.getResidence().getId());
            profile.setLastModified(allUserService.findUser(id).getLastModified());
            return profile;
        }
        return null;
    }
}
