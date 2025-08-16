package journey.controller.consults;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.consultationmessageboard.ConsultTypeBean;
import journey.domain.users.UserBean;
import journey.dto.consults.ConsultRequest;
import journey.service.consults.ConsultService;

@RestController
@RequestMapping("/api")
public class ConsultController {

    @Autowired
    private ConsultService consultService;

    // (1) 獲取用戶資訊 API
    @GetMapping("/user/info")
    public ResponseEntity<UserBean> getUserInfo() {
        UserBean user = consultService.getLoggedInUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.noContent().build(); // 204 No Content 表示沒有登入用戶資訊(沒有登入的用戶來使用)
        }
    }

    // (2) 獲取諮詢類型 API
    @GetMapping("/consult/types")
    public ResponseEntity<List<ConsultTypeBean>> getConsultTypes() {
        List<ConsultTypeBean> types = consultService.getAllConsultTypes();
        return ResponseEntity.ok(types);
    }

    // (3) 提交諮詢 API
    @PostMapping("/consults")
    public ResponseEntity<String> submitConsult(@RequestBody ConsultRequest consultRequest) {
        try {
            consultService.submitConsult(consultRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("諮詢提交成功！");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("諮詢提交失敗：" + e.getMessage());
        }
    }
}
