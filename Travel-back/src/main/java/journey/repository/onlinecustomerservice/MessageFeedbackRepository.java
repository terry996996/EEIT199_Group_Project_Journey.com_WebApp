package journey.repository.onlinecustomerservice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.onlinecustomerservice.MessageFeedbackBean;

@Repository
public interface MessageFeedbackRepository extends JpaRepository<MessageFeedbackBean, Integer> {

    Optional<MessageFeedbackBean> findByMessage_MessageId(Integer messageId);

    // 根據聊天室 ID 查找所有相關訊息的評點
    List<MessageFeedbackBean> findByMessage_ChatRoom_ChatRoomId(Integer chatRoomId);
}
