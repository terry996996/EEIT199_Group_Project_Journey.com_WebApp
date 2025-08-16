package journey.repository.consults;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.consultationmessageboard.ReplyBean;

public interface ReplyRepository extends JpaRepository<ReplyBean, Integer> {

}
