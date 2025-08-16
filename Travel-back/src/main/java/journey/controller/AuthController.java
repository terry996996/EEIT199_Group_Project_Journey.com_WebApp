package journey.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.users.AllUserBean;
import journey.dto.admin.LoginRequestDTO;
import journey.dto.admin.LoginResponseDTO;
import journey.dto.user.JwtDTO;
import journey.dto.user.RegistrationFormDTO;
import journey.repository.users.VendorRepository;
import journey.service.user.AllUserService;
import journey.utils.JwtUtil;
import journey.utils.PasswordHashUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AllUserService allUserService;
    @Autowired
    private VendorRepository vendorRepository;

    /**
     * 用戶登入
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            System.out.println("收到登入請求: " + loginRequest.getUsername());

            // 驗證輸入
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("帳號和密碼不能為空");
            }

            // 查找用戶
            AllUserBean user = allUserService.findUser(loginRequest.getUsername());
            if (user == null) {
                System.out.println("用戶不存在: " + loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
            }

            // 驗證密碼
            if (!PasswordHashUtils.isValidPassword(loginRequest.getPassword(), user.getPasswordHash())) {
                System.out.println("密碼錯誤: " + loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
            }

            // 生成JWT token
            String token = JwtUtil.createJWT(user);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token生成失敗");
            }

            // 查詢 vendorName（如果是VENDOR）
            String vendorName = null;
            if ("VENDOR".equals(user.getUserType().getType().name())) {
                var vendor = vendorRepository.findById(user.getId()).orElse(null);
                if (vendor != null) {
                    vendorName = vendor.getVendorName();
                }
            }

            // 建立回應
            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setUserType(user.getUserType().getType().name());
            response.setIcon(user.getIcon());
            response.setVendorName(vendorName);

            System.out.println("登入成功: " + user.getUsername() + ", 角色: " + user.getUserType().getType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("登入過程發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登入失敗，請稍後再試");
        }
    }

    /**
     * 用戶註冊
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationFormDTO registrationDTO) {
        try {
            System.out.println("收到註冊請求: " + registrationDTO.getUsername());

            // 驗證輸入
            if (registrationDTO.getUsername() == null || registrationDTO.getPasswordString() == null
                    || registrationDTO.getEmail() == null) {
                return ResponseEntity.badRequest().body("所有欄位都必須填寫");
            }

            // 檢查用戶名是否已存在
            if (allUserService.isUserExist(registrationDTO.getUsername())) {
                return ResponseEntity.badRequest().body("用戶名已存在");
            }

            // 檢查email是否已存在
            if (allUserService.isEmailExist(registrationDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Email已存在");
            }

            // 註冊用戶
            AllUserBean newUser = allUserService.register(registrationDTO);
            if (newUser == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("註冊失敗");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "註冊成功");
            response.put("userId", newUser.getId());
            response.put("username", newUser.getUsername());

            System.out.println("註冊成功: " + newUser.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("註冊過程發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("註冊失敗，請稍後再試");
        }
    }

    /**
     * 驗證JWT token
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            if (token == null) {
                return ResponseEntity.badRequest().body("Token不能為空");
            }

            JwtDTO jwtDTO = JwtUtil.verfiy(token);
            if (jwtDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token無效或已過期");
            }

            return ResponseEntity.ok(jwtDTO);

        } catch (Exception e) {
            System.err.println("Token驗證過程發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("驗證失敗");
        }
    }
}