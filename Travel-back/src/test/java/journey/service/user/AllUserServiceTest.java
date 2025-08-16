package journey.service.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import journey.domain.users.AllUserBean;
import journey.dto.user.RegistrationFormDTO;

@SuppressWarnings("unused")
@SpringBootTest
public class AllUserServiceTest {
    @Autowired
    private AllUserService allUserService;

    @Test
    void testRegister() {
        RegistrationFormDTO dto = new RegistrationFormDTO();
        dto.setUsername("user12345");
        dto.setPasswordString("pwd12345");
        dto.setEmail("aaa@bbb.ccc");

        System.out.println(allUserService.register(dto));
    }

    @Test
    void testUploadIcon() throws IOException {
        // Path path = Paths.get("uploads/avatars/test/avatar.png");
        // String orgName = path.getFileName().toString();
        // byte[] file = Files.readAllBytes(path);
        // MultipartFile mockMultipartFile = new MockMultipartFile(
        // "file",
        // orgName,
        // Files.probeContentType(path),
        // file);
        // AllUserBean bean = allUserService.uploadIcon("test1", mockMultipartFile);
        AllUserBean bean = allUserService.updateIcon("test1", null);
        System.out.println(bean);
    }

    @Test
    void testIsUserExist() {
        System.out.println(allUserService.isUserExist("user12345"));
    }

    @Test
    @Transactional
    void testLogin() {
        System.out.println(allUserService.login("admin", "Admin@Journey"));
    }

}
