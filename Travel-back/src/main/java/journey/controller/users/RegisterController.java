package journey.controller.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.users.AllUserBean;
import journey.domain.users.UserBean;
import journey.dto.user.ProfileDTO;
import journey.dto.user.RegisterUserDTO;
import journey.dto.user.RegistrationFormDTO;
import journey.service.user.AllUserService;
import journey.service.user.UserService;
import journey.utils.JwtUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/registrate")
public class RegisterController {

    @Autowired
    AllUserService allUserService;

    @Autowired
    UserService userService;

    @PostMapping("/submit")
    public ResponseEntity<?> registrate(@RequestBody RegisterUserDTO dto) {
        if (dto != null) {
            RegistrationFormDTO form = dto.getForm();
            ProfileDTO profile = dto.getProfile();

            // first insert into all_user
            if (!allUserService.isUserExist(form.getUsername()) &&
                    !allUserService.isEmailExist(form.getEmail())) {
                AllUserBean allUserBean = allUserService.register(form);

                // then add profile into users
                if (allUserBean != null) {
                    UserBean user = userService.newUser(allUserBean, profile);
                    if (user != null) {
                        String token = JwtUtil.createJWT(allUserBean);
                        return ResponseEntity
                                .ok()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .body("註冊成功！");
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body("註冊失敗！");
    }

    @PostMapping("/newProfile")
    public ResponseEntity<?> addNewProfile(@RequestBody ProfileDTO profile) {
        // TODO: process POST request

        return null;
    }

    @PostMapping("/checkUserName")
    public ResponseEntity<String> sameUserName(@RequestBody String username) {
        if (!allUserService.isUserExist(username)) {
            return ResponseEntity
                    .ok()
                    .body("帳號可以使用");
        }
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("帳號已被使用！");
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<String> sameEmail(@RequestBody String email) {
        if (!allUserService.isEmailExist(email)) {
            return ResponseEntity
                    .ok()
                    .body("電子郵件可以使用");
        }
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("電子郵件已被使用！");
    }

}
