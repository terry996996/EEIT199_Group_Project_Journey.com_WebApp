package journey.service.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import journey.domain.users.AllUserBean;
import journey.domain.users.UserTypeBean;
import journey.dto.user.RegistrationFormDTO;
import journey.dto.user.UploadIconDTO;
import journey.repository.users.AllUserRepository;
import journey.service.uploads.UploadService;
import journey.utils.PasswordHashUtils;

/**
 * all user related services
 * 
 * @author 乃文
 * @version 2.0 - 07/03/2025 rename to AllUserServie, fixed bugs in icon
 *          relating methods. overload findUser by id, overload isUserExist by
 *          id, add updateEmail
 * 
 * @version 1.1 - 07/02/2025 add icons related methods
 * @version 1.0
 * @since 06/24/2025
 */
@Service
@Transactional
public class AllUserService {

    @Autowired
    private AllUserRepository allUserRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private UploadService uploadService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * find a user with specific id
     * 
     * @param id
     * @return null if not found
     */
    public AllUserBean findUser(Integer id) {
        if (id != null) {
            Optional<AllUserBean> opt = allUserRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    /**
     * find a user with specific username
     * 
     * @param username
     * @return null if not found
     */
    public AllUserBean findUser(String username) {
        if (username != null) {
            Optional<AllUserBean> opt = allUserRepository.findByUsername(username);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    /**
     * find a user with specific email
     * 
     * @param email
     * @return null if not found
     */
    public AllUserBean findByUserEmail(String email) {
        if (email != null) {
            Optional<AllUserBean> opt = allUserRepository.findByEmail(email);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }

    /**
     * check if a user id exists
     * 
     * @param id
     * @return true if exists
     */
    public boolean isUserExist(Integer id) {
        if (id != null) {
            return findUser(id) != null;
        }
        return false;
    }

    /**
     * check if a username exists
     * 
     * @param username
     * @return true if exists
     */
    public boolean isUserExist(String username) {
        if (username != null) {
            return findUser(username) != null;
        }
        return false;
    }

    /**
     * check if an email exists
     * 
     * @param email
     * @return true if exists
     */
    public boolean isEmailExist(String email) {
        if (email != null) {
            return findByUserEmail(email) != null;
        }
        return false;
    }

    /**
     * update user email
     * 
     * @param bean     the user
     * @param newEmail the new email
     * @return updated all_user entity if succeeds
     */
    public AllUserBean updateEmail(AllUserBean bean, String newEmail) {
        if (bean != null && newEmail != null) {
            if (!isEmailExist(newEmail)) {
                bean.setEmail(newEmail);
                return allUserRepository.save(bean);
            }
        }
        return null;
    }

    /**
     * normal user registration
     * 
     * @param form DTO from front end, should be validated by controller
     * @return the new user if succeeded
     */
    public AllUserBean register(RegistrationFormDTO form) {
        if (form != null) {
            if (!isUserExist(form.getUsername()) && !isEmailExist(form.getEmail())) {
                AllUserBean bean = new AllUserBean();
                bean.setUsername(form.getUsername());
                bean.setEmail(form.getEmail());

                String pwd = form.getPasswordString();
                bean.setPasswordHash(PasswordHashUtils.getPasswordHash(pwd));

                // normal user registration here

                UserTypeBean userType = userTypeService.findById(form.getUserType());
                bean.setUserType(userType);

                // add default icon
                addIcon(bean);

                return allUserRepository.save(bean);
            }
        }
        return null;
    }

    /**
     * login
     * 
     * @param username
     * @param passwordString
     * @return the user entity if valid password
     */
    public AllUserBean login(String username, String passwordString) {
        if (username != null && passwordString != null) {
            AllUserBean bean = findUser(username);
            if (bean != null) {
                if (PasswordHashUtils.isValidPassword(passwordString, bean.getPasswordHash())) {
                    return bean;
                }
            }
        }
        return null;
    }

    /**
     * add default user icon
     */
    public void addIcon(AllUserBean bean) {
        String defaultIcon = uploadDir + "avatars/default/user-default.svg";
        bean.setIcon(defaultIcon);

        try {
            String mimeType = Files.probeContentType(Paths.get(defaultIcon));
            bean.setIconType(mimeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * add user uploaded icon
     */
    public void addIcon(AllUserBean bean, String iconPath) {
        String defaultIcon = uploadDir + "avatars/user-default.svg";
        bean.setIcon(defaultIcon);

        try {
            String mimeType = Files.probeContentType(Paths.get(defaultIcon));
            bean.setIconType(mimeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update user icon (if not exist then set to null)
     * 
     * @param username
     * @param iconPath the path to the existing file received from controller
     * @return all_user entity that sets the selected file as icon
     * @throws IOException when selected file does not exist
     */
    public AllUserBean updateIcon(String username, String iconPath) throws IOException {
        if (username != null) {
            Optional<AllUserBean> opt = allUserRepository.findByUsername(username);
            if (opt.isPresent() && opt != null) {
                AllUserBean bean = opt.get();
                if (iconPath != null) {
                    if (Files.exists(Paths.get(iconPath))) {
                        bean.setIcon(iconPath);
                        try {
                            bean.setIconType(Files.probeContentType(Paths.get(iconPath)));
                            return allUserRepository.save(bean);
                        } catch (IOException e) {
                            System.err.println("Error when getting content type at updateIcon()");
                            e.printStackTrace();
                        }
                    } else {
                        throw new IOException("Not Existing File!");
                    }
                } else {
                    // set to default icon
                    addIcon(bean);
                    return allUserRepository.save(bean);
                }
            }
        }
        return null;
    }

    /**
     * upload and change user icon
     * 
     * @param username User name
     * @param iconFile MultipartFile received from the controller
     * @return all_user entity that sets the latest upload file as icon
     */
    public AllUserBean uploadIcon(String username, MultipartFile iconFile) {
        if (username != null && iconFile != null) {
            AllUserBean bean = findUser(username);
            if (bean != null) {
                try {
                    UploadIconDTO dto = uploadService.storeIcon(iconFile, bean.getId());
                    bean.setIcon(dto.getIconPath());
                    bean.setIconType(dto.getMimeType());
                    return allUserRepository.save(bean);
                } catch (IOException e) {
                    System.out.println("Error at AllUserService updateIcon()");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean childEdited(Integer id) {
        if (id != null) {
            AllUserBean bean = findUser(id);
            if (bean != null) {
                bean.setLastModified(LocalDateTime.now());
                if (allUserRepository.save(bean) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 獲取所有一般用戶列表 (管理員功能)
     * 
     * @author Max
     * @since 2025-07-08
     * @return 一般用戶列表 DTO (只包含 user_type = 1 的用戶)
     */
    public List<journey.dto.admin.UserListDTO> getAllUsers() {
        List<AllUserBean> allUsers = allUserRepository.findAll();
        
        return allUsers.stream()
            .filter(user -> user.getUserType().getId() == 1) // 只篩選一般用戶 (user_type = 1)
            .map(user -> {
                journey.dto.admin.UserListDTO dto = new journey.dto.admin.UserListDTO();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                dto.setUserType(user.getUserType().getType().name());
                dto.setCreatedAt(user.getCreatedAt().toString());
                return dto;
            })
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 刪除用戶 (管理員功能)
     * 
     * @author Max
     * @since 2025-07-08
     * @param userId 用戶ID
     */
    public void deleteUser(Integer userId) {
        AllUserBean user = allUserRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("用戶不存在"));
        
        allUserRepository.delete(user);
    }
}