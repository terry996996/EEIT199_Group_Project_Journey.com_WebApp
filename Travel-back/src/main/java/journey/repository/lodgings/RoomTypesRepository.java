package journey.repository.lodgings;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.lodgings.RoomTypesBean;
import journey.domain.comments.CommentsBean;

@Repository
public interface RoomTypesRepository extends JpaRepository<RoomTypesBean, Integer> {

        /**
         * 根據旅館ID查詢所有房型（簡歷）
         */
        @Query("SELECT rt FROM RoomTypesBean rt " +
                        "LEFT JOIN FETCH rt.lodging l " +
                        "LEFT JOIN FETCH rt.lodgingImages li " +
                        "WHERE rt.lodging.id = :lodgingId " +
                        "AND rt.deleteStatus = 1 " +
                        "AND l.deleteStatus = 1 " +
                        "ORDER BY rt.createdAt DESC")
        List<RoomTypesBean> findByLodgingId(@Param("lodgingId") Integer lodgingId);

        /**
         * 根據旅館ID和房型ID查詢房型詳細資訊
         */
        @Query("SELECT rt FROM RoomTypesBean rt " +
                        "LEFT JOIN FETCH rt.lodging l " +
                        "LEFT JOIN FETCH rt.roomTypeBeds rtb " +
                        "LEFT JOIN FETCH rt.roomFacilities rf " +
                        "LEFT JOIN FETCH rt.roomAvailability ra " +
                        "LEFT JOIN FETCH rt.lodgingImages li " +
                        "WHERE rt.id = :roomTypeId AND rt.lodging.id = :lodgingId " +
                        "AND rt.isActive = true AND rt.deleteStatus = 1 " +
                        "AND l.isActive = true AND l.deleteStatus = 1")
        Optional<RoomTypesBean> findByLodgingIdAndRoomTypeIdWithDetails(
                        @Param("lodgingId") Integer lodgingId,
                        @Param("roomTypeId") Integer roomTypeId);

        /**
         * 根據旅館ID、房型ID和商家ID查詢房型（用於權限驗證）
         */
        @Query("SELECT rt FROM RoomTypesBean rt " +
                        "LEFT JOIN FETCH rt.lodging l " +
                        "WHERE rt.id = :roomTypeId AND rt.lodging.id = :lodgingId " +
                        "AND l.vendor.id = :vendorId " +
                        "AND rt.deleteStatus = 1 " +
                        "AND l.deleteStatus = 1")
        Optional<RoomTypesBean> findByLodgingIdAndRoomTypeIdAndVendorId(
                        @Param("lodgingId") Integer lodgingId,
                        @Param("roomTypeId") Integer roomTypeId,
                        @Param("vendorId") Integer vendorId);

        // 評分統計已移至 UnifiedRatingRepository

        // 輕量查詢：只載入基本資訊，避免關聯載入
        @Query("SELECT rt FROM RoomTypesBean rt " +
                "WHERE rt.id = :id AND rt.isActive = true AND rt.deleteStatus = 1")
        Optional<RoomTypesBean> findByIdLightweight(@Param("id") Integer id);
}