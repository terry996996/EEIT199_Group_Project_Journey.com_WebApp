package journey.repository.coupons;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import journey.domain.coupons.CouponInstancesBean;

public interface CouponInstancesRepository extends JpaRepository<CouponInstancesBean, UUID> {
    List<CouponInstancesBean> findByUserId(Integer userId);

    // 這樣在從資料庫取出 CouponInstancesBean 時，couponType 已經被填充
    @Query("SELECT ci FROM CouponInstancesBean ci JOIN FETCH ci.couponType WHERE ci.user.id = :userId")
    List<CouponInstancesBean> findByUserIdWithCouponType(@Param("userId") Integer userId);

}
