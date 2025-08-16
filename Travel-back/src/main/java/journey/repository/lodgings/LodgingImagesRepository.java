package journey.repository.lodgings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.lodgings.LodgingImagesBean;

@Repository
public interface LodgingImagesRepository extends JpaRepository<LodgingImagesBean, Integer> {

        /**
         * 根據住宿ID查詢有效的圖片
         */
        @Query("SELECT li FROM LodgingImagesBean li WHERE li.lodging.id = :lodgingId ORDER BY li.sortOrder ASC")
        List<LodgingImagesBean> findByLodgingIdAndIsActiveTrueAndDeleteStatus(@Param("lodgingId") Integer lodgingId,
                        @Param("deleteStatus") Integer deleteStatus);

        /**
         * 根據房型ID查詢有效的圖片
         */
        @Query("SELECT li FROM LodgingImagesBean li WHERE li.roomType.id = :roomTypeId ORDER BY li.sortOrder ASC")
        List<LodgingImagesBean> findByRoomTypeIdAndIsActiveTrueAndDeleteStatus(@Param("roomTypeId") Integer roomTypeId,
                        @Param("deleteStatus") Integer deleteStatus);

        /**
         * 根據住宿ID查詢所有圖片（包括已軟刪除的）
         */
        @Query(value = "SELECT * FROM lodging_images WHERE lodging_id = :lodgingId ORDER BY sort_order ASC", nativeQuery = true)
        List<LodgingImagesBean> findByLodgingId(@Param("lodgingId") Integer lodgingId);

        /**
         * 根據房型ID查詢所有圖片（包括已軟刪除的）
         */
        @Query(value = "SELECT * FROM lodging_images WHERE room_type_id = :roomTypeId ORDER BY sort_order ASC", nativeQuery = true)
        List<LodgingImagesBean> findByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

}
