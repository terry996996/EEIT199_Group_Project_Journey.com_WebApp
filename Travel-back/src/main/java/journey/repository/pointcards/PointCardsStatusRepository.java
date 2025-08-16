package journey.repository.pointcards;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import journey.domain.pointcards.PointCardsStatusBean;

public interface PointCardsStatusRepository extends JpaRepository<PointCardsStatusBean, Long> {
    @Query("SELECT COALESCE(SUM(ps.pointChange), 0) FROM PointCardsStatusBean ps WHERE ps.pointCard.pointCardId = :pointCardId")
    Integer sumPointsByPointCardId(@Param("pointCardId") UUID pointCardId);
}
