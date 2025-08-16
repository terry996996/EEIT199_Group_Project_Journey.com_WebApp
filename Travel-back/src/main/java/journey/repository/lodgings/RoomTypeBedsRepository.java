package journey.repository.lodgings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import journey.domain.lodgings.RoomTypeBedsBean;
import journey.domain.lodgings.RoomTypeBedsBean.RoomTypeBedsId;

@Repository
public interface RoomTypeBedsRepository extends JpaRepository<RoomTypeBedsBean, RoomTypeBedsId> {

    /**
     * 根據房型ID查詢所有床型關聯
     */
    @Query("SELECT rtb FROM RoomTypeBedsBean rtb " +
            "LEFT JOIN FETCH rtb.bedType bt " +
            "WHERE rtb.roomTypeId = :roomTypeId")
    List<RoomTypeBedsBean> findByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    /**
     * 根據房型ID刪除所有床型關聯
     */
    @Modifying
    @Query("DELETE FROM RoomTypeBedsBean rtb WHERE rtb.roomTypeId = :roomTypeId")
    void deleteByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

}
