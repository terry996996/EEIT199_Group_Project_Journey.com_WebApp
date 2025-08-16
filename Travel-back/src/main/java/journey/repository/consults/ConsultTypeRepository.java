package journey.repository.consults;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.consultationmessageboard.ConsultTypeBean;

public interface ConsultTypeRepository extends JpaRepository<ConsultTypeBean, Integer> {
}
