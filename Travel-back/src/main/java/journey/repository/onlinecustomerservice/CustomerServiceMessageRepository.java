package journey.repository.onlinecustomerservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.onlinecustomerservice.CustomerServiceChatroomBean;
import journey.domain.onlinecustomerservice.CustomerServiceMessageBean;

@Repository
public interface CustomerServiceMessageRepository extends JpaRepository<CustomerServiceMessageBean, Integer> {

    // 查找特定聊天室內的所有訊息，並按時間排序
    List<CustomerServiceMessageBean> findByChatRoomOrderBySentTimeAsc(CustomerServiceChatroomBean chatRoom);
}