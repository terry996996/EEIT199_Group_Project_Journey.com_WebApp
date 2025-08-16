package journey.repository.lodgings;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import journey.domain.lodgings.LodgingsBean;
import journey.dto.lodgings.LodgingHotSearchResponseDTO;

public interface LodgingsRepository extends JpaRepository<LodgingsBean, Integer> {

  // 查含日期、人數、可住房間（複雜查詢）- 實作步驟 1,2,3
  @Query(value = """
      /* === 城市名稱 → city_id === */
      DECLARE @cityId INT = (
          SELECT TOP 1 id
          FROM   cities
          WHERE  name = :cityName OR name_zh = :cityName
      );

      /*-------------------------------------------------
        STEP 0：挑出「整段日期每天剩房 > 2」的房型
      -------------------------------------------------*/
      DECLARE @EligibleRoomTypes TABLE (room_type_id INT PRIMARY KEY, lodging_id INT);

      INSERT INTO @EligibleRoomTypes(room_type_id, lodging_id)
      SELECT  rt.id,
              rt.lodging_id
      FROM    room_types rt
      JOIN    lodgings l ON l.id = rt.lodging_id
      JOIN    room_availability ra ON ra.room_type_id = rt.id
      WHERE   rt.max_guests >= :guestCount
        AND   rt.is_active = 1
        AND   rt.delete_status = 1
        AND   l.is_active = 1
        AND   l.delete_status = 1
        AND   ra.[date] BETWEEN :checkInDate AND :checkOutDate
      GROUP BY rt.id, rt.lodging_id
      HAVING  MIN(ra.available_rooms) > 2;      -- ★ 每天都還有 >2 間

      /*-------------------------------------------------
        STEP 1：把符合的旅館 id 丟進暫存表
      -------------------------------------------------*/
      DECLARE @CandidateLodgings TABLE (id INT PRIMARY KEY);

      INSERT INTO @CandidateLodgings(id)
      SELECT DISTINCT lodging_id
      FROM   @EligibleRoomTypes;

      /*-------------------------------------------------
        STEP 2：計算每間旅館的 avgRating / reviewCnt
                → 存進 table variable，後面可用多次
      -------------------------------------------------*/
      DECLARE @RatingStats TABLE (
          lodging_id INT PRIMARY KEY,
          avgRating  DECIMAL(5,2),
          reviewCnt  BIGINT
      );

      INSERT INTO @RatingStats(lodging_id, avgRating, reviewCnt)
      SELECT lodging_id,
             AVG(CAST(rating AS FLOAT)) AS avgRating,
             COUNT(*)                   AS reviewCnt
      FROM   comments
      WHERE  rating IS NOT NULL
        AND  is_active = 1
        AND  delete_status = 1
        AND  parent_comment_id IS NULL
        AND  created_at >= DATEADD(DAY, -30, GETDATE())
        AND  lodging_id IN (SELECT id FROM @CandidateLodgings)
      GROUP BY lodging_id;

      /*-------------------------------------------------
        STEP 3：抓每房型的第一張圖片
      -------------------------------------------------*/
      ;WITH first_img AS (
          SELECT  room_type_id,
                  MIN(image_id) AS imgId
          FROM    lodging_images
          WHERE   image_type   = 'room'
            AND   is_active    = 1
            AND   delete_status= 1
          GROUP BY room_type_id
      )

      /*-------------------------------------------------
        RESULT #1 —— 顯示用資料
      -------------------------------------------------*/
      SELECT
          l.id                       AS lodgingId,
          l.lodging_name             AS lodgingName,
          ISNULL(rs.avgRating,0)     AS avgRating,
          ISNULL(rs.reviewCnt,0)     AS reviewCount,
          rt.id                      AS roomTypeId,
          rt.[name]                  AS roomTypeName,
          rt.[description]           AS roomTypeDescription,
          rt.max_guests              AS maxGuests,
          rt.price_per_night         AS pricePerNight,
          img.image_url              AS imageUrl,
          l.lodging_type             AS lodgingTypeId,
          lt.type_text               AS lodgingType,
          ISNULL(bed_types.bed_types_json, '[]') AS bedTypes,
          ISNULL(facilities.facilities_json, '[]') AS facilities
      FROM        @EligibleRoomTypes ert
      JOIN        room_types         rt   ON rt.id = ert.room_type_id
      JOIN        lodgings           l    ON l.id  = ert.lodging_id
      LEFT JOIN   lodgings_type      lt   ON lt.id = l.lodging_type
      LEFT JOIN   @RatingStats       rs   ON rs.lodging_id = l.id
      LEFT JOIN   first_img          fi   ON fi.room_type_id = rt.id
      LEFT JOIN   lodging_images     img  ON img.image_id   = fi.imgId
      LEFT JOIN   (
          SELECT  rtb.room_type_id,
                  '[' + STRING_AGG(
                      '{"id":' + CAST(bt.id AS VARCHAR) + ',"name":"' + bt.[name] + '","quantity":' + CAST(rtb.quantity AS VARCHAR) + '}',
                      ','
                  ) + ']' AS bed_types_json
          FROM    room_type_beds rtb
          JOIN    bed_types      bt ON bt.id = rtb.bed_type_id
          WHERE   rtb.room_type_id IN (SELECT room_type_id FROM @EligibleRoomTypes)
          GROUP BY rtb.room_type_id
      ) bed_types ON bed_types.room_type_id = rt.id
      LEFT JOIN   (
          SELECT  rf.room_type_id,
                  '[' + STRING_AGG(
                      '{"id":' + CAST(fc.id AS VARCHAR) + ',"name":"' + fc.facility_name + '"}',
                      ','
                  ) + ']' AS facilities_json
          FROM    room_facilities rf
          JOIN    facilities      fc ON fc.id = rf.facility_id
          WHERE   rf.room_type_id IN (SELECT room_type_id FROM @EligibleRoomTypes)
          GROUP BY rf.room_type_id
      ) facilities ON facilities.room_type_id = rt.id
      WHERE       l.city_id = @cityId
      ORDER BY    l.lodging_name;
      """, nativeQuery = true)
  List<Object[]> searchAvailableRoomTypes(
      @Param("cityName") String cityName,
      @Param("checkInDate") LocalDate checkInDate,
      @Param("checkOutDate") LocalDate checkOutDate,
      @Param("guestCount") Integer guestCount);

