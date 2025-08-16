package journey.repository.attractions;
// package journey.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
// import journey.domain.AttractionTicketsOrder;

// import java.util.List;
// import java.util.Optional;

// @Repository
// public interface AttractionTicketsOrderRepository extends JpaRepository<AttractionTicketsOrder, Integer> {

//     // 根據訂單編號查詢
//     Optional<AttractionTicketsOrder> findByOrderNo(String orderNo);

//     // 查詢某個行程的所有訂單
//     List<AttractionTicketsOrder> findByTripId(Integer tripId);

//     // 查詢付款紀錄
//     List<AttractionTicketsOrder> findByPaymentId(Integer paymentId);

//     // 查詢訂單狀態（例如：成功/未付款）
//     List<AttractionTicketsOrder> findByStatus(Boolean status);
// }