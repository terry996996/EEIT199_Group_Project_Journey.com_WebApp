package journey.repository.onlinecustomerservice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.onlinecustomerservice.CustomerServiceQuestionBean;

@Repository
public interface CustomerServiceQuestionRepository extends JpaRepository<CustomerServiceQuestionBean, Integer> {
    // 根據 TypeEnumBean 來查找問題
    List<CustomerServiceQuestionBean> findByType_TypeId(Integer typeId); // 根據type_id查找問題

    Optional<CustomerServiceQuestionBean> findByQuestionTemplateId(Integer questionTemplateId);
}