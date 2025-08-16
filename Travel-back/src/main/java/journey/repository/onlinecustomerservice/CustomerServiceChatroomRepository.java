package journey.repository.onlinecustomerservice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.onlinecustomerservice.CustomerServiceChatroomBean;
import journey.domain.payments.TypeEnumBean;
import journey.domain.users.UserBean;

@Repository
public interface CustomerServiceChatroomRepository extends JpaRepository<CustomerServiceChatroomBean, Integer> {

    // 根據用戶和主題類型查找聊天室
    Optional<CustomerServiceChatroomBean> findByUserAndType(UserBean user, TypeEnumBean type);

    // 定義一個方法來查找某個用戶的所有聊天室
    List<CustomerServiceChatroomBean> findByUser(UserBean user);
}