  // 熱門旅宿（使用統一評分標準）
  @Query(value = """
      ;WITH stats AS (
        SELECT  l.id,
                l.lodging_name,
                l.city_id,
                AVG(CAST(c.rating AS FLOAT))  AS avg_score,
                COUNT_BIG(c.id)               AS review_cnt
        FROM    lodgings  l
        JOIN    comments  c  ON c.lodging_id = l.id
        WHERE   l.is_active      = 1  AND l.delete_status   = 1  AND l.deleted_time IS NULL
          AND   c.is_active      = 1  AND c.delete_status   = 1  AND c.rating IS NOT NULL
          AND   c.parent_comment_id IS NULL
          AND   c.created_at >= DATEADD(DAY, -30, GETDATE())
        GROUP BY l.id, l.lodging_name, l.city_id
        HAVING   AVG(CAST(c.rating AS FLOAT)) >= 4
           AND   COUNT_BIG(c.id) >= 5
      )
      SELECT TOP (:topRows)
             s.id, s.lodging_name, city.name_zh,
             s.avg_score, s.review_cnt,
             img.image_url
      FROM   stats AS s
      JOIN   cities city ON city.id = s.city_id
      OUTER APPLY (
        SELECT TOP 1 image_url
        FROM   lodging_images li
        WHERE  li.lodging_id = s.id
          AND  li.is_active = 1
          AND  li.delete_status = 1
        ORDER BY li.sort_order, li.image_id
      ) AS img
      ORDER BY s.avg_score DESC, s.review_cnt DESC
      """, nativeQuery = true)
  List<LodgingHotSearchResponseDTO> findTopRatedLodgings(@Param("topRows") int topRows);

  // 根據供應商ID和刪除狀態查詢旅宿
  List<LodgingsBean> findByVendorIdAndDeleteStatus(Integer vendorId, Integer deleteStatus);

  // 根據供應商ID和刪除狀態查詢旅宿，預載入關聯資料
  @Query("SELECT DISTINCT l FROM LodgingsBean l " +
      "LEFT JOIN FETCH l.city " +
      "LEFT JOIN FETCH l.country " +
      "LEFT JOIN FETCH l.lodgingType " +
      "WHERE l.vendor.id = :vendorId AND l.deleteStatus = :deleteStatus")
  List<LodgingsBean> findByVendorIdAndDeleteStatusWithJoins(
      @Param("vendorId") Integer vendorId,
      @Param("deleteStatus") Integer deleteStatus);

  // 根據供應商ID、啟用狀態和刪除狀態查詢旅宿，預載入關聯資料
  @Query("SELECT DISTINCT l FROM LodgingsBean l " +
      "LEFT JOIN FETCH l.city " +
      "LEFT JOIN FETCH l.country " +
      "LEFT JOIN FETCH l.lodgingType " +
      "WHERE l.vendor.id = :vendorId " +
      "AND (:isActive IS NULL OR l.isActive = :isActive) " +
      "AND (:deleteStatus IS NULL OR l.deleteStatus = :deleteStatus)")
  List<LodgingsBean> findByVendorIdAndStatusWithJoins(
      @Param("vendorId") Integer vendorId,
      @Param("isActive") Boolean isActive,
      @Param("deleteStatus") Integer deleteStatus);

  // 根據供應商ID查詢所有旅宿（不分狀態），預載入關聯資料
  @Query("SELECT DISTINCT l FROM LodgingsBean l " +
      "LEFT JOIN FETCH l.city " +
      "LEFT JOIN FETCH l.country " +
      "LEFT JOIN FETCH l.lodgingType " +
      "WHERE l.vendor.id = :vendorId")
  List<LodgingsBean> findByVendorIdWithJoins(@Param("vendorId") Integer vendorId);

  // 根據ID和供應商ID查詢旅宿（用於狀態管理）
  @Query("SELECT l FROM LodgingsBean l " +
      "WHERE l.id = :lodgingId AND l.vendor.id = :vendorId")
  Optional<LodgingsBean> findByIdAndVendorId(
      @Param("lodgingId") Integer lodgingId,
      @Param("vendorId") Integer vendorId);

  // 輕量查詢：只載入基本資訊，避免關聯載入
  @Query("SELECT l FROM LodgingsBean l " +
      "WHERE l.id = :id AND l.isActive = true AND l.deleteStatus = 1")
  Optional<LodgingsBean> findByIdLightweight(@Param("id") Integer id);

}
