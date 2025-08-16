package journey.service.user;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.users.AllUserBean;
import journey.domain.users.UserBean;
import journey.dto.user.ProfileDTO;
import journey.enums.GenderEnum;
import journey.repository.users.UserRepository;
import journey.service.locations.CountryService;

/**
 * user related services
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/03/2025
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AllUserService allUserService;

    @Autowired
    private CountryService countryService;

    public UserBean newUser(AllUserBean allUserBean, ProfileDTO profile) {
        if (allUserBean != null && profile != null) {
            UserBean user = new UserBean();
            BeanUtils.copyProperties(profile, user, "email", "phoneCode", "residence", "nationality", "gender");
            user.setAllUserBean(allUserBean);
            user.setGender(GenderEnum.fromCode(profile.getGender()));
            user.setNationality(countryService.findCountry(profile.getNationality()));
            user.setResidence(countryService.findCountry(profile.getResidence()));
            return userRepository.save(user);
        }
        return null;
    }

    public String findUserEmailById(Integer id) {
        if (id != null) {
            AllUserBean allUserBean = allUserService.findUser(id);
            if (allUserBean != null) {
                return allUserBean.getEmail();
            }
        }
        return null;
    }

    /**
     * find usertype = "USER" by id
     * 
     * @return user entity if exists
     */
    public UserBean findUser(Integer id) {
        if (id != null) {
            Optional<UserBean> opt = userRepository.findById(id);
            if (opt.isPresent() && opt != null) {
                return opt.get();
            }
        }
        return null;
    }

    /**
     * udpate user profile (except email)
     * 
     * @param id      user id
     * @param profile dto from frontend
     * @return updated user entity if succeeds
     */
    public UserBean updateProfile(Integer id, ProfileDTO profile) {
        if (id != null && profile != null) {
            if (allUserService.isUserExist(id)) {
                UserBean bean = findUser(id);
                if (bean == null) {
                    bean = new UserBean();
                }
                // ignore email column (should be updated by AllUserService)
                // deal with FKs and Enums later
                BeanUtils.copyProperties(profile, bean, "email", "gender", "nationality", "residence", "phoneCode");

                bean.setGender(GenderEnum.fromCode(profile.getGender()));
                bean.setNationality(countryService.findCountry(profile.getNationality()));
                bean.setResidence(countryService.findCountry(profile.getResidence()));

                if (allUserService.childEdited(id)) {
                    return userRepository.save(bean);
                }
            }
        }
        return null;
    }
}
