package journey.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import journey.domain.users.AllUserBean;
import journey.service.user.AllUserService;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    AllUserService allUserService;

    @Test
    void testCreateJWT() {
        String username = "test1";
        AllUserBean user = allUserService.findUser(username);
        String token = JwtUtil.createJWT(user);
        System.out.println(token);
    }

    @Test
    void testVerfiy() throws InterruptedException {
        String username = "test1";
        AllUserBean user = allUserService.findUser(username);
        String token = JwtUtil.createJWT(user);
        // Thread.sleep(1000 * 61);
        System.out.println(JwtUtil.verfiy(token));
    }
}
