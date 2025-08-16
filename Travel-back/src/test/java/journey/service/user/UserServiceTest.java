package journey.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import journey.dto.user.RegistrationFormDTO;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testRegister() {
        RegistrationFormDTO dto = new RegistrationFormDTO();
        dto.setUsername("test1");
        dto.setPasswordString("pwd1234");
        dto.setEmail("aaa@bbb.ccc");

        // System.out.println(userService.register(dto));
    }
}
