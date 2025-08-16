package journey.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.users.UserTypeBean;
import journey.repository.users.UserTypeRepository;

/**
 * user related services
 * 
 * @author 乃文
 * @version 1.0
 * @since 06/24/2025
 */
@Service
@Transactional
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    /**
     * get the usertype by primary key
     * 
     * @param id
     * @return null if not found
     */
    public UserTypeBean findById(Integer id) {
        if (id != null) {
            Optional<UserTypeBean> opt = userTypeRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }
}
