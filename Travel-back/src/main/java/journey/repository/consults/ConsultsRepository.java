package journey.repository.consults;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.consultationmessageboard.ConsultsBean;

public interface ConsultsRepository extends JpaRepository<ConsultsBean, Integer> {

}
