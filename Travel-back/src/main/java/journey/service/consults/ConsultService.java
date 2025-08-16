package journey.service.consults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journey.domain.consultationmessageboard.ConsultTypeBean;
import journey.domain.consultationmessageboard.ConsultsBean;
import journey.domain.users.UserBean;
import journey.dto.consults.ConsultRequest;
import journey.repository.consults.ConsultTypeRepository;
import journey.repository.consults.ConsultsRepository;
import journey.repository.consults.ReplyRepository;
import journey.repository.users.AdminRepository;
import journey.repository.users.UserRepository;

@Service
public class ConsultService {
    @Autowired
    private ConsultsRepository consultsRepository;

    @Autowired
    private ConsultTypeRepository consultTypeRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // (1) 獲取用戶資訊
    public UserBean getLoggedInUser() {
        Optional<UserBean> userOptional = userRepository.findById(1);
        return userOptional.orElse(null);
    }

    // (2) 獲取所有諮詢類型
    public List<ConsultTypeBean> getAllConsultTypes() {
        return consultTypeRepository.findAll();
    }

    public ConsultsBean submitConsult(ConsultRequest consultRequest) {
        ConsultsBean consult = new ConsultsBean();
        consult.setName(consultRequest.getContactName());
        consult.setGender(consultRequest.getGender().equals("先生") ? 1 : 2);
        consult.setEmail(consultRequest.getEmail());
        consult.setPhone(consultRequest.getPhoneNumber());
        consult.setConsultContent(consultRequest.getDetails());
        consult.setConsultTime(LocalDateTime.now());

        // 查找諮詢類型 (根據 ID 查找)
        if (consultRequest.getFeedbackTypeId() == null) {
            throw new IllegalArgumentException("諮詢類型必須選擇。"); // <-- 改寫這裡的訊息
        }

        Optional<ConsultTypeBean> consultTypeOptional = consultTypeRepository
                .findById(consultRequest.getFeedbackTypeId());
        if (consultTypeOptional.isEmpty()) {
            throw new IllegalArgumentException("您選擇的諮詢類型不存在。 ID: " + consultRequest.getFeedbackTypeId());
        }
        consult.setConsultType(consultTypeOptional.get());

        Optional<UserBean> userOptional = userRepository.findById(1);
        if (userOptional.isPresent()) {
            consult.setUser(userOptional.get());
        } else {
            throw new RuntimeException(
                    "User with ID not found in the database. Please ensure a user with ID exists for testing.");
        }

        return consultsRepository.save(consult);
    }

}
