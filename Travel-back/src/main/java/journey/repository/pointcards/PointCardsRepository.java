package journey.repository.pointcards;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import journey.domain.pointcards.PointCardsBean;

@Repository
public interface PointCardsRepository extends JpaRepository<PointCardsBean, UUID> {
    Optional<PointCardsBean> findByUserId(Integer userId);

}
