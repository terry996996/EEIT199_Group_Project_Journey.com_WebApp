package journey.repository.payments;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.payments.TypeEnumBean;

@Repository
public interface TypeEnumRepository extends JpaRepository<TypeEnumBean, Integer> {
    Optional<TypeEnumBean> findByTypeId(Integer typeId);
}