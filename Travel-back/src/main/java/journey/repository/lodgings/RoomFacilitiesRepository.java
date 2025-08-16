package journey.repository.lodgings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.lodgings.RoomFacilitiesBean;
import journey.domain.lodgings.RoomFacilitiesBean.RoomFacilitiesId;

@Repository
public interface RoomFacilitiesRepository extends JpaRepository<RoomFacilitiesBean, RoomFacilitiesId> {

    /**
     * 根據房型ID查詢所有設施關聯
     */
    @Query("SELECT rf FROM RoomFacilitiesBean rf " +
            "LEFT JOIN FETCH rf.facility f " +
            "WHERE rf.roomTypeId = :roomTypeId")
    List<RoomFacilitiesBean> findByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    /**
     * 根據房型ID刪除所有設施關聯
     */
    @Modifying
    @Query("DELETE FROM RoomFacilitiesBean rf WHERE rf.roomTypeId = :roomTypeId")
    void deleteByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

}
