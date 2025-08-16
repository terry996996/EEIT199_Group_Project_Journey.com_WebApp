package journey.service.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.users.AllUserBean;
import journey.domain.users.VendorBean;
import journey.domain.users.VendorStatusBean;
import journey.dto.admin.CreateVendorRequestDTO;
import journey.dto.admin.CreateVendorResponseDTO;
import journey.dto.admin.VendorListDTO;
import journey.repository.users.AllUserRepository;
import journey.repository.users.UserTypeRepository;
import journey.repository.users.VendorRepository;
import journey.repository.users.VendorStatus;
import journey.utils.PasswordHashUtils;

/**
 * 商家服務層
 * 
 * @author Max
 * @since 2025-07-08
 */
@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AllUserRepository allUserRepository;

    @Autowired
    private VendorStatus vendorStatusRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 創建商家帳號
     */
    @Transactional
    public CreateVendorResponseDTO createVendor(CreateVendorRequestDTO request) {
        CreateVendorResponseDTO response = new CreateVendorResponseDTO();

        try {
            // 1. 驗證輸入資料
            if (request.getVendorName() == null || request.getVendorName().trim().isEmpty()) {
                throw new IllegalArgumentException("飯店名稱不能為空");
            }
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("商家帳號不能為空");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("密碼不能為空");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email不能為空");
            }

            // 2. 檢查商家帳號是否已存在
            Optional<AllUserBean> existingUser = allUserRepository.findByUsername(request.getUsername());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("商家帳號已存在");
            }

            // 3. 檢查Email是否已存在
            Optional<AllUserBean> existingEmail = allUserRepository.findByEmail(request.getEmail());
            if (existingEmail.isPresent()) {
                throw new IllegalArgumentException("Email已存在");
            }

            // 4. 創建用戶帳號
            AllUserBean newUser = new AllUserBean();
            newUser.setUsername(request.getUsername());
            newUser.setPasswordHash(PasswordHashUtils.getPasswordHash(request.getPassword()));
            newUser.setEmail(request.getEmail());

            String defaultIcon = uploadDir + "avatars/default/user-default.svg";
            newUser.setIcon(defaultIcon);

            try {
                String mimeType = Files.probeContentType(Paths.get(defaultIcon));
                newUser.setIconType(mimeType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // newUser.setIcon("default-admin-icon.png");
            // newUser.setIconType("png");
            // 需要設置 userType，先找到商家類型
            newUser.setUserType(userTypeRepository.findById(3).orElse(null));

            AllUserBean savedUser = allUserRepository.save(newUser);

            // 5. 創建商家資料
            VendorBean newVendor = new VendorBean();
            newVendor.setVendorName(request.getVendorName());
            // 設置預設狀態：正常 (status_id = 2)
            VendorStatusBean defaultStatus = vendorStatusRepository.findById(2).orElse(null);
            newVendor.setStatus(defaultStatus);
            newVendor.setAllUserBean(savedUser);

            VendorBean savedVendor = vendorRepository.save(newVendor);

            // 6. 設置回應資料
            response.setSuccess(true);
            response.setMessage("商家帳號創建成功");
            response.setVendorId(savedVendor.getId());
            response.setVendorName(savedVendor.getVendorName());
            response.setUsername(savedUser.getUsername());

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    /**
     * 獲取所有商家列表
     */
    public List<VendorListDTO> getAllVendors() {
        List<VendorBean> vendors = vendorRepository.findAll();

        return vendors.stream()
                .map(vendor -> {
                    VendorListDTO dto = new VendorListDTO();
                    dto.setId(vendor.getId());
                    dto.setVendorName(vendor.getVendorName());
                    dto.setUsername(vendor.getAllUserBean().getUsername());
                    dto.setEmail(vendor.getAllUserBean().getEmail());
                    dto.setStatus(vendor.getStatus().getType());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 刪除商家
     */
    @Transactional
    public void deleteVendor(Integer vendorId) {
        // 查找商家
        VendorBean vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("商家不存在"));

        // 獲取關聯的用戶帳號
        AllUserBean user = vendor.getAllUserBean();
        if (user == null) {
            throw new IllegalArgumentException("商家關聯的用戶帳號不存在");
        }

        // 先刪除商家資料
        vendorRepository.delete(vendor);

        // 再刪除用戶帳號
        allUserRepository.delete(user);
    }
}