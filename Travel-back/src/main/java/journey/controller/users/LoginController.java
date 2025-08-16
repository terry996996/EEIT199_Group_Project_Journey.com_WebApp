package journey.controller.users;

import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import jakarta.transaction.Transactional;
import journey.domain.users.AllUserBean;
import journey.dto.user.LoginDTO;
import journey.service.user.AllUserService;
import journey.utils.JwtUtil;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AllUserService allUserService;

    @PostMapping("/")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginForm) {
        String username = loginForm.getUsername();
        String pwd = loginForm.getPasswordString();
        System.out.println("test1");
        AllUserBean bean = allUserService.login(username, pwd);
        System.out.println(bean);
        if (bean != null) {
            System.out.println("test2");
            String token = JwtUtil.createJWT(bean);
            if (token != null) {
                System.out.println("test3");
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body("登入成功！");
            }
        }
        // status code = 401
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(Map.of("error", "帳號或密碼錯誤！"));
    }
}
